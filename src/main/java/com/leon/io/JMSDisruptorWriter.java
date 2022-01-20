package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class JMSDisruptorWriter implements DisruptorWriter
{
	private static final Logger logger = LoggerFactory.getLogger(JMSDisruptorWriter.class);
	private boolean inSilentMode = false;
	private JmsTemplate jmsTemplate;
	private String positionCheckResponseTopic;

	@Override
	public void toggleSilentMode()
	{
		inSilentMode = !inSilentMode;
		logger.info(inSilentMode ? "Silent mode toggled ON!" : "Silent mode toggled OFF!");
	}

	@Override
	public void start(ConfigurationServiceImpl configurationService)
	{
		this.inSilentMode = configurationService.inSilentMode();
		this.jmsTemplate = configurationService.jmsTemplate();
		this.positionCheckResponseTopic = configurationService.getPositionCheckResponseTopic();
	}

	@Override
	public void writeAll(Flux<DisruptorPayload> payload)
	{
		payload.subscribe(load ->
		{
			try
			{
				if(!inSilentMode)
				{
					jmsTemplate.convertAndSend(positionCheckResponseTopic, load.getPayload());
					logger.info("Used topic: " + positionCheckResponseTopic  + " to send message: " + load.getPayload());
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.error("Exception thrown while sending message:" + e + " on topic: " + positionCheckResponseTopic);
			}
		});
	}

	@Override
	public void write(DisruptorPayload payload)
	{
		try
		{
			if(!inSilentMode)
			{
				jmsTemplate.convertAndSend(positionCheckResponseTopic, payload.getPayload());
				logger.info("Used topic: " + positionCheckResponseTopic  + " to send message: " + payload.getPayload());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("Exception thrown while sending message:" + e.getLocalizedMessage() + " on topic: " + positionCheckResponseTopic);
		}
	}

	@Override
	public void stop()
	{

	}
}
