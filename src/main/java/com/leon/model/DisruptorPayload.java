package com.leon.model;

import java.util.Objects;

public class DisruptorPayload
{
    private String payloadType;
    private String payload;

    public DisruptorPayload(String payloadType, String payload)
    {
        this.payloadType = payloadType;
        this.payload = payload;
    }

    public String getPayloadType()
    {
        return payloadType;
    }

    public void setPayloadType(String payloadType)
    {
        this.payloadType = payloadType;
    }

    public String getPayload()
    {
        return payload;
    }

    public void setPayload(String payload)
    {
        this.payload = payload;
    }

    @Override
    public String toString()
    {
        return "DisruptorPayload{" + "payloadType='" + payloadType + '\'' + ", payload='" + payload + '\'' + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisruptorPayload that = (DisruptorPayload) o;
        return getPayloadType().equals(that.getPayloadType()) && getPayload().equals(that.getPayload());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getPayloadType(), getPayload());
    }
}
