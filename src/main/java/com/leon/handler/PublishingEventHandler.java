package com.leon.handler;

import com.leon.io.DisruptorWriter;
import com.leon.model.DisruptorEvent;
import com.leon.model.DisruptorPayload;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishingEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(PublishingEventHandler.class);
    private DisruptorWriter writer;

    public PublishingEventHandler(DisruptorWriter writer)
    {
        this.writer = writer;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        DisruptorPayload payload = event.getPayload();
        if(payload != null)
        {
            writer.write(payload);
            logger.info("Published response " + payload + " for check request with UID: " + payload.getUid() + ", time taken in nano-seconds: " + (System.nanoTime() - payload.getCreatedTime()));
        }
    }
}
