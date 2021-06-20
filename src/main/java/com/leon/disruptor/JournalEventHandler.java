package com.leon.disruptor;

import com.lmax.disruptor.EventHandler;

public class JournalEventHandler implements EventHandler<DistruptorEvent>
{
    public void onEvent(DistruptorEvent event, long sequence, boolean endOfBatch)
    {
        System.out.println("Journal writing handler consumes: " + event);
    }
}
