package com.leon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import java.util.Objects;

public class InventoryCheckResponse
{
	@JsonProperty("lockQuantity")
	private int lockedQuantity;
	@JsonProperty("unlockedQuantity")
	private int unlockedQuantity;
	@JsonProperty("result")
	private String result;
	@JsonProperty("referenceId")
	private String referenceId;
	@JsonProperty("clientId")
	private int clientId;
	@JsonProperty("instrumentId")
	private int instrumentId;
	@JsonProperty("lockedCash")
	private double lockedCash;
	@JsonProperty("unlockedCash")
	private double unlockedCash;
	@JsonProperty("requestType")
	private String requestType;
	@JsonProperty("requestSubType")
	private String requestSubType;
	@JsonProperty("uid")
	private String uid;


	public InventoryCheckResponse(int lockedQuantity, int unlockedQuantity, String result, String referenceId, int clientId, int instrumentId, double lockedCash, double unlockedCash, String requestType, String requestSubType)
	{
		this.lockedQuantity = lockedQuantity;
		this.unlockedQuantity = unlockedQuantity;
		this.result = result;
		this.referenceId = referenceId;
		this.clientId = clientId;
		this.instrumentId = instrumentId;
		this.lockedCash = lockedCash;
		this.unlockedCash = unlockedCash;
		this.requestType = requestType;
		this.requestSubType = requestSubType;
		this.uid = UUID.randomUUID().toString();
	}

	public InventoryCheckResponse()
	{
		this.uid = UUID.randomUUID().toString();
	}

	public int getLockedQuantity()
	{
		return lockedQuantity;
	}

	public void setLockedQuantity(int lockedQuantity)
	{
		this.lockedQuantity = lockedQuantity;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public String getReferenceId()
	{
		return referenceId;
	}

	public void setReferenceId(String referenceId)
	{
		this.referenceId = referenceId;
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

	public double getLockedCash()
	{
		return lockedCash;
	}

	public void setLockedCash(double lockedCash)
	{
		this.lockedCash = lockedCash;
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

	public int getUnlockedQuantity()
	{
		return unlockedQuantity;
	}

	public void setUnlockedQuantity(int unlockedQuantity)
	{
		this.unlockedQuantity = unlockedQuantity;
	}

	public double getUnlockedCash()
	{
		return unlockedCash;
	}

	public void setUnlockedCash(double unlockedCash)
	{
		this.unlockedCash = unlockedCash;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InventoryCheckResponse that = (InventoryCheckResponse) o;
		return getLockedQuantity() == that.getLockedQuantity() && getClientId() == that.getClientId() && Double.compare(that.getLockedCash(), getLockedCash()) == 0
				&& getResult().equals(that.getResult()) && getReferenceId().equals(that.getReferenceId()) && getInstrumentId() == that.getInstrumentId()
				&& getRequestType().equals(that.getRequestType()) && getRequestSubType().equals(that.getRequestSubType()) && getUnlockedQuantity() == that.getUnlockedQuantity()
				&& Double.compare(that.getUnlockedCash(), getUnlockedCash()) == 0 && getUid().equals(that.getUid());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getLockedQuantity(), getResult(), getReferenceId(), getClientId(), getUnlockedCash(),
				getInstrumentId(), getLockedCash(), getRequestType(), getRequestSubType(), getUnlockedQuantity(), getUid());
	}

	@Override
	public String toString()
	{
		return "InventoryCheckResponse{" + "lockedQuantity=" + lockedQuantity + ", unlockedQuantity=" + unlockedQuantity + ", result='" + result + '\'' + ", referenceId='" + referenceId + '\'' + ", clientId=" + clientId + ", instrumentId=" + instrumentId + ", lockedCash=" + lockedCash + ", unlockedCash=" + unlockedCash + ", requestType='" + requestType + '\'' + ", requestSubType='" + requestSubType + '\'' + ", uid='" + uid + '\'' + '}';
	}
}
