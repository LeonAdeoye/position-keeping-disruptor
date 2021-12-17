package com.leon.model;

import com.lmax.disruptor.EventFactory;

public class DisruptorEventFactory implements EventFactory<DisruptorEvent>
{
    public DisruptorEvent newInstance()
    {
        return new DisruptorEvent();
    }
}
