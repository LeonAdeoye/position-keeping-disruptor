package com.leon.io;

import com.leon.model.DisruptorPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component("JMSDisruptorWriter")
@ConditionalOnProperty(value="jms.output.writer", havingValue = "true")
public class JMSDisruptorWriter implements DisruptorWriter
{
	private static final Logger logger = LoggerFactory.getLogger(JMSDisruptorWriter.class);
	@Value("${is.primary}")
	private boolean isPrimary = false;
	@Value("${spring.activemq.position.check.response.topic}")
	private String positionCheckResponseTopic;
	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public boolean togglePrimary()
	{
		isPrimary = !isPrimary;
		logger.info(isPrimary ? "Toggled. Writer now running in PRIMARY mode." : "Writer now running in SECONDARY mode.");
		return isPrimary;
	}

	@Override
	public void start()
	{
		logger.info((isPrimary ? "Started writer in PRIMARY mode" : "Started writer in SECONDARY mode") + " with topic: " + positionCheckResponseTopic);
	}

	@Override
	public void writeAll(Flux<DisruptorPayload> payload)
	{
		payload.subscribe(load -> write(load));
	}

	@Override
	public void write(DisruptorPayload payload)
	{
		try
		{
			if(isPrimary)
				jmsTemplate.send(positionCheckResponseTopic, s -> s.createTextMessage(payload.getPayload()));
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
