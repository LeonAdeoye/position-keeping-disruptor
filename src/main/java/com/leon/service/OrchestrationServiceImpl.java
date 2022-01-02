package com.leon.service;

import com.leon.handler.InboundJournalEventHandler;
import com.leon.handler.OutboundJournalEventHandler;
import com.leon.handler.BusinessLogicEventHandler;
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
    private DisruptorReader reader;
    @Autowired
    private DisruptorWriter writer;
    @Autowired
    private MessageService messageService;

    private BusinessLogicEventHandler businessLogicEventHandler;

    @Override
    public void start()
    {
        reader.initialize(configurationService);
        writer.initialize(configurationService);
        messageService.setReader(reader);
        messageService.setWriter(writer);
        logger.info("Initialized reading and writing.");

        inboundDisruptor.start("INBOUND", new InboundJournalEventHandler(), new BusinessLogicEventHandler(outboundDisruptor));
        outboundDisruptor.start("OUTBOUND", new OutboundJournalEventHandler(), new PublishingEventHandler(writer) );
        logger.info("Started inbound and outbound disruptors.");

        reader.readAll().subscribe((request) -> inboundDisruptor.push(request));
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
