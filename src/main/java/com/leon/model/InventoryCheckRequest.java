package com.leon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties
public class InventoryCheckRequest
{
    @JsonProperty("side")
    private char side;
    @JsonProperty("quantity")
    private long quantity;
    @JsonProperty("stockCode")
    private String instrumentId;
    @JsonProperty("clientCode")
    private int clientCode;
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("sequenceId")
    private int sequenceId;
    @JsonProperty("requestType")
    private String requestType;
    public InventoryCheckRequest(char side, long quantity, String instrumentId, int clientCode, String orderId, int sequenceId, String requestType)
    {
        this.side = side;
        this.quantity = quantity;
        this.instrumentId = instrumentId;
        this.clientCode = clientCode;
        this.orderId = orderId;
        this.sequenceId = sequenceId;
        this.requestType = requestType;
    }

    public InventoryCheckRequest()
    {

    }

    public char getSide()
    {
        return side;
    }

    public void setSide(char side)
    {
        this.side = side;
    }

    public long getQuantity()
    {
        return quantity;
    }

    public void setQuantity(long quantity)
    {
        this.quantity = quantity;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId)
    {
        this.instrumentId = instrumentId;
    }

    public int getClientCode()
    {
        return clientCode;
    }

    public void setClientCode(int clientCode)
    {
        this.clientCode = clientCode;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public int getSequenceId()
    {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId)
    {
        this.sequenceId = sequenceId;
    }

    public String getRequestType()
    {
        return requestType;
    }

    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryCheckRequest request = (InventoryCheckRequest) o;
        return getSide() == request.getSide() && getQuantity() == request.getQuantity() && getClientCode() == request.getClientCode()
                && getSequenceId() == request.getSequenceId() && getInstrumentId() == request.getInstrumentId() && Objects.equals(getOrderId(), request.getOrderId())
                && getRequestType().equals(request.getRequestType());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getSide(), getQuantity(), getInstrumentId(), getClientCode(), getOrderId(), getSequenceId(), getRequestType());
    }

    @Override
    public String toString()
    {
        return "Request{" +
                "side=" + side +
                ", quantity=" + quantity +
                ", instrumentId='" + instrumentId  +
                ", clientCode=" + clientCode +
                ", orderId='" + orderId +
                ", sequenceId=" + sequenceId +
                ", requestType='" + requestType +
                '}';
    }
}
