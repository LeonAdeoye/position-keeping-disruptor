package com.leon.service;

import com.leon.handler.InboundJournalEventHandler;
import com.leon.handler.OutboundJournalEventHandler;
import com.leon.handler.BusinessLogicEventHandler;
import com.leon.handler.PublishingEventHandler;
import com.leon.io.DisruptorReader;
import com.leon.io.FileDisruptorReader;
import com.leon.io.FileDisruptorWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class OrchestrationServiceImpl implements OrchestrationService
{
    private static final Logger logger = LoggerFactory.getLogger(OrchestrationServiceImpl.class);

    @Autowired
    private DisruptorService inboundDisruptor;
    @Autowired
    private DisruptorService outboundDisruptor;
    @Autowired
    ConfigurationServiceImpl configurationService;
    @Autowired
    FileDisruptorReader fileReader;
    @Autowired
    FileDisruptorWriter fileWriter;
    @Autowired
    MessageService messageService;



    @Override
    public void start()
    {
        messageService.setReader(fileReader);
        messageService.setWriter(fileWriter);
        inboundDisruptor.start("INBOUND", new InboundJournalEventHandler(), new BusinessLogicEventHandler(outboundDisruptor), messageService);
        outboundDisruptor.start("OUTBOUND", new OutboundJournalEventHandler(), new PublishingEventHandler(), messageService);
        logger.info("Started inbound and outbound disruptor.");
    }

    @Override
    public void stop()
    {

        inboundDisruptor.stop();
        outboundDisruptor.stop();
        logger.info("Halted inbound and outbound disruptors.");
    }
}
