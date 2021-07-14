package com.leon.disruptor;

import com.leon.service.DistruptorEventType;
import com.leon.service.OutcomeType;

import java.util.Objects;

public class PositionResponse
{
    private double reservedQuantity;
    private OutcomeType outcomeType;
    private int clientID;
    private String RIC;
    private DistruptorEventType eventType;
    private DistruptorEventRequestType requestType;

    public PositionResponse(double reservedQuantity, OutcomeType outcomeType, int clientID, String RIC, DistruptorEventType eventType, DistruptorEventRequestType requestType)
    {
        this.reservedQuantity = reservedQuantity;
        this.outcomeType = outcomeType;
        this.clientID = clientID;
        this.RIC = RIC;
        this.eventType = eventType;
        this.requestType = requestType;
    }

    @Override
    public String toString()
    {
        return "PositionResponse{" +
                "reservedQuantity=" + reservedQuantity +
                ", outcomeType=" + outcomeType +
                ", clientID='" + clientID + '\'' +
                ", RIC='" + RIC + '\'' +
                ", eventType=" + eventType +
                ", requestType=" + requestType +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        PositionResponse that = (PositionResponse) o;
        return Double.compare(that.getReservedQuantity(), getReservedQuantity()) == 0 &&
                getOutcomeType() == that.getOutcomeType() &&
                getClientID() == that.getClientID() &&
                getRIC().equals(that.getRIC()) &&
                getEventType() == that.getEventType() &&
                getRequestType() == that.getRequestType();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getReservedQuantity(), getOutcomeType(), getClientID(), getRIC(), getEventType(), getRequestType());
    }

    public double getReservedQuantity()
    {
        return reservedQuantity;
    }

    public void setReservedQuantity(double reservedQuantity)
    {
        this.reservedQuantity = reservedQuantity;
    }

    public OutcomeType getOutcomeType()
    {
        return outcomeType;
    }

    public void setOutcomeType(OutcomeType outcomeType)
    {
        this.outcomeType = outcomeType;
    }

    public int getClientID()
    {
        return clientID;
    }

    public void setClientID(int clientID)
    {
        this.clientID = clientID;
    }

    public String getRIC()
    {
        return RIC;
    }

    public void setRIC(String RIC)
    {
        this.RIC = RIC;
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
}
