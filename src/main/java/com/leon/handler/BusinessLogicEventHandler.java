package com.leon.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.model.*;
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

    public BusinessLogicEventHandler(DisruptorService outboundDisruptor)
    {
        initializeChronicleMap();
        this.outboundDisruptor = outboundDisruptor;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        String result = "";
        try
        {
            switch (RequestTypeEnum.valueOf(event.getPayload().getPayloadType()))
            {
                case CASH_CHECK_REQUEST_TYPE:
                    result = processCashCheckRequest(MessageFactory.createCashCheckRequestMessage(event.getPayload().getPayload()));
                    break;
                case POSITION_CHECK_REQUEST_TYPE:
                    result = processPositionCheckRequest(MessageFactory.createPositionCheckRequestMessage(event.getPayload().getPayload()));
                    break;
                case EXECUTION_MESSAGE_TYPE:
                    result = processExecution(MessageFactory.createExecutionMessage(event.getPayload().getPayload()));
            }
        }
        catch(IllegalArgumentException e)
        {
            logger.error("Event ignored because cannot convert " + event.getPayload().getPayloadType() + " to RequestTypeEnum. Exception thrown: " + e.getMessage());
        }
        outboundDisruptor.push(new DisruptorPayload("RESPONSE", result)); // TODO
    }

    private String processCashCheckRequest(CheckCashRequestMessage checkRequestMessage)
    {
        return OutcomeType.SUCCESS.toString();
    }

    private String processPositionCheckRequest(CheckStockRequestMessage checkRequestMessage)
    {
        return OutcomeType.SUCCESS.toString();
    }

    private String processExecution(ExecutionMessage executionMessage)
    {
        return OutcomeType.SUCCESS.toString();
    }

    public void uploadSODPositions(String startOfDayInventoryPositionFilePath)
    {
        try
        {
            int size = persistedDisruptorMap.size();
            if(size > 0)
            {
                persistedDisruptorMap.clear();
                logger.info("Cleared the chronicle map of " + size + " inventory positions.");
            }

            final ObjectMapper objectMapper = new ObjectMapper();
            List<PositionInventory> positionInventories = objectMapper.readValue(
                    new File(startOfDayInventoryPositionFilePath),
                    new TypeReference<List<PositionInventory>>(){});

            positionInventories.forEach(positionInventory ->
                    persistedDisruptorMap.put(String.format("%05d%s", positionInventory.getClientId(), positionInventory.getStockCode()), positionInventory));

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

            logger.info("Created the chronicle map with " + persistedDisruptorMap.size() + " inventory positions.");
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
