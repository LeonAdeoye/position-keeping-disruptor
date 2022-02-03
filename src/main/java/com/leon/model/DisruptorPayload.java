package com.leon.model;

import java.util.Objects;
import java.util.UUID;

public class DisruptorPayload
{
    private String payloadType;
    private String payload;
    private String uid;
    private long createdInstant;

    public DisruptorPayload(String payloadType, String payload)
    {
        this.payloadType = payloadType;
        this.payload = payload;
        this.uid = UUID.randomUUID().toString();
        this.createdInstant = System.nanoTime();

    }

    public DisruptorPayload(String payloadType, String payload, String uid, long createdInstant)
    {
        this.payloadType = payloadType;
        this.payload = payload;
        this.uid = uid;
        this.createdInstant = createdInstant;
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

    public long getCreatedInstant()
    {
        return createdInstant;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisruptorPayload that = (DisruptorPayload) o;
        return getPayloadType().equals(that.getPayloadType()) && getPayload().equals(that.getPayload()) && getUid().equals(that.getUid()) && getCreatedInstant() == that.getCreatedInstant();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getPayloadType(), getPayload(), getUid(), getCreatedInstant());
    }

    @Override
    public String toString()
    {
        return "DisruptorPayload{" + "payloadType='" + payloadType + '\'' + ", payload='" + payload + '\'' + ", uid='" + uid + '\'' + ", createdInstant=" + createdInstant + '}';
    }
}
