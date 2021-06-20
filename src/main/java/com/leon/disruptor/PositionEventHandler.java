package com.leon.disruptor;

import com.lmax.disruptor.EventHandler;

public class PositionEventHandler implements EventHandler<DistruptorEvent>
{
    public void onEvent(DistruptorEvent event, long sequence, boolean endOfBatch)
    {
        System.out.println(PositionServiceFactory.getInstance(event.getPositionRequest().getEventType()).check(event.getPositionRequest()));
    }
}
