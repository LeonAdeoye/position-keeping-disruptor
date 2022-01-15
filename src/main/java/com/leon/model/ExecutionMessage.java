package com.leon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ExecutionMessage
{
	@JsonProperty("executedPrice")
	private double executedPrice;
	@JsonProperty("executedQuantity")
	private int executedQuantity;
	@JsonProperty("cumulativeQuantity")
	private int cumulativeQuantity;
	@JsonProperty("orderQuantity")
	private int orderQuantity;
	@JsonProperty("side")
	private char side;
	@JsonProperty("executionId")
	private String executionId;
	@JsonProperty("orderId")
	private String orderId;
	@JsonProperty("instrumentId")
	private int instrumentId;
	@JsonProperty("clientId")
	private int clientId;

	public ExecutionMessage()
	{
	}

	public ExecutionMessage(double executedPrice, int executedQuantity, int cumulativeQuantity, int orderQuantity, char side, String executionId, String orderId, int instrumentId, int clientId)
	{
		this.executedPrice = executedPrice;
		this.executedQuantity = executedQuantity;
		this.cumulativeQuantity = cumulativeQuantity;
		this.orderQuantity = orderQuantity;
		this.side = side;
		this.executionId = executionId;
		this.orderId = orderId;
		this.instrumentId = instrumentId;
		this.clientId = clientId;
	}

	public int getExecutedQuantity()
	{
		return executedQuantity;
	}

	public void setExecutedQuantity(int executedQuantity)
	{
		this.executedQuantity = executedQuantity;
	}

	public int getCumulativeQuantity()
	{
		return cumulativeQuantity;
	}

	public void setCumulativeQuantity(int cumulativeQuantity)
	{
		this.cumulativeQuantity = cumulativeQuantity;
	}

	public int getOrderQuantity()
	{
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity)
	{
		this.orderQuantity = orderQuantity;
	}

	public char getSide()
	{
		return side;
	}

	public void setSide(char side)
	{
		this.side = side;
	}

	public String getExecutionId()
	{
		return executionId;
	}

	public void setExecutionId(String executionId)
	{
		this.executionId = executionId;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public int getInstrumentId()
	{
		return instrumentId;
	}

	public void setInstrumentId(int instrumentId)
	{
		this.instrumentId = instrumentId;
	}

	public int getClientId()
	{
		return clientId;
	}

	public void setClientId(int clientId)
	{
		this.clientId = clientId;
	}

	public double getExecutedPrice()
	{
		return executedPrice;
	}

	public void setExecutedPrice(double executedPrice)
	{
		this.executedPrice = executedPrice;
	}

	@Override
	public String toString()
	{
		return "ExecutionMessage{" + "executedPrice=" + executedPrice + ", executedQuantity=" + executedQuantity + ", cumulativeQuantity=" + cumulativeQuantity + ", orderQuantity=" + orderQuantity + ", side=" + side + ", executionId='" + executionId + ", orderId='" + orderId + ", instrumentId=" + instrumentId + ", clientId=" + clientId + '}';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ExecutionMessage that = (ExecutionMessage) o;
		return Double.compare(that.getExecutedPrice(), getExecutedPrice()) == 0 && getExecutedQuantity() == that.getExecutedQuantity() && getCumulativeQuantity() == that.getCumulativeQuantity() && getOrderQuantity() == that.getOrderQuantity() && getSide() == that.getSide() && getInstrumentId() == that.getInstrumentId() && getClientId() == that.getClientId() && getExecutionId().equals(that.getExecutionId()) && getOrderId().equals(that.getOrderId());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getExecutedPrice(), getExecutedQuantity(), getCumulativeQuantity(), getOrderQuantity(), getSide(), getExecutionId(), getOrderId(), getInstrumentId(), getClientId());
	}
}
