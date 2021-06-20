package com.leon.disruptor;

import com.lmax.disruptor.EventFactory;

public class DistruptorEventFactory implements EventFactory<DistruptorEvent>
{
    public DistruptorEvent newInstance()
    {
        return new DistruptorEvent();
    }
}

