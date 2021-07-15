package com.leon.event;

import com.leon.event.DistruptorEvent;
import com.lmax.disruptor.EventFactory;

public class DistruptorEventFactory implements EventFactory<DistruptorEvent>
{
    public DistruptorEvent newInstance()
    {
        return new DistruptorEvent();
    }
}

