package com.leon.io;

import com.leon.model.DisruptorPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component("JMSDisruptorReader")
public class JMSDisruptorReader implements DisruptorReader, MessageListener
{
	private static final Logger logger = LoggerFactory.getLogger(JMSDisruptorReader.class);
	Flux<DisruptorPayload> flux = Flux.empty();

	@Override
	public void start()
	{
	}

	@Override
	public Flux<DisruptorPayload> readAll()
	{
		return flux;
	}

	@Override
	public void stop()
	{
	}

	@Override
	@JmsListener(destination = "${spring.activemq.position.check.response.topic}")
	public void onMessage(Message message)
	{
		try
		{
			if(message instanceof TextMessage)
			{
				TextMessage textMessage = (TextMessage) message;

				flux = Flux.concat(flux, Flux.just(new DisruptorPayload("Leon", textMessage.getText())));

				String[] splitInput  = textMessage.getText().split("=");
				if (splitInput.length == 2)
					flux = Flux.concat(flux, Flux.just(new DisruptorPayload(splitInput[0], splitInput[1])));
				else
					logger.error("Incorrect message format: {}", textMessage.getText());
			}
		}
		catch(Exception e)
		{
			logger.error("Received Exception with processing position check request: " + e);
		}
	}
}
