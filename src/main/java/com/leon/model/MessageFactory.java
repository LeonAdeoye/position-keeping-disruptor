package com.leon.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageFactory
{
	private static final Logger logger = LoggerFactory.getLogger(MessageFactory.class);
	final static ObjectMapper objectMapper = new ObjectMapper();

	public static ExecutionMessage createExecutionMessage(String payload)
	{
		try
		{
			return objectMapper.readValue(payload, ExecutionMessage.class);
		}
		catch (JsonProcessingException e)
		{
			logger.error("Failed to process execution message JSON: " + payload + " due to exception: " + e.getLocalizedMessage());
		}
		return null;
	}

	public static CheckCashRequestMessage createCashCheckRequestMessage(String payload)
	{
		try
		{
			return objectMapper.readValue(payload, CheckCashRequestMessage.class);
		}
		catch (JsonProcessingException e)
		{
			logger.error("Failed to process cash check request JSON: " + payload + " due to exception: " + e.getLocalizedMessage());
		}
		return null;
	}

	public static CheckPositionRequestMessage createPositionCheckRequestMessage(String payload)
	{
		try
		{
			return objectMapper.readValue(payload, CheckPositionRequestMessage.class);
		}
		catch (JsonProcessingException e)
		{
			logger.error("Failed to process stock check request JSON: " + payload + " due to exception: " + e.getLocalizedMessage());
		}
		return null;
	}
}
