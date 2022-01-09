package com.leon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PositionInventory
{
	@JsonProperty("clientId")
	private int clientId;
	@JsonProperty("stockCode")
	private String stockCode;
	@JsonProperty("startOfDayQuantity")
	private int startOfDayQuantity;
	@JsonProperty("executedQuantity")
	private int executedQuantity;
	@JsonProperty("reservedQuantity")
	private int reservedQuantity;
	@JsonProperty("borrowedQuantity")
	private int borrowedQuantity;
	@JsonProperty("startOfDayCash")
	private double startOfDayCash;
	@JsonProperty("executedCash")
	private double executedCash;
	@JsonProperty("reservedCash")
	private double reservedCash;

	public PositionInventory(int clientId, String stockCode, int startOfDayQuantity, int executedQuantity, int reservedQuantity, int borrowedQuantity, double startOfDayCash, double executedCash, double reservedCash)
	{
		this.clientId = clientId;
		this.stockCode = stockCode;
		this.startOfDayQuantity = startOfDayQuantity;
		this.executedQuantity = executedQuantity;
		this.reservedQuantity = reservedQuantity;
		this.borrowedQuantity = borrowedQuantity;
		this.startOfDayCash = startOfDayCash;
		this.executedCash = executedCash;
		this.reservedCash = reservedCash;
	}

	public PositionInventory()
	{
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

	public int getStartOfDayQuantity()
	{
		return startOfDayQuantity;
	}

	public void setStartOfDayQuantity(int startOfDayQuantity)
	{
		this.startOfDayQuantity = startOfDayQuantity;
	}

	public int getExecutedQuantity()
	{
		return executedQuantity;
	}

	public void setExecutedQuantity(int executedQuantity)
	{
		this.executedQuantity = executedQuantity;
	}

	public int getReservedQuantity()
	{
		return reservedQuantity;
	}

	public void setReservedQuantity(int reservedQuantity)
	{
		this.reservedQuantity = reservedQuantity;
	}

	public int getBorrowedQuantity()
	{
		return borrowedQuantity;
	}

	public void setBorrowedQuantity(int borrowedQuantity)
	{
		this.borrowedQuantity = borrowedQuantity;
	}

	public double getStartOfDayCash()
	{
		return startOfDayCash;
	}

	public void setStartOfDayCash(double startOfDayCash)
	{
		this.startOfDayCash = startOfDayCash;
	}

	public double getExecutedCash()
	{
		return executedCash;
	}

	public void setExecutedCash(double executedCash)
	{
		this.executedCash = executedCash;
	}

	public double getReservedCash()
	{
		return reservedCash;
	}

	public void setReservedCash(double reservedCash)
	{
		this.reservedCash = reservedCash;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PositionInventory that = (PositionInventory) o;
		return getClientId() == that.getClientId() && getStartOfDayQuantity() == that.getStartOfDayQuantity() && getExecutedQuantity() == that.getExecutedQuantity() && getReservedQuantity() == that.getReservedQuantity() && getBorrowedQuantity() == that.getBorrowedQuantity() && Double.compare(that.getStartOfDayCash(), getStartOfDayCash()) == 0 && Double.compare(that.getExecutedCash(), getExecutedCash()) == 0 && Double.compare(that.getReservedCash(), getReservedCash()) == 0 && getStockCode() == (that.getStockCode());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getClientId(), getStockCode(), getStartOfDayQuantity(), getExecutedQuantity(), getReservedQuantity(), getBorrowedQuantity(), getStartOfDayCash(), getExecutedCash(), getReservedCash());
	}

	@Override
	public String toString()
	{
		return "PositionInventory{" + "clientId=" + clientId + ", stockCode='" + stockCode + '\'' + ", startOfDayQuantity=" + startOfDayQuantity + ", executedQuantity=" + executedQuantity + ", reservedQuantity=" + reservedQuantity + ", borrowedQuantity=" + borrowedQuantity + ", startOfDayCash=" + startOfDayCash + ", executedCash=" + executedCash + ", reservedCash=" + reservedCash + '}';
	}
}
