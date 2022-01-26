package com.leon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class HeartBeatServiceImpl implements HeartBeatService
{
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatServiceImpl.class);
	@Autowired
	private JmsTemplate jmsTemplate;
	private boolean started = false;
	@Value("${spring.activemq.heartbeat.topic}")
	String heartbeatTopic;
	@Value("${heartbeat.check.maximum.interval}")
	long heartbeatCheckMaximumInterval;
	private boolean isPrimary;
	private String sender;
	private LocalDateTime timeStamp;

	HeartBeatServiceImpl(boolean isPrimary )
	{
		this.isPrimary = isPrimary;
	}

	@Override
	public void ping()
	{
		if(!started)
			return;

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

				sender = textMessage.getText();
				timeStamp = LocalDateTime.now();

				logger.info("Received heartbeat message from: {} at time stamp: {}", sender, timeStamp);
			}
		}
		catch(Exception e)
		{
			logger.error("Received Exception with processing position check request: " + e);
		}
	}

	@Scheduled(fixedDelay=1000)
	private boolean checkHeartbeat()
	{
		return started && !sender.isEmpty() && Math.abs(ChronoUnit.MILLIS.between(timeStamp, LocalDateTime.now())) > heartbeatCheckMaximumInterval;
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
