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

    @Override
    public void start()
    {
        requestReader.initialize(configurationService.getReaderFilePath());
        responseWriter.initialize(configurationService);
        logger.info("Initialized reader and writer.");
        businessLogicEventHandler = new BusinessLogicEventHandler(outboundDisruptor, configurationService.getStartOfDayInventoryPositionFilePath());
        inboundDisruptor.start("INBOUND", new InboundJournalEventHandler(), businessLogicEventHandler);
        outboundDisruptor.start("OUTBOUND", new OutboundJournalEventHandler(), new PublishingEventHandler(responseWriter) );
        requestReader.readAll().subscribe((request) -> inboundDisruptor.push(request));
    }

    @Override
    public void stop()
    {
        businessLogicEventHandler.close();
        inboundDisruptor.stop();
        outboundDisruptor.stop();
        requestReader.close();
        responseWriter.close();
        logger.info("Shutdown and cleanup completed.");
    }
}
