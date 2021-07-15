package com.leon.event;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplicationEventHandler implements EventHandler<DistruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(ReplicationEventHandler.class);

    public void onEvent(DistruptorEvent event, long sequence, boolean endOfBatch)
    {
        System.out.println("Replication handler consumes: " + event);
    }
}
