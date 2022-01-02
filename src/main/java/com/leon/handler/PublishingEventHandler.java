package com.leon.handler;

import com.leon.io.DisruptorWriter;
import com.leon.model.DisruptorPayload;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leon.model.DisruptorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

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
        DisruptorPayload payLoad = event.getPayload();
        if(payLoad != null)
        {
            logger.debug(payLoad.toString());
            writer.write(Mono.just(payLoad));
        }
    }
}
