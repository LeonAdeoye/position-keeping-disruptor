package com.leon.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.model.DisruptorEvent;
import com.leon.model.PositionInventory;
import com.leon.model.PositionInventorySerializer;
import com.leon.service.DisruptorService;
import com.lmax.disruptor.EventHandler;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class BusinessLogicEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(BusinessLogicEventHandler.class);
    private DisruptorService outboundDisruptor;
    private  ChronicleMap<String, PositionInventory> persistedDisruptorMap;

    public BusinessLogicEventHandler(DisruptorService outboundDisruptor, String startOfDayInventoryPositionFilePath)
    {
        initializeChronicleMap();
        uploadSODPositions(startOfDayInventoryPositionFilePath);
        this.outboundDisruptor = outboundDisruptor;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        outboundDisruptor.push(event.getPayload());
    }

    public void uploadSODPositions(String startOfDayInventoryPositionFilePath)
    {
        try
        {
            final ObjectMapper objectMapper = new ObjectMapper();
            List<PositionInventory> positionInventories = objectMapper.readValue(
                    new File(startOfDayInventoryPositionFilePath),
                    new TypeReference<List<PositionInventory>>(){});

            positionInventories.forEach(positionInventory ->
                    persistedDisruptorMap.put(String.format("%05d%05d", positionInventory.getClientId(), positionInventory.getStockCode()), positionInventory));

            logger.info("Loaded Chronicle map with " + positionInventories.size() + " inventory positions.");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void initializeChronicleMap()
    {
        try
        {
            persistedDisruptorMap = ChronicleMapBuilder
                    .of(String.class, PositionInventory.class)
                    .name("position-inventory-map")
                    .entries(5_000)
                    .averageValue(new PositionInventory())
                    .valueMarshaller(PositionInventorySerializer.getInstance())
                    .averageKey("00000100001") //TODO convert to Bloomberg code
                    .createPersistedTo(new File("../logs/position-inventory.txt"));
        }
        catch(IOException ioe)
        {
            logger.error(ioe.getMessage());
        }
    }

    public void close()
    {
        if(persistedDisruptorMap != null && persistedDisruptorMap.isOpen())
            persistedDisruptorMap.close();
        logger.info("Closed Chronicle map with inventory positions.");
    }
}
