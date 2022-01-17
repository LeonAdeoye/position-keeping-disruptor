package com.leon.model;

import java.util.Objects;

public class Instrument
{
	private String bloombergCode;
	private int instrumentId;
	private String description;
	private String MIC;

	public Instrument()
	{
	}

	public Instrument(String bloombergCode, int instrumentId, String description, String MIC)
	{
		this.bloombergCode = bloombergCode;
		this.instrumentId = instrumentId;
		this.description = description;
		this.MIC = MIC;
	}

	public String getBloombergCode()
	{
		return bloombergCode;
	}

	public void setBloombergCode(String bloombergCode)
	{
		this.bloombergCode = bloombergCode;
	}

	public int getInstrumentId()
	{
		return instrumentId;
	}

	public void setInstrumentId(int instrumentId)
	{
		this.instrumentId = instrumentId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getMIC()
	{
		return MIC;
	}

	public void setMIC(String MIC)
	{
		this.MIC = MIC;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Instrument that = (Instrument) o;
		return getInstrumentId() == that.getInstrumentId() && getBloombergCode().equals(that.getBloombergCode()) && getDescription().equals(that.getDescription()) && getMIC().equals(that.getMIC());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getBloombergCode(), getInstrumentId(), getDescription(), getMIC());
	}

	@Override
	public String toString()
	{
		return "Instrument{" + "bloombergCode='" + bloombergCode + '\'' + ", instrumentId=" + instrumentId + ", description='" + description + '\'' + ", MIC='" + MIC + '\'' + '}';
	}
}
