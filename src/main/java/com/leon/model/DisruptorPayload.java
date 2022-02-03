package com.leon.model;

import java.util.Objects;
import java.util.UUID;

public class DisruptorPayload
{
    private String payloadType;
    private String payload;
    private String uid;

    public DisruptorPayload(String payloadType, String payload)
    {
        this.payloadType = payloadType;
        this.payload = payload;
        this.uid = UUID.randomUUID().toString();
    }

    public DisruptorPayload(String payloadType, String payload, String uid)
    {
        this.payloadType = payloadType;
        this.payload = payload;
        this.uid = uid;
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

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    @Override
    public String toString()
    {
        return "DisruptorPayload{" + "payloadType='" + payloadType + '\'' + ", payload='" + payload + '\'' + ", uid='" + uid + '\'' + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisruptorPayload that = (DisruptorPayload) o;
        return getPayloadType().equals(that.getPayloadType()) && getPayload().equals(that.getPayload()) && getUid().equals(that.getUid());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getPayloadType(), getPayload(), getUid());
    }
}
