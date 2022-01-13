package com.leon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}