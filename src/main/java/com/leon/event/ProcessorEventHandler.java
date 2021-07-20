package com.leon.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessorEventHandler implements EventHandler<DistruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(ProcessorEventHandler.class);
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public void onEvent(DistruptorEvent event, long sequence, boolean endOfBatch)
    {
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
