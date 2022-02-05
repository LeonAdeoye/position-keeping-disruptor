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
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class HeartBeatServiceImpl implements HeartBeatService, MessageListener
{
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatServiceImpl.class);
	@Autowired
	private JmsTemplate jmsTemplate;
	private boolean hasStarted = false;
	@Value("${spring.activemq.heartbeat.topic}")
	String heartbeatTopic;
	@Value("${heartbeat.check.maximum.interval}")
	long heartbeatCheckMaximumInterval;
	private boolean isPrimary;
	private String sender;
	private LocalDateTime lastTimeStamp;

	@Scheduled(fixedDelay=100)
	public void ping()
	{
		if(!hasStarted)
			return;

		jmsTemplate.send(heartbeatTopic, s -> s.createTextMessage(isPrimary? "PRIMARY" : "SECONDARY"));
	}

	@Override
	@JmsListener(destination = "${spring.activemq.heartbeat.topic}")
	public void onMessage(Message message)
	{
		if(!hasStarted)
			return;

		try
		{
			if(message instanceof TextMessage)
			{
				TextMessage textMessage = (TextMessage) message;
				sender = textMessage.getText();
				if(sender.equals(isPrimary ? "PRIMARY" : "SECONDARY"))
					return;

				lastTimeStamp = LocalDateTime.now();
				logger.info("Received heartbeat message from: {} at: {}", sender, lastTimeStamp);
			}
		}
		catch(Exception e)
		{
			logger.error("Received Exception with processing heartbeat: " + e);
		}
	}

	// TODO: act on failure by switching from secondary to primary if primary is down
	@Scheduled(fixedDelay=200)
	private void checkHeartbeat()
	{
		if(hasStarted && sender != null && !sender.isEmpty() && sender.equals(isPrimary ? "PRIMARY" : "SECONDARY") && lastTimeStamp !=null)
		{
			long gap = Math.abs(ChronoUnit.MILLIS.between(lastTimeStamp, LocalDateTime.now()));
			if(gap > heartbeatCheckMaximumInterval)
				logger.error("Heartbeat error!! Sender: {}, lastTimeStamp: {}, gap: {}, maxInterval: {}", sender, lastTimeStamp, gap, heartbeatCheckMaximumInterval);
		}
	}

	@Override
	public void start(boolean isPrimary)
	{
		this.isPrimary = isPrimary;
		hasStarted = true;
		ping();
	}

	@Override
	public void stop()
	{
		hasStarted = false;
	}
}
