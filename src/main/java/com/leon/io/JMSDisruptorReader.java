package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
@ConditionalOnProperty(value="jms.input.reader", havingValue = "true")
public class JMSDisruptorReader implements DisruptorReader, MessageListener
{
	private static final Logger logger = LoggerFactory.getLogger(JMSDisruptorReader.class);
	public final ReplayProcessor<DisruptorPayload> processor = ReplayProcessor.create();
	private final FluxSink<DisruptorPayload> sink = processor.sink();

	@Value("${input.reader.end.indicator}")
	private String endIndicator;

	@Override
	public void start()
	{

	}

	@Override
	public void start(ConfigurationServiceImpl configurationService)
	{

	}

	@Override
	public Flux<DisruptorPayload> readAll()
	{
		return processor;
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
			if (message instanceof TextMessage)
			{
				TextMessage textMessage = (TextMessage) message;
				logger.info("Received message: {}", textMessage.getText());
				String[] splitInput  = textMessage.getText().split("=");

				if(textMessage.getText().equals(endIndicator))
					sink.complete();
				else
				{
					if (splitInput.length == 2)
						sink.next(new DisruptorPayload(splitInput[0], splitInput[1]));
					else
						logger.error("Cannot push incorrect message onto disruptor because of format: {}. ", textMessage.getText());
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Received Exception with processing position check request from JMS listener: " + e.getLocalizedMessage());
			sink.error(e);
		}
	}
}
