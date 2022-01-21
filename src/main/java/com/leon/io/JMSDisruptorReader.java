package com.leon.io;

import com.leon.model.DisruptorPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import reactor.core.publisher.Flux;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


public class JMSDisruptorReader implements DisruptorReader, MessageListener
{
	private static final Logger logger = LoggerFactory.getLogger(JMSDisruptorReader.class);

	@Override
	public void start()
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

	@Override
	@JmsListener(destination = "${spring.activemq.position.check.request.topic}")
	public void onMessage(Message message)
	{
		try
		{
			if(message instanceof TextMessage)
			{
				TextMessage textMessage = (TextMessage) message;
				logger.info("Received message position check request: " + textMessage.getText());
			}
		}
		catch(Exception e)
		{
			logger.error("Received Exception with processing position check request: " + e);
		}
	}
}
