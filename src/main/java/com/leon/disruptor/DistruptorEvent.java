package com.leon.disruptor;

// To get started with the Disruptor we are going to consider very simple and contrived example,
// one that will pass a single long value from a producer to a consumer, where the consumer will
// simply print out the value. Firstly we will define the Event that will carry the data.

public class DistruptorEvent
{
    private PositionRequest positionRequest;

    public PositionRequest getPositionRequest()
    {
        return positionRequest;
    }

    public void setPositionRequest(PositionRequest positionRequest)
    {
        this.positionRequest = positionRequest;
    }

    @Override
    public String toString()
    {
        return "DistruptorEvent{" +
                "request=" + positionRequest +
                '}';
    }
}
