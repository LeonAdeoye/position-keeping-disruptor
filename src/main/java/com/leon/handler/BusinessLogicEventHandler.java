package com.leon.handler;

import com.leon.service.DisruptorService;
import com.lmax.disruptor.EventHandler;
import net.openhft.chronicle.core.values.LongValue;
import net.openhft.chronicle.map.*;
import net.openhft.chronicle.values.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leon.model.DisruptorEvent;

import java.io.File;
import java.io.IOException;

/*
ChronicleMap Interface
There are few specials methods which are provided by ChronicleMap. See below
V getUsing(K key, V value); getUsing is same as get(key) but getUsing will return the value in value parameter without creating a new object whereas get will create a new object for returning the value with key.
V acquireUsing(K key, V value); acquireUsing is again same as getUsing but if there is no value defined with key, it will insert a new entry with key and returns the same value.

ChronicleMap can read and write the data from/to a JSON object. The following methods can be used to do so
void getAll(File toFile) throws IOException; To read map from the file which was created by another ChronicleMap using JSON format.
void putAll(File fromFile) throws IOException; To dump the entire map into a file using a JSON format.
void close();
As the data is stored off-heap, it's recommended to close the map to release the heap data and persist the data.
*/

public class BusinessLogicEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicEventHandler.class);
    private DisruptorService outboundDisruptor;
    private  ChronicleMap<LongValue, CharSequence> persistedDisruptorMap;

    public BusinessLogicEventHandler(DisruptorService outboundDisruptor)
    {
        try
        {
            persistedDisruptorMap = ChronicleMapBuilder
                .of(LongValue.class, CharSequence.class)
                .name("disruptor-map")
                .entries(1_000)
                .averageValue("America")
                .createPersistedTo(new File("../logs/disruptor-processed.txt"));

            LongValue qatarKey = Values.newHeapInstance(LongValue.class);
            qatarKey.setValue(1);
            CharSequence country = persistedDisruptorMap.get(qatarKey);
            logger.info("Created chronicle map.");
        }
        catch(IOException ioe)
        {
            logger.error(ioe.getMessage());
        }

        outboundDisruptor = outboundDisruptor;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        logger.debug(event.getPayload().toString());
        outboundDisruptor.push(event.getPayload());
    }

    public void close()
    {
        persistedDisruptorMap.close();
    }
}
