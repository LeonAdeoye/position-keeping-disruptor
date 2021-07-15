package com.leon.disruptor;

import com.leon.event.DistruptorEventRequestType;
import com.leon.service.DistruptorEventType;

import java.time.LocalDateTime;
import java.util.Objects;

public class PositionRequest
{
    private int clientId;
    private String RIC;
    private double delta;
    private DistruptorEventType eventType;
    private DistruptorEventRequestType requestType;
    private LocalDateTime timeStamp;

    public PositionRequest(int clientId, String RIC, double delta, DistruptorEventType eventType, DistruptorEventRequestType requestType)
    {
        this.clientId = clientId;
        this.RIC = RIC;
        this.delta = delta;
        this.eventType = eventType;
        this.requestType = requestType;
        this.timeStamp = LocalDateTime.now();
    }

    public PositionRequest(int clientId, String RIC, double delta, DistruptorEventType eventType, DistruptorEventRequestType requestType, LocalDateTime timeStamp)
    {
        this.clientId = clientId;
        this.RIC = RIC;
        this.delta = delta;
        this.eventType = eventType;
        this.requestType = requestType;
        this.timeStamp = timeStamp;
    }

    public static PositionRequest lockCashPosition(int clientID, double delta)
    {
        return new PositionRequest(clientID, null, delta, DistruptorEventType.CASH, DistruptorEventRequestType.LOCK);
    }

    public static PositionRequest unlockCashPosition(int clientID, double delta)
    {
        return new PositionRequest(clientID, null, delta, DistruptorEventType.CASH, DistruptorEventRequestType.UNLOCK);
    }

    public static PositionRequest lockStockPosition(int clientID, String RIC, double delta)
    {
        return new PositionRequest(clientID, RIC, delta, DistruptorEventType.STOCK, DistruptorEventRequestType.LOCK);
    }

    public static PositionRequest unlockStockPosition(int clientID, String RIC, double delta)
    {
        return new PositionRequest(clientID, RIC, delta, DistruptorEventType.STOCK, DistruptorEventRequestType.UNLOCK);
    }

    public int getClientId()
    {
        return clientId;
    }

    public void setClientId(int clientId)
    {
        this.clientId = clientId;
    }

    public String getRIC()
    {
        return RIC;
    }

    public void setRIC(String RIC)
    {
        this.RIC = RIC;
    }

    public double getDelta()
    {
        return delta;
    }

    public void setDelta(double delta)
    {
        this.delta = delta;
    }

    public DistruptorEventType getEventType()
    {
        return eventType;
    }

    public void setEventType(DistruptorEventType eventType)
    {
        this.eventType = eventType;
    }

    public DistruptorEventRequestType getRequestType()
    {
        return requestType;
    }

    public void setRequestType(DistruptorEventRequestType requestType)
    {
        this.requestType = requestType;
    }

    public LocalDateTime getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        PositionRequest that = (PositionRequest) o;
        return getClientId() == that.getClientId() &&
                Double.compare(that.getDelta(), getDelta()) == 0 &&
                getRIC().equals(that.getRIC()) &&
                getEventType() == that.getEventType() &&
                getRequestType() == that.getRequestType() &&
                getTimeStamp().equals(that.getTimeStamp());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getClientId(), getRIC(), getDelta(), getEventType(), getRequestType(), getTimeStamp());
    }

    @Override
    public String toString()
    {
        return "PositionRequest{" +
                "clientId=" + clientId +
                ", RIC='" + RIC + '\'' +
                ", delta=" + delta +
                ", eventType=" + eventType +
                ", requestType=" + requestType +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
