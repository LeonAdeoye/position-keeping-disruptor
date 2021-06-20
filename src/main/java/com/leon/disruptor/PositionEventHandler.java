package com.leon.disruptor;

import com.lmax.disruptor.EventHandler;

public class PositionEventHandler implements EventHandler<DistruptorEvent>
{
    public void onEvent(DistruptorEvent event, long sequence, boolean endOfBatch)
    {
        PositionServiceFactory.getInstance(event.getPositionRequest().getEventType()).check(event.getPositionRequest());

        if(event.getPositionRequest().getClientId() % 5000000 == 0)
            System.out.println(event);
    }
}
