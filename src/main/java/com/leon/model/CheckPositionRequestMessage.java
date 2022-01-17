package com.leon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CheckPositionRequestMessage
{
	@JsonProperty("lockQuantity")
	private int lockQuantity;
	@JsonProperty("unlockQuantity")
	private int unlockQuantity;
	@JsonProperty("clientId")
	private int clientId;
	@JsonProperty("instrumentId")
	private int instrumentId;
	@JsonProperty("requestType")
	private String requestType;

	public CheckPositionRequestMessage(int lockQuantity, int unlockQuantity, int clientId, int instrumentId, String requestType)
	{
		this.lockQuantity = lockQuantity;
		this.unlockQuantity = unlockQuantity;
		this.clientId = clientId;
		this.instrumentId = instrumentId;
		this.requestType = requestType;
	}

	public CheckPositionRequestMessage()
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

	public int getInstrumentId()
	{
		return instrumentId;
	}

	public void setInstrumentId(int instrumentId)
	{
		this.instrumentId = instrumentId;
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
	public String toString()
	{
		return "CheckStockRequestMessage{" + "lockQuantity=" + lockQuantity + ", unlockQuantity=" + unlockQuantity + ", clientId=" + clientId + ", instrumentId=" + instrumentId  + ", requestType=" + requestType + "}";
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CheckPositionRequestMessage that = (CheckPositionRequestMessage) o;
		return getLockQuantity() == that.getLockQuantity() && getUnlockQuantity() == that.getUnlockQuantity() && getClientId() == that.getClientId() && getInstrumentId() == that.getInstrumentId() && getRequestType().equals(that.getRequestType());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getLockQuantity(), getUnlockQuantity(), getClientId(), getInstrumentId(), getRequestType());
	}
}