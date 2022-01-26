package com.leon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.TextMessage;

@Service
public class HeartBeatServiceImpl implements HeartBeatService
{
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatServiceImpl.class);
	@Autowired
	private JmsTemplate jmsTemplate;
	private boolean started = false;
	@Value("${spring.activemq.heartbeat.topic}")
	String heartbeatTopic;
	private boolean isPrimary;

	HeartBeatServiceImpl(boolean isPrimary )
	{
		this.isPrimary = isPrimary;
	}

	@Override
	public void ping()
	{
		jmsTemplate.send(heartbeatTopic, s -> s.createTextMessage(isPrimary? "PRIMARY" : "SECONDARY"));
	}

	@Override
	@JmsListener(destination = "${spring.activemq.position.check.response.topic}")
	public void receive(Message message)
	{
		if(!started)
			return;
		try
		{
			if(message instanceof TextMessage)
			{
				TextMessage textMessage = (TextMessage) message;

				logger.info("Received heartbeat message from: {}", textMessage.getText());
			}
		}
		catch(Exception e)
		{
			logger.error("Received Exception with processing position check request: " + e);
		}
	}

	@Override
	public void start()
	{
		started = true;
	}

	@Override
	public void stop()
	{
		started = false;
	}
}
