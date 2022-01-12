package com.leon.model;

public class MessageFactory
{
	public static ExecutionMessage createExecutionMessage(String payload)
	{
		return new ExecutionMessage();
	}

	public static CheckRequestMessage createCashCheckRequestMessage(String payload)
	{
		return new CheckRequestMessage();
	}

	public static CheckRequestMessage createPositionCheckRequestMessage(String payload)
	{
		return new CheckRequestMessage();
	}
}
