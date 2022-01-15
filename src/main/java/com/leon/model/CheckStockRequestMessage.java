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
	@JsonProperty("instrumentId")
	private String instrumentId;

	public CheckStockRequestMessage(int lockQuantity, int unlockQuantity, int clientId, String instrumentId)
	{
		this.lockQuantity = lockQuantity;
		this.unlockQuantity = unlockQuantity;
		this.clientId = clientId;
		this.instrumentId = instrumentId;
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

	public String getInstrumentId()
	{
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId)
	{
		this.instrumentId = instrumentId;
	}

	@Override
	public String toString()
	{
		return "CheckStockRequestMessage{" + "lockQuantity=" + lockQuantity + ", unlockQuantity=" + unlockQuantity + ", clientId=" + clientId + ", instrumentId='" + instrumentId + '\'' + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CheckStockRequestMessage that = (CheckStockRequestMessage) o;
		return getLockQuantity() == that.getLockQuantity() && getUnlockQuantity() == that.getUnlockQuantity() && getClientId() == that.getClientId() && getInstrumentId() == that.getInstrumentId();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getLockQuantity(), getUnlockQuantity(), getClientId(), getInstrumentId());
	}
}