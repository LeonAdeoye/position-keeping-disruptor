package com.leon.service;

import com.leon.handler.InboundJournalEventHandler;
import com.leon.handler.OutboundJournalEventHandler;
import com.leon.handler.BusinessLogicEventHandler;
import com.leon.handler.PublishingEventHandler;
import com.leon.io.FileDisruptorReader;
import com.leon.io.FileDisruptorWriter;
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
    private FileDisruptorReader fileReader;
    @Autowired
    private FileDisruptorWriter fileWriter;
    @Autowired
    private MessageService messageService;
    private BusinessLogicEventHandler businessLogicEventHandler;

    @Override
    public void start()
    {
        businessLogicEventHandler = new BusinessLogicEventHandler(outboundDisruptor);
        messageService.setReader(fileReader);
        messageService.setWriter(fileWriter);
        inboundDisruptor.start("INBOUND", new InboundJournalEventHandler(), businessLogicEventHandler, messageService);
        outboundDisruptor.start("OUTBOUND", new OutboundJournalEventHandler(), new PublishingEventHandler(), messageService);
        logger.info("Started inbound and outbound disruptor.");
    }

    @Override
    public void stop()
    {
        businessLogicEventHandler.close();
        inboundDisruptor.stop();
        outboundDisruptor.stop();
        logger.info("Halted inbound and outbound disruptors.");
    }
}
