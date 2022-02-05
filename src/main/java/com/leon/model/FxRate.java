package com.leon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Objects;

public class FxRate
{
	@JsonProperty("currency")
	private String currency;
	@JsonProperty("fxRateAgainstUSD")
	private double fxRateAgainstUSD;
	private LocalDateTime updatedDateTime;

	public FxRate()
	{
		this.updatedDateTime = LocalDateTime.now();
	}

	public FxRate(String currency, double fxRateAgainstUSD)
	{
		this.currency = currency;
		this.fxRateAgainstUSD = fxRateAgainstUSD;
		this.updatedDateTime = LocalDateTime.now();
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public double getFxRateAgainstUSD()
	{
		return fxRateAgainstUSD;
	}

	public void setFxRateAgainstUSD(double fxRateAgainstUSD)
	{
		this.fxRateAgainstUSD = fxRateAgainstUSD;
	}

	public LocalDateTime getUpdatedDateTime()
	{
		return updatedDateTime;
	}

	@Override
	public String toString()
	{
		return "FxRate{" + "currency='" + currency + '\'' + ", fxRateAgainstUSD=" + fxRateAgainstUSD + ", updatedDateTime=" + updatedDateTime + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FxRate fxRate = (FxRate) o;
		return Double.compare(fxRate.getFxRateAgainstUSD(), getFxRateAgainstUSD()) == 0 && getCurrency().equals(fxRate.getCurrency()) && getUpdatedDateTime().equals(fxRate.getUpdatedDateTime());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getCurrency(), getFxRateAgainstUSD(), getUpdatedDateTime());
	}
}
