package com.leon.io;

import com.leon.model.DisruptorPayload;
import reactor.core.publisher.Flux;

public class JMSDisruptorReader implements DisruptorReader
{
	@Override
	public void start(String readerFilePath)
	{

	}

	@Override
	public Flux<DisruptorPayload> readAll()
	{
		return null;
	}

	@Override
	public void stop()
	{
	}
}
