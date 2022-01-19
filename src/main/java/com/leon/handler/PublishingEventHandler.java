package com.leon.handler;

import com.leon.io.DisruptorWriter;
import com.leon.model.DisruptorEvent;
import com.leon.model.DisruptorPayload;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class PublishingEventHandler implements EventHandler<DisruptorEvent>
{
    @Autowired
    JmsTemplate jmsTemplate;
    private String positionCheckResponseTopic;
    private static final Logger logger = LoggerFactory.getLogger(PublishingEventHandler.class);
    private DisruptorWriter writer;

    public PublishingEventHandler(DisruptorWriter writer, String publishingTopic)
    {
        this.writer = writer;
        this.positionCheckResponseTopic = publishingTopic;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        DisruptorPayload payload = event.getPayload();
        if(payload != null)
        {
            try
            {
                // TODO move this to DisruptorWriter
                jmsTemplate.convertAndSend(positionCheckResponseTopic, new Integer(1));
            }
            catch(Exception e)
            {
                logger.error("Exception thrown while sending message:" + e.getLocalizedMessage() + " on topic: " + positionCheckResponseTopic);
            }
        }
    }
}
