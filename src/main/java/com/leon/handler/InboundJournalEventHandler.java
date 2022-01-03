package com.leon.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leon.model.DisruptorEvent;

public class InboundJournalEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(InboundJournalEventHandler.class);

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        logger.debug(event.getPayload().toString());
    }
}
