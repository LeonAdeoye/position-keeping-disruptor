package com.leon.model;

public class DisruptorPayload
{
    private String payloadType;

    public DisruptorPayload(String payloadType)
    {
        this.payloadType = payloadType;
    }

    public String getPayloadType()
    {
        return payloadType;
    }

    public void setPayloadType(String payloadType)
    {
        this.payloadType = payloadType;
    }

    @Override
    public String toString()
    {
        return "DisruptorPayload { payloadType='" + payloadType + "'}";
    }
}
