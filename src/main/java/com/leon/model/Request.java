package com.leon.model;

import java.util.Objects;

public class Request
{
    private char side;
    private long quantity;
    private String stockCode;
    private int clientCode;
    private String orderId;
    private int sequenceId;
    private String requestType;
    private String requestSubType;

    public Request(char side, long quantity, String stockCode, int clientCode, String orderId, int sequenceId, String requestType, String requestSubType)
    {
        this.side = side;
        this.quantity = quantity;
        this.stockCode = stockCode;
        this.clientCode = clientCode;
        this.orderId = orderId;
        this.sequenceId = sequenceId;
        this.requestType = requestType;
        this.requestSubType = requestSubType;
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

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode)
    {
        this.stockCode = stockCode;
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

    public String getRequestSubType()
    {
        return requestSubType;
    }

    public void setRequestSubType(String requestSubType)
    {
        this.requestSubType = requestSubType;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return getSide() == request.getSide() && getQuantity() == request.getQuantity() && getClientCode() == request.getClientCode()
                && getSequenceId() == request.getSequenceId() && getStockCode().equals(request.getStockCode()) && Objects.equals(getOrderId(), request.getOrderId())
                && getRequestType().equals(request.getRequestType()) && Objects.equals(getRequestSubType(), request.getRequestSubType());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getSide(), getQuantity(), getStockCode(), getClientCode(), getOrderId(), getSequenceId(), getRequestType(), getRequestSubType());
    }

    @Override
    public String toString()
    {
        return "Request{" +
                "side=" + side +
                ", quantity=" + quantity +
                ", stockCode='" + stockCode + '\'' +
                ", clientCode=" + clientCode +
                ", orderId='" + orderId + '\'' +
                ", sequenceId=" + sequenceId +
                ", requestType='" + requestType + '\'' +
                ", requestSubType='" + requestSubType + '\'' +
                '}';
    }
}
