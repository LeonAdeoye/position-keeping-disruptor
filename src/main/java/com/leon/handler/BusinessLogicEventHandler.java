package com.leon.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.model.*;
import com.leon.service.DisruptorService;
import com.leon.service.FxService;
import com.leon.service.InstrumentService;
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
    private InstrumentService instrumentService;
    private FxService fxService;
    private  ChronicleMap<String, Inventory> persistedDisruptorMap;

    public BusinessLogicEventHandler(DisruptorService outboundDisruptor, InstrumentService instrumentService, FxService fxService)
    {
        if(instrumentService == null)
        {
            logger.error("Instrument service is invalid.");
            throw new NullPointerException("Instrument service is invalid.");
        }
        else
            this.instrumentService = instrumentService;


        if(fxService == null)
        {
            logger.error("Fx service is invalid.");
            throw new NullPointerException("Fx service is invalid.");
        }
        else
            this.fxService = fxService;

        if(outboundDisruptor == null)
        {
            logger.error("Outbound disruptor is invalid.");
            throw new NullPointerException("Outbound disruptor is invalid.");
        }
        else
            this.outboundDisruptor = outboundDisruptor;
    }

    public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch)
    {
        try
        {
            switch (RequestTypeEnum.valueOf(event.getPayload().getPayloadType()))
            {
                case CASH_CHECK_REQUEST:
                    processCashCheckRequest(MessageFactory.createCashCheckRequestMessage(event.getPayload().getPayload()));
                    break;
                case POSITION_CHECK_REQUEST:
                    processPositionCheckRequest(MessageFactory.createPositionCheckRequestMessage(event.getPayload().getPayload()));
                    break;
                case EXECUTION_MESSAGE:
                    processExecution(MessageFactory.createExecutionMessage(event.getPayload().getPayload()));
            }
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
            logger.error("Event ignored because cannot convert " + event.getPayload().getPayloadType() + " to RequestTypeEnum. Exception thrown: " + e.getLocalizedMessage());
        }
        outboundDisruptor.push(new DisruptorPayload("RESPONSE", "Result is??")); // TODO
    }

    private String printCashInventory(Inventory inventory, double lockedCash)
    {
        return String.format("SOD cash: %f, Executed cash: %f, Reserved cash: %f, Recently locked cash: %f",
                inventory.getStartOfDayCash(),
                inventory.getExecutedCash(),
                inventory.getReservedCash(),
                lockedCash);
    }

    private String printInventory(Inventory inventory)
    {
        return String.format("SOD cash: 5f, Executed cash: %f, Reserved cash: %f,SOD position quantity: %d, Executed position quantity: %d, Reserved position quantity: %d",
                inventory.getStartOfDayCash(),
                inventory.getExecutedCash(),
                inventory.getReservedCash(),
                inventory.getStartOfDayQuantity(),
                inventory.getExecutedQuantity(),
                inventory.getReservedQuantity());
    }

    private double processCashCheckRequest(CheckCashRequestMessage checkRequestMessage)
    {
        // TODO Handle FX.
        String key = String.format("%06d%06d", checkRequestMessage.getInstrumentId(), checkRequestMessage.getClientId());
        Inventory inventory = persistedDisruptorMap.get(key);

        double balance = inventory.getStartOfDayCash() + inventory.getExecutedCash() - inventory.getReservedCash();
        double lockedCash = 0.0;

        if(balance >= checkRequestMessage.getLockCash())
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
        logger.info(String.format("Cash checked completed. For stock: %06d and client: %06d the current cash inventory is: %s",
                checkRequestMessage.getInstrumentId(), checkRequestMessage.getClientId(), printCashInventory(inventory, lockedCash)));

        return lockedCash;
    }

    private void processPositionCheckRequest(CheckPositionRequestMessage checkPositionRequestMessage)
    {
        String key = String.format("%06d%06d", checkPositionRequestMessage.getInstrumentId(), checkPositionRequestMessage.getClientId());
        Inventory inventory = persistedDisruptorMap.get(key);
        int result = 0;

        if(checkPositionRequestMessage.getLockQuantity() > 0)
            result = handlePositionLockRequest(checkPositionRequestMessage, inventory);
        else if(checkPositionRequestMessage.getUnlockQuantity() > 0)
            result = handlePositionUnlockRequest(checkPositionRequestMessage, inventory);

        persistedDisruptorMap.put(key, inventory);
        logger.info(String.format("Position check request completed for stock: %06d and client: %06d",
                checkPositionRequestMessage.getInstrumentId(),
                checkPositionRequestMessage.getClientId()));
    }

    private int handlePositionLockRequest(CheckPositionRequestMessage checkPositionRequestMessage, Inventory inventory)
    {
        int balance = (inventory.getStartOfDayQuantity() + inventory.getBorrowedQuantity() + inventory.getExecutedQuantity()) - inventory.getReservedQuantity();

        if(balance == 0)
        {
            logger.error(String.format("For inventory: %s, the available position balance is zero - cannot lock quantity of %d", inventory, checkPositionRequestMessage.getLockQuantity()));
            return 0;
        }

        if(balance >= checkPositionRequestMessage.getLockQuantity())
        {
            inventory.setReservedQuantity(inventory.getReservedQuantity() + checkPositionRequestMessage.getLockQuantity());
            logger.info(String.format("Successfully locked FULL quantity of %d. The inventory is now: %s", checkPositionRequestMessage.getLockQuantity(), inventory));
            return checkPositionRequestMessage.getLockQuantity();
        }

        if(balance > 0 && balance < checkPositionRequestMessage.getLockQuantity())
        {
            inventory.setReservedQuantity(inventory.getReservedQuantity() + balance);
            logger.info(String.format("Successfully locked PARTIAL quantity of %d. The inventory is now: %s", balance, inventory));
            return balance;
        }

        logger.error(String.format("Unable to lock %d for inventory: ", checkPositionRequestMessage.getLockQuantity(), inventory));
        return  0;
    }

    private int handlePositionUnlockRequest(CheckPositionRequestMessage checkPositionRequestMessage, Inventory inventory)
    {
        if(inventory.getReservedQuantity() >= checkPositionRequestMessage.getUnlockQuantity())
        {
            inventory.setReservedQuantity(inventory.getReservedQuantity() - checkPositionRequestMessage.getUnlockQuantity());
            logger.info(String.format("Successfully unlocked FULL quantity of %d. The inventory is now: %s", checkPositionRequestMessage.getUnlockQuantity(), inventory));
            return checkPositionRequestMessage.getUnlockQuantity();
        }
        else
        {
            logger.error(String.format("For inventory: %s, the reserved quantity cannot be less than the unlock quantity of %d", inventory, checkPositionRequestMessage.getUnlockQuantity()));
            return 0;
        }
    }

    private void processExecution(ExecutionMessage executionMessage)
    {
        String key = String.format("%06d%06d", executionMessage.getInstrumentId(), executionMessage.getClientId());
        Inventory inventory = persistedDisruptorMap.get(key);

        if(executionMessage.getSide() == 'B')
            inventory.setExecutedQuantity(inventory.getExecutedQuantity() + executionMessage.getExecutedQuantity());
        else //TODO handle FX.
            inventory.setExecutedCash(inventory.getExecutedCash() + (executionMessage.getExecutedQuantity() * executionMessage.getExecutedPrice()));

        logger.info(String.format("Processed execution message: %s, the current inventory is updated to: %s", executionMessage, printInventory(inventory)));
        persistedDisruptorMap.put(key, inventory);
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
                    persistedDisruptorMap.put(String.format("%06d%06d",  inventory.getInstrumentId(), inventory.getClientId()), inventory));

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
