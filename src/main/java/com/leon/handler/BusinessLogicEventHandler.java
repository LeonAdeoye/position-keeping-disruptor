package com.leon.handler;

import com.leon.service.DisruptorService;
import com.lmax.disruptor.EventHandler;
import net.openhft.chronicle.core.values.LongValue;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.values.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leon.model.DisruptorEvent;

import java.io.File;
import java.io.IOException;

public class BusinessLogicEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicEventHandler.class);
    private DisruptorService outboundDisruptor;
    private  ChronicleMap<LongValue, CharSequence> persistedDisruptorMap;

    public BusinessLogicEventHandler(DisruptorService outboundDisruptor)
    {
        try
        {
            persistedDisruptorMap = ChronicleMap
                .of(LongValue.class, CharSequence.class)
                .name("disruptor-map")
                .entries(1_000_000)
                .averageValue("America")
                .createPersistedTo(new File("../logs/disruptor-processed.txt"));

            LongValue qatarKey = Values.newHeapInstance(LongValue.class);
            qatarKey.setValue(1);
            persistedDisruptorMap.put(qatarKey, "England");
            CharSequence country = persistedDisruptorMap.get(qatarKey);
            System.out.println("Country: " + country.toString());
            logger.info("Created chronicle map.");
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

    public void close()
    {
        persistedDisruptorMap.close();
    }
}
