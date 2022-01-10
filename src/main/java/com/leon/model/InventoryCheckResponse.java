package com.leon.model;

import java.util.Objects;

public class InventoryCheckResponse
{
	private int reservedQuantity;
	private int outstandingQuantity;
	private String result;
	private String orderId;
	private int clientId;
	private String stockCode;
	private char side;
	private double reservedCash;
	private double outstandingCash;
	private String requestType;
	private String requestSubType;

	public InventoryCheckResponse(int reservedQuantity, int outstandingQuantity, String result, String orderId, int clientId, String stockCode, char side, double reservedCash, double outstandingCash, String requestType, String requestSubType)
	{
		this.reservedQuantity = reservedQuantity;
		this.outstandingQuantity = outstandingQuantity;
		this.result = result;
		this.orderId = orderId;
		this.clientId = clientId;
		this.stockCode = stockCode;
		this.side = side;
		this.reservedCash = reservedCash;
		this.outstandingCash = outstandingCash;
		this.requestType = requestType;
		this.requestSubType = requestSubType;
	}

	public InventoryCheckResponse()	{}

	public int getReservedQuantity()
	{
		return reservedQuantity;
	}

	public void setReservedQuantity(int reservedQuantity)
	{
		this.reservedQuantity = reservedQuantity;
	}

	public int getOutstandingQuantity()
	{
		return outstandingQuantity;
	}

	public void setOutstandingQuantity(int outstandingQuantity)
	{
		this.outstandingQuantity = outstandingQuantity;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public int getClientId()
	{
		return clientId;
	}

	public void setClientId(int clientId)
	{
		this.clientId = clientId;
	}

	public String getStockCode()
	{
		return stockCode;
	}

	public void setStockCode(String stockCode)
	{
		this.stockCode = stockCode;
	}

	public char getSide()
	{
		return side;
	}

	public void setSide(char side)
	{
		this.side = side;
	}

	public double getReservedCash()
	{
		return reservedCash;
	}

	public void setReservedCash(double reservedCash)
	{
		this.reservedCash = reservedCash;
	}

	public double getOutstandingCash()
	{
		return outstandingCash;
	}

	public void setOutstandingCash(double outstandingCash)
	{
		this.outstandingCash = outstandingCash;
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
	public String toString()
	{
		return "InventoryCheckResponse{" + "reservedQuantity=" + reservedQuantity + ", outstandingQuantity=" + outstandingQuantity + ", result='" + result + '\'' + ", orderId='" + orderId + '\'' + ", clientId=" + clientId + ", stockCode='" + stockCode + '\'' + ", side=" + side + ", reservedCash=" + reservedCash + ", outstandingCash=" + outstandingCash + ", requestType='" + requestType + '\'' + ", requestSubType='" + requestSubType + '\'' + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InventoryCheckResponse that = (InventoryCheckResponse) o;
		return getReservedQuantity() == that.getReservedQuantity() && getOutstandingQuantity() == that.getOutstandingQuantity() && getClientId() == that.getClientId() && getSide() == that.getSide() && Double.compare(that.getReservedCash(), getReservedCash()) == 0 && Double.compare(that.getOutstandingCash(), getOutstandingCash()) == 0 && getResult().equals(that.getResult()) && getOrderId().equals(that.getOrderId()) && getStockCode().equals(that.getStockCode()) && getRequestType().equals(that.getRequestType()) && getRequestSubType().equals(that.getRequestSubType());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getReservedQuantity(), getOutstandingQuantity(), getResult(), getOrderId(), getClientId(), getStockCode(), getSide(), getReservedCash(), getOutstandingCash(), getRequestType(), getRequestSubType());
	}
}
