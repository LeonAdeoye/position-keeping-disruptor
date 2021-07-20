package com.leon.event;

// To get started with the Disruptor we are going to consider very simple and contrived example,
// one that will pass a single long value from a producer to a consumer, where the consumer will
// simply print out the value. Firstly we will define the Event that will carry the data.

import com.leon.io.Payload;

public class DistruptorEvent
{
    private Payload payload;

    public Payload getPayload()
    {
        return payload;
    }

    public void setPayload(Payload payload)
    {
        this.payload = payload;
    }

    @Override
    public String toString()
    {
        return "DistruptorEvent{" +
                "payload=" + payload +
                '}';
    }
}
