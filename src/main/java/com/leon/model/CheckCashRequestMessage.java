package com.leon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CheckCashRequestMessage
{
	@JsonProperty("lockCash")
	private int lockCash;
	@JsonProperty("unlockCash")
	private int unlockCash;
	@JsonProperty("clientId")
	private int clientId;
	@JsonProperty("stockCode")
	private String stockCode;

	public int getUnlockCash()
	{
		return unlockCash;
	}

	public void setUnlockCash(int unlockCash)
	{
		this.unlockCash = unlockCash;
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

	public int getLockCash()
	{
		return lockCash;
	}

	public void setLockCash(int lockCash)
	{
		this.lockCash = lockCash;
	}

	public CheckCashRequestMessage()
	{
	}

	public CheckCashRequestMessage(int lockCash, int unlockCash, int clientId, String stockCode)
	{
		this.lockCash = lockCash;
		this.unlockCash = unlockCash;
		this.clientId = clientId;
		this.stockCode = stockCode;
	}

	@Override
	public String toString()
	{
		return "CheckCashRequestMessage{" + "lockCash=" + lockCash + ", unlockCash=" + unlockCash + ", clientId=" + clientId + ", stockCode='" + stockCode + '\'' + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CheckCashRequestMessage that = (CheckCashRequestMessage) o;
		return getLockCash() == that.getLockCash() && getUnlockCash() == that.getUnlockCash() && getClientId() == that.getClientId() && getStockCode().equals(that.getStockCode());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getLockCash(), getUnlockCash(), getClientId(), getStockCode());
	}
}