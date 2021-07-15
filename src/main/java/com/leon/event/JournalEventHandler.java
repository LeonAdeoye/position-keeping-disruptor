package com.leon.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JournalEventHandler implements EventHandler<DistruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(JournalEventHandler.class);

    public void onEvent(DistruptorEvent event, long sequence, boolean endOfBatch)
    {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try
        {
            logger.info(mapper.writeValueAsString(event.getPositionRequest()));
        }
        catch(JsonProcessingException jpe)
        {
            jpe.printStackTrace();
        }
    }
}
