package com.leon.handler;

import com.leon.service.DisruptorService;
import com.lmax.disruptor.EventHandler;
import net.openhft.chronicle.core.values.LongValue;
import net.openhft.chronicle.map.ChronicleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leon.model.DisruptorEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class BusinessLogicEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicEventHandler.class);
    private DisruptorService outboundDisruptor;
    private  ChronicleMap<LongValue, LocalDateTime> persistedDisruptorMap;

    public BusinessLogicEventHandler(DisruptorService outboundDisruptor)
    {
        try
        {
            persistedDisruptorMap = ChronicleMap
                .of(LongValue.class, LocalDateTime.class)
                .name("disruptor-map")
                .entries(1_000_000)
                .averageValue(LocalDateTime.now())
                .createPersistedTo(new File(System.getProperty("user.home") + "/../log/country-details.dat"));
        }
        catch(IOException ioe)
        {
            logger.error(ioe.getMessage());
        }

        this.outboundDisruptor = outboundDisruptor;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        System.out.println(event.getPayload());
        outboundDisruptor.push(event.getPayload());
    }
}
