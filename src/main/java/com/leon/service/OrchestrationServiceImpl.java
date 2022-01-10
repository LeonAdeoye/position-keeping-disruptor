package com.leon.service;

import com.leon.handler.BusinessLogicEventHandler;
import com.leon.handler.InboundJournalEventHandler;
import com.leon.handler.OutboundJournalEventHandler;
import com.leon.handler.PublishingEventHandler;
import com.leon.io.DisruptorReader;
import com.leon.io.DisruptorWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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
    private DisruptorReader requestReader;
    @Autowired
    private DisruptorWriter responseWriter;
    private BusinessLogicEventHandler businessLogicEventHandler;

    boolean started = false;
    boolean stopped = false;

    @PostConstruct
    public void initialization()
    {
        businessLogicEventHandler = new BusinessLogicEventHandler(outboundDisruptor);
        logger.info("Initialization completed.");
    }

    @Override
    public void start()
    {
        if(!started)
        {
            requestReader.initialize(configurationService.getReaderFilePath());
            responseWriter.initialize(configurationService);
            inboundDisruptor.start("INBOUND", new InboundJournalEventHandler(), businessLogicEventHandler);
            outboundDisruptor.start("OUTBOUND", new OutboundJournalEventHandler(), new PublishingEventHandler(responseWriter) );
            requestReader.readAll().subscribe((request) -> inboundDisruptor.push(request));
            started = true;
            stopped = false;
            logger.info("Starting of disruptors completed.");
        }
        else
            logger.error("Cannot start components because they have already been started.");
    }

    @Override
    public void stop()
    {
        if(!stopped)
        {
            businessLogicEventHandler.close();
            inboundDisruptor.stop();
            outboundDisruptor.stop();
            requestReader.close();
            responseWriter.close();
            started = false;
            stopped = true;
            logger.info("Shutdown and cleanup completed.");
        }
        else
            logger.error("Cannot stop components because they have already been stopped.");
    }

    @Override
    public void upload(String sodFilePath)
    {
        if(businessLogicEventHandler != null && !started && !stopped)
            businessLogicEventHandler.uploadSODPositions(sodFilePath);
        else
            logger.error("Upload cannot run when orchestration service is in an invalid state.");
    }
}
