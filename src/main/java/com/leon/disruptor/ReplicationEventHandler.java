package com.leon.disruptor;

import com.lmax.disruptor.EventHandler;

public class ReplicationEventHandler implements EventHandler<DistruptorEvent>
{
    public void onEvent(DistruptorEvent event, long sequence, boolean endOfBatch)
    {
        System.out.println("Replication handler consumes: " + event);
    }
}
