package com.leon.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leon.service.PayloadServiceFactory;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionEventHandler implements EventHandler<DistruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(PositionEventHandler.class);
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public void onEvent(DistruptorEvent event, long sequence, boolean endOfBatch)
    {
        try
        {
            logger.info(mapper.writeValueAsString(PayloadServiceFactory.getInstance(event.getPayload().getPayloadType())));
        }
        catch(JsonProcessingException jpe)
        {
            jpe.printStackTrace();
        }
    }
}
