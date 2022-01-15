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
    private  ChronicleMap<String, Inventory> persistedDisruptorMap;

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

    private String printPositionInventory(Inventory inventory, int lockedQuantity)
    {
        return String.format("SOD position quantity: {0},\n Executed position quantity: {1},\n Reserved position quantity: {3},\n Recently locked position quantity: {4}",
                inventory.getStartOfDayQuantity(),
                inventory.getExecutedQuantity(),
                inventory.getReservedQuantity(),
                lockedQuantity);
    }

    private String printCashInventory(Inventory inventory, double lockedCash)
    {
        return String.format("SOD cash: {0},\n Executed cash: {1},\n Reserved cash: {3},\n Recently locked cash: {4}",
                inventory.getStartOfDayCash(),
                inventory.getExecutedCash(),
                inventory.getReservedCash(),
                lockedCash);
    }

    private String printInventory(Inventory inventory)
    {
        return String.format("SOD cash: {0},\n Executed cash: {1},\n Reserved cash: {3},\nSOD position quantity: {4},\n Executed position quantity: {5},\n Reserved position quantity: {6}",
                inventory.getStartOfDayCash(),
                inventory.getExecutedCash(),
                inventory.getReservedCash(),
                inventory.getStartOfDayQuantity(),
                inventory.getExecutedQuantity(),
                inventory.getReservedQuantity());
    }

    private double processCashCheckRequest(CheckCashRequestMessage checkRequestMessage)
    {
        String key = String.format("%06d%06d", checkRequestMessage.getInstrumentId(), checkRequestMessage.getClientId());
        Inventory inventory = persistedDisruptorMap.get(key);
        double balance = inventory.getStartOfDayCash() + inventory.getExecutedCash() - inventory.getReservedCash();
        double lockedCash = 0.0;

        if(balance > checkRequestMessage.getLockCash())
        {
            inventory.setReservedCash(inventory.getReservedCash() + checkRequestMessage.getLockCash());
            lockedCash = checkRequestMessage.getLockCash();
        }
        else if(balance > 0.0 && balance < checkRequestMessage.getLockCash())
        {
            inventory.setReservedCash(inventory.getReservedCash() + balance);
            lockedCash  =  balance;
        }

        persistedDisruptorMap.put(key, inventory);
        logger.info(String.format("Cash checked completed. For stock: {1} and client: {2} the current cash inventory is: {3}",
                checkRequestMessage.getInstrumentId(),
                checkRequestMessage.getClientId(),
                printCashInventory(inventory,
                lockedCash)));

        return lockedCash;
    }

    private int processPositionCheckRequest(CheckStockRequestMessage checkRequestMessage)
    {
        String key = String.format("%06d%06d", checkRequestMessage.getInstrumentId(), checkRequestMessage.getClientId());
        Inventory inventory = persistedDisruptorMap.get(key);
        int balance = inventory.getStartOfDayQuantity() + inventory.getExecutedQuantity() - inventory.getReservedQuantity();
        int lockedQuantity = 0;
        if(balance > checkRequestMessage.getLockQuantity())
        {
            inventory.setReservedQuantity(inventory.getReservedQuantity() + checkRequestMessage.getLockQuantity());
            lockedQuantity = checkRequestMessage.getLockQuantity();
        }
        if(balance > 0 && balance < checkRequestMessage.getLockQuantity())
        {
            inventory.setReservedQuantity(inventory.getReservedQuantity() + balance);
            lockedQuantity = balance;
        }

        persistedDisruptorMap.put(key, inventory);
        logger.info(String.format("Position check completed. For stock: {1} and client: {2} the current position inventory is: {3}",
                checkRequestMessage.getInstrumentId(),
                checkRequestMessage.getClientId(),
                printPositionInventory(inventory,
                lockedQuantity)));

        return lockedQuantity;
    }

    private String processExecution(ExecutionMessage executionMessage)
    {
        String key = String.format("%06%06d", executionMessage.getInstrumentId(), executionMessage.getClientId());
        Inventory inventory = persistedDisruptorMap.get(key);
        if(executionMessage.getSide() == 'B')
            inventory.setExecutedCash(inventory.getExecutedCash() + (executionMessage.getExecutedQuantity() * executionMessage.getExecutedPrice()));
        else
            inventory.setExecutedQuantity(inventory.getExecutedQuantity() - executionMessage.getExecutedQuantity());
        logger.info("Processed execution message: " + executionMessage + ", the current inventory is updated to: " + printInventory(inventory));
        persistedDisruptorMap.put(key, inventory);
        return OutcomeType.SUCCESS.toString();
    }

    private void initializeChronicleMap(String chronicleMapFilePath)
    {
        try
        {
            persistedDisruptorMap = ChronicleMapBuilder
                    .of(String.class, Inventory.class)
                    .name("position-inventory-map")
                    .entries(5_000)
                    .averageValue(new Inventory())
                    .valueMarshaller(InventorySerializer.getInstance())
                    .averageKey("000001000001")
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
            List<Inventory> positionInventories = objectMapper.readValue(
                    new File(startOfDayInventoryPositionFilePath),
                    new TypeReference<List<Inventory>>(){});

            positionInventories.forEach(inventory ->
                    persistedDisruptorMap.put(String.format("%05d%s", inventory.getClientId(), inventory.getInstrumentId()), inventory));

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
