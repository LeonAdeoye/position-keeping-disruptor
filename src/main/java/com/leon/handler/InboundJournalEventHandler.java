package com.leon.handler;

import com.leon.model.DisruptorEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboundJournalEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(InboundJournalEventHandler.class);

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        //logger.info(event.getPayload().getPayloadType() + "=" + event.getPayload().getPayload());
    }
}
