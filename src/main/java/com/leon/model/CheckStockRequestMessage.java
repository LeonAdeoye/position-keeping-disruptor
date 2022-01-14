package com.leon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CheckStockRequestMessage
{
	@JsonProperty("lockQuantity")
	private int lockQuantity;
	@JsonProperty("unlockQuantity")
	private int unlockQuantity;
	@JsonProperty("clientId")
	private int clientId;
	@JsonProperty("stockCode")
	private String stockCode;

	public CheckStockRequestMessage(int lockQuantity, int unlockQuantity, int clientId, String stockCode)
	{
		this.lockQuantity = lockQuantity;
		this.unlockQuantity = unlockQuantity;
		this.clientId = clientId;
		this.stockCode = stockCode;
	}

	public CheckStockRequestMessage()
	{
	}

	public int getLockQuantity()
	{
		return lockQuantity;
	}

	public void setLockQuantity(int lockQuantity)
	{
		this.lockQuantity = lockQuantity;
	}

	public int getUnlockQuantity()
	{
		return unlockQuantity;
	}

	public void setUnlockQuantity(int unlockQuantity)
	{
		this.unlockQuantity = unlockQuantity;
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

	@Override
	public String toString()
	{
		return "CheckStockRequestMessage{" + "lockQuantity=" + lockQuantity + ", unlockQuantity=" + unlockQuantity + ", clientId=" + clientId + ", stockCode='" + stockCode + '\'' + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CheckStockRequestMessage that = (CheckStockRequestMessage) o;
		return getLockQuantity() == that.getLockQuantity() && getUnlockQuantity() == that.getUnlockQuantity() && getClientId() == that.getClientId() && getStockCode().equals(that.getStockCode());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getLockQuantity(), getUnlockQuantity(), getClientId(), getStockCode());
	}
}