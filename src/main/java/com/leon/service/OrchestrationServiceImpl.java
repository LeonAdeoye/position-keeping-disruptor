package com.leon.service;

import com.leon.handler.InboundJournalEventHandler;
import com.leon.handler.InventoryCheckEventHandler;
import com.leon.handler.OutboundJournalEventHandler;
import com.leon.handler.PublishingEventHandler;
import com.leon.io.DisruptorReader;
import com.leon.io.DisruptorWriter;
import com.leon.model.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrchestrationServiceImpl implements OrchestrationService
{
    private static final Logger logger = LoggerFactory.getLogger(OrchestrationServiceImpl.class);
    @Autowired
    private DisruptorService inboundDisruptor;
    @Autowired
    private DisruptorService outboundDisruptor;
    @Autowired
    private ConfigurationServiceImpl configurationService;
    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private FxService fxService;
    @Autowired
    private BeanFactory beanFactory;

    private InventoryCheckEventHandler inventoryCheckEventHandler;
    private boolean uploaded = false;
    private boolean stopped = false;
    private boolean started = false;
    private DisruptorReader requestReader;
    private DisruptorWriter responseWriter;

    @Value("${disruptor.reader.class}")
    private String disruptorReaderClass;
    @Value("${disruptor.writer.class}")
    private String disruptorWriterClass;

    @PostConstruct
    public void initialization()
    {
        inventoryCheckEventHandler = new InventoryCheckEventHandler(outboundDisruptor, instrumentService, fxService);
        inventoryCheckEventHandler.start(configurationService.getChronicleMapFilePath());
        logger.info("Initialization completed.");
        uploaded = true;
    }

    @Override
    public void start()
    {
        if(!started)
        {
            responseWriter = beanFactory.getBean(disruptorWriterClass, DisruptorWriter.class);
            requestReader = beanFactory.getBean(disruptorReaderClass, DisruptorReader.class);

            inboundDisruptor.start("INBOUND", new InboundJournalEventHandler(), inventoryCheckEventHandler);
            outboundDisruptor.start("OUTBOUND", new OutboundJournalEventHandler(), new PublishingEventHandler(responseWriter));

            requestReader.start();
            requestReader.readAll().subscribe((request) -> inboundDisruptor.push(request));

            uploaded = false;
            started = true;
            logger.info("All components started.");
        }
        else
            logger.error("Cannot start components because they have already been started.");
    }

    @Override
    public void stop()
    {
        if(!stopped)
        {
            inventoryCheckEventHandler.stop();
            inboundDisruptor.stop();
            outboundDisruptor.stop();
            requestReader.stop();
            uploaded = false;
            stopped = true;
            logger.info("Shutdown and cleanup completed.");
        }
        else
            logger.error("Cannot stop components because they have already been stopped.");
    }

    // TODO - system needs to be able to reload SOD positions at anytime.

    @Override
    public void upload(String sodFilePath)
    {
        if(inventoryCheckEventHandler != null && uploaded)
        {
            inventoryCheckEventHandler.uploadSODPositions(sodFilePath);
            uploaded = false;
        }
        else
            logger.error("Cannot upload SOD file because orchestration service is not in the right state.");
    }

    @Override
    public List<Inventory> getInventory()
    {
        return inventoryCheckEventHandler == null ? new ArrayList<>() : inventoryCheckEventHandler.getInventory();
    }

    @Override
    public void clearInventory()
    {
        inventoryCheckEventHandler.clearInventory();
    }

    @Override
    public void updateInventory(Inventory inventory)
    {
        inventoryCheckEventHandler.updateInventory(inventory);
    }

    @Override
    public void deleteInventory(Inventory inventory)
    {
        inventoryCheckEventHandler.deleteInventory(inventory);
    }
}
