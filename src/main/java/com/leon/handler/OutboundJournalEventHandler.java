package com.leon.handler;

import com.leon.model.DisruptorEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutboundJournalEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(OutboundJournalEventHandler.class);

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        logger.debug(event.getPayload().getPayload());
    }
}
