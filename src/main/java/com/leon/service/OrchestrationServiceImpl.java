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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
    private HeartBeatService heartbeatService;

    private InventoryCheckEventHandler inventoryCheckEventHandler;
    @Autowired
    private DisruptorReader requestReader;
    @Autowired
    private DisruptorWriter responseWriter;

    @Value("${inbound.journal.recovery.file.path}")
    private String inboundJournalRecoveryFilePath;
    @Value("${chronicle.map.file.path}")
    private String chronicleMapFilePath;
    private boolean hasStarted = false;

    @Override
    public void initialize()
    {
        inventoryCheckEventHandler = new InventoryCheckEventHandler(outboundDisruptor, instrumentService, fxService);
        inventoryCheckEventHandler.start(chronicleMapFilePath);
        inboundDisruptor.start("INBOUND", new InboundJournalEventHandler(), inventoryCheckEventHandler);
        outboundDisruptor.start("OUTBOUND", new OutboundJournalEventHandler(), new PublishingEventHandler(responseWriter));
        heartbeatService.start(true);
        logger.info("Completed initialization of components with isPrimary mode = " + configurationService.isPrimary());
    }

    @Override
    public void start()
    {
        if(!hasStarted)
        {
            logger.info("Now starting to listen to inbound requests...");
            requestReader.start();
            requestReader.readAll().subscribe(inboundDisruptor::push, err -> logger.error(err.getMessage()));
            hasStarted = true;
        }
        else
            logger.error("Cannot start components because they have already been started.");
    }

    @Override
    public void stop()
    {
        if(hasStarted)
        {
            inventoryCheckEventHandler.stop();
            inboundDisruptor.stop();
            outboundDisruptor.stop();
            requestReader.stop();
            heartbeatService.stop();
            logger.info("Shutdown and cleanup completed.");
        }
        else
            logger.error("Cannot stop components because they have not been started correctly.");
    }

    @Override
    
    public void upload(String sodFilePath)
    {
        if(!hasStarted)
            inventoryCheckEventHandler.uploadSODPositions(sodFilePath);
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

    @Override
    public boolean togglePrimary()
    {
        boolean isPrimary = responseWriter.togglePrimary();
        configurationService.setPrimary(isPrimary);
        heartbeatService.start(isPrimary);
        logger.info("After toggling, the configuration of isPrimary mode is set to: " + isPrimary);
        return isPrimary;
    }
}
