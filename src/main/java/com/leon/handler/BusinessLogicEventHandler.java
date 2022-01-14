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
        this.outboundDisruptor = outboundDisruptor;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        try
        {
            switch (RequestTypeEnum.valueOf(event.getPayload().getPayloadType()))
            {
                case CASH_CHECK_REQUEST_TYPE:
                    double reservedCash = processCashCheckRequest(MessageFactory.createCashCheckRequestMessage(event.getPayload().getPayload()));
                    break;
                case POSITION_CHECK_REQUEST_TYPE:
                    int reservedQuantity = processPositionCheckRequest(MessageFactory.createPositionCheckRequestMessage(event.getPayload().getPayload()));
                    break;
                case EXECUTION_MESSAGE_TYPE:
                    String result = processExecution(MessageFactory.createExecutionMessage(event.getPayload().getPayload()));
            }
        }
        catch(IllegalArgumentException e)
        {
            logger.error("Event ignored because cannot convert " + event.getPayload().getPayloadType() + " to RequestTypeEnum. Exception thrown: " + e.getMessage());
        }
        outboundDisruptor.push(new DisruptorPayload("RESPONSE", "Result is??")); // TODO
    }

    private double processCashCheckRequest(CheckCashRequestMessage checkRequestMessage)
    {
        // TODO Add logging
        String key = checkRequestMessage.getStockCode() + checkRequestMessage.getClientId(); // TODO create the right matching key
        PositionInventory positionInventory = persistedDisruptorMap.get(key);
        double balance = positionInventory.getStartOfDayCash() + positionInventory.getExecutedCash() - positionInventory.getReservedCash();
        if(balance > checkRequestMessage.getLockCash())
        {
            positionInventory.setReservedCash(positionInventory.getReservedCash() + checkRequestMessage.getLockCash());
            // TODO Write to chronicle map
            return checkRequestMessage.getLockCash();
        }
        if(balance > 0.0 && balance < checkRequestMessage.getLockCash())
        {
            positionInventory.setReservedCash(positionInventory.getReservedCash() + balance);
            // TODO Write to chronicle map
            return balance;
        }
        return 0.0;
    }

    private int processPositionCheckRequest(CheckStockRequestMessage checkRequestMessage)
    {
        // TODO Add logging
        String key = checkRequestMessage.getStockCode() + checkRequestMessage.getClientId(); // TODO create the right matching key
        PositionInventory positionInventory = persistedDisruptorMap.get(key);
        int balance = positionInventory.getStartOfDayQuantity() + positionInventory.getExecutedQuantity() - positionInventory.getReservedQuantity();
        if(balance > checkRequestMessage.getLockQuantity())
        {
            positionInventory.setReservedQuantity(positionInventory.getReservedQuantity() + checkRequestMessage.getLockQuantity());
            // TODO Write to chronicle map
            return checkRequestMessage.getLockQuantity();
        }
        if(balance > 0 && balance < checkRequestMessage.getLockQuantity())
        {
            positionInventory.setReservedQuantity(positionInventory.getReservedQuantity() + balance);
            // TODO Write to chronicle map
            return balance;
        }
        return 0;
    }

    private String processExecution(ExecutionMessage executionMessage)
    {
        // TODO Add logging
        String key = executionMessage.getStockCode() + executionMessage.getClientId(); // TODO create the right matching key
        PositionInventory positionInventory = persistedDisruptorMap.get(key);
        positionInventory.setExecutedCash(positionInventory.getExecutedCash());
        positionInventory.setExecutedQuantity(positionInventory.getExecutedQuantity());
        // TODO Write to chronicle map
        return OutcomeType.SUCCESS.toString();
    }

    private void initializeChronicleMap(String chronicleMapFilePath)
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
                    .createPersistedTo(new File(chronicleMapFilePath));

            logger.info("Created the chronicle map from persisted file: " + chronicleMapFilePath + " with " + persistedDisruptorMap.size() + " inventory positions.");
        }
        catch(IOException ioe)
        {
            logger.error(ioe.getMessage());
        }
    }

    public void start(String chronicleMapFilePath)
    {
        initializeChronicleMap(chronicleMapFilePath);
    }

    public void stop()
    {
        if(persistedDisruptorMap != null && persistedDisruptorMap.isOpen())
            persistedDisruptorMap.close();

        logger.info("Closed Chronicle map with inventory positions.");
    }

    public void uploadSODPositions(String startOfDayInventoryPositionFilePath)
    {
        if(persistedDisruptorMap == null || persistedDisruptorMap.isClosed())
        {
            logger.error("Chronicle map is in an invalid state and upload is not possible.");
            return;
        }

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
        catch (FileNotFoundException fnfe)
        {
            logger.error(fnfe.getLocalizedMessage());
        }
        catch (IOException ioe)
        {
            logger.error(ioe.getLocalizedMessage());
        }
    }
}
