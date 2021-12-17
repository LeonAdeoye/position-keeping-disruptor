package com.leon.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leon.model.DisruptorEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OutboundJournalEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(OutboundJournalEventHandler.class);

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try
        {
            logger.info(mapper.writeValueAsString(event.getPayload()));
        }
        catch(JsonProcessingException jpe)
        {
            jpe.printStackTrace();
        }
    }
}
