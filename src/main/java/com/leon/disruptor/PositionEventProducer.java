package com.leon.disruptor;

import com.leon.event.DistruptorEvent;
import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionEventProducer
{
    private static final Logger logger = LoggerFactory.getLogger(PositionEventProducer.class);

    private final RingBuffer<DistruptorEvent> ringBuffer;

    public PositionEventProducer(RingBuffer<DistruptorEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    public void onData(PositionRequest positionRequest)
    {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try
        {
            DistruptorEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            // for the sequence
            event.setPositionRequest(positionRequest);  // Fill with data
        }
        finally
        {
            ringBuffer.publish(sequence);
        }
    }
}