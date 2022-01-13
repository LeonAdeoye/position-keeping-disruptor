package com.leon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}