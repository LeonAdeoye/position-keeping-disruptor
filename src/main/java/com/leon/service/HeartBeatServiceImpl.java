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
import java.util.UUID;

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
	private String heartbeatSender;
	private String heartbeatUID;
	private LocalDateTime lastTimeStamp;
	private String uid = UUID.randomUUID().toString();

	@Scheduled(fixedDelay=100)
	public void ping()
	{
		if(!hasStarted)
			return;

		jmsTemplate.send(heartbeatTopic, s -> s.createTextMessage((isPrimary? "PRIMARY" : "SECONDARY") + "=" + uid));
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
				String[] splitHeartbeat  = textMessage.getText().split("=");
				if(splitHeartbeat.length == 2)
				{
					heartbeatSender = splitHeartbeat[0];
					heartbeatUID = splitHeartbeat[1];
				}
				else
					return;

				if(heartbeatSender.equals(isPrimary ? "PRIMARY" : "SECONDARY"))
				{
					if(!splitHeartbeat[1].equals(uid))
						logger.error("Another service is also running as {} with a UID: {}", (isPrimary ? "PRIMARY" : "SECONDARY"), heartbeatUID);
					return;
				}

				lastTimeStamp = LocalDateTime.now();
				logger.info("Received heartbeat message from: {} with UID: {} at: {}", heartbeatSender, heartbeatUID, lastTimeStamp);
			}
		}
		catch(Exception e)
		{
			logger.error("Received Exception with processing heartbeat: " + e);
		}
	}

	@Scheduled(fixedDelay=200)
	private void checkHeartbeat()
	{
		if(hasStarted && heartbeatSender != null && !heartbeatSender.isEmpty() && heartbeatSender.equals(isPrimary ? "PRIMARY" : "SECONDARY") && lastTimeStamp !=null)
		{
			long gap = Math.abs(ChronoUnit.MILLIS.between(lastTimeStamp, LocalDateTime.now()));
			if(gap > heartbeatCheckMaximumInterval)
				logger.error("Heartbeat error!! Sender: {}, lastTimeStamp: {}, gap: {}, maxInterval: {}", heartbeatSender, lastTimeStamp, gap, heartbeatCheckMaximumInterval);
		}
	}

	@Override
	public void start(boolean isPrimary)
	{
		this.isPrimary = isPrimary;
		logger.info("Heartbeat service started running in {} mode with UID: {}", (isPrimary ? "PRIMARY" : "SECONDARY"), uid);
		hasStarted = true;
		ping();
	}

	@Override
	public void stop()
	{
		hasStarted = false;
	}
}
