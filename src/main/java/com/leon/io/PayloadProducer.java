package com.leon.io;

import com.leon.event.DistruptorEvent;
import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayloadProducer
{
    private static final Logger logger = LoggerFactory.getLogger(PayloadProducer.class);

    private final RingBuffer<DistruptorEvent> ringBuffer;

    public PayloadProducer(RingBuffer<DistruptorEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    public void onData(Payload payload)
    {
        long sequence = ringBuffer.next();  // Grab the next sequence
        try
        {
            DistruptorEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            // for the sequence
            event.setPayload(payload);  // Fill with data
        }
        finally
        {
            ringBuffer.publish(sequence);
        }
    }
}