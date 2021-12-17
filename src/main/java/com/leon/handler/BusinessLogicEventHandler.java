package com.leon.handler;

import com.leon.service.DisruptorService;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leon.model.DisruptorEvent;

public class BusinessLogicEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicEventHandler.class);
    private DisruptorService outboundDisruptor;

    public BusinessLogicEventHandler(DisruptorService outboundDisruptor)
    {
        this.outboundDisruptor = outboundDisruptor;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        outboundDisruptor.push(event.getPayload());
    }
}
