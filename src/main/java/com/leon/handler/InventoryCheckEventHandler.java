package com.leon.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.leon.model.*;
import com.leon.service.DisruptorService;
import com.leon.service.FxService;
import com.leon.service.FxServiceImpl;
import com.leon.service.InstrumentService;
import com.lmax.disruptor.EventHandler;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InventoryCheckEventHandler implements EventHandler<DisruptorEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(InventoryCheckEventHandler.class);
    private DisruptorService outboundDisruptor;
    private InstrumentService instrumentService;
    private FxService fxService;
    private  ChronicleMap<String, Inventory> persistedDisruptorMap;

    public InventoryCheckEventHandler(DisruptorService outboundDisruptor, InstrumentService instrumentService, FxService fxService)
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
            logger.error("FX service is invalid.");
            throw new NullPointerException("FX service is invalid.");
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
        DisruptorPayload payload = event.getPayload();
        logger.info("Processing event with payload: " + payload);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String result;
        try
        {
            switch (RequestTypeEnum.valueOf(payload.getPayloadType()))
            {
                case CASH_CHECK_REQUEST:
                    result = mapper.writeValueAsString(processCashCheckRequest(MessageFactory.createCashCheckRequestMessage(payload.getPayload())));
                    outboundDisruptor.push(new DisruptorPayload("CASH_CHECK_RESPONSE", result, payload.getUid(), payload.getCreatedTime()));
                    break;
                case POSITION_CHECK_REQUEST:
                    result = mapper.writeValueAsString(processPositionCheckRequest(MessageFactory.createPositionCheckRequestMessage(payload.getPayload())));
                    outboundDisruptor.push(new DisruptorPayload("POSITION_CHECK_RESPONSE", result, payload.getUid(), payload.getCreatedTime()));
                    break;
                case EXECUTION_MESSAGE:
                    processExecution(MessageFactory.createExecutionMessage(payload.getPayload()));
            }
        }
        catch(IllegalArgumentException e)
        {
            logger.error("Event ignored because cannot convert " + payload.getPayloadType() + " to RequestTypeEnum. Exception thrown: " + e.getLocalizedMessage());
        }
        catch (JsonProcessingException e)
        {
            logger.error("Event ignored because cannot convert " + payload.getPayload() + " to JSON. Exception thrown: " + e.getLocalizedMessage());
        }
    }

    private InventoryCheckResponse processPositionCheckRequest(CheckPositionRequestMessage checkPositionRequestMessage)
    {
        Instant start = Instant.now();
        InventoryCheckResponse inventoryCheckResponse = new InventoryCheckResponse();
        String key = String.format("%06d%06d", checkPositionRequestMessage.getInstrumentId(), checkPositionRequestMessage.getClientId());
        Inventory inventory = persistedDisruptorMap.get(key);

        if(checkPositionRequestMessage.getLockQuantity() > 0)
            inventoryCheckResponse = handlePositionLockRequest(checkPositionRequestMessage, inventory);
        else if(checkPositionRequestMessage.getUnlockQuantity() > 0)
            inventoryCheckResponse = handlePositionUnlockRequest(checkPositionRequestMessage, inventory);

        persistedDisruptorMap.put(key, inventory);
        logger.info(String.format("Completed position check: %s, time taken: %d ms.", checkPositionRequestMessage, Duration.between(start, Instant.now()).toMillis()));
        return inventoryCheckResponse;
    }

    private InventoryCheckResponse processCashCheckRequest(CheckCashRequestMessage checkCashRequestMessage)
    {
        Instant start = Instant.now();
        InventoryCheckResponse inventoryCheckResponse = new InventoryCheckResponse();
        String key = String.format("%06d%06d", checkCashRequestMessage.getInstrumentId(), checkCashRequestMessage.getClientId());
        Inventory inventory = persistedDisruptorMap.get(key);

        if(checkCashRequestMessage.getLockCash() > 0)
            inventoryCheckResponse = handleCashLockRequest(checkCashRequestMessage, inventory);
        else if(checkCashRequestMessage.getUnlockCash() > 0)
            inventoryCheckResponse = handleCashUnlockRequest(checkCashRequestMessage, inventory);

        persistedDisruptorMap.put(key, inventory);
        logger.info(String.format("Completed cash check: %s, time taken: %d ms.", checkCashRequestMessage, Duration.between(start, Instant.now()).toMillis()));
        return inventoryCheckResponse;
    }

    private InventoryCheckResponse handleCashLockRequest(CheckCashRequestMessage checkCashRequestMessage, Inventory inventory)
    {
        double balance = inventory.getStartOfDayCash() + inventory.getExecutedCash() - inventory.getReservedCash();
        double lockedCash = checkCashRequestMessage.getLockCash();

        if(balance >= lockedCash)
        {
            inventory.setReservedCash(inventory.getReservedCash() + lockedCash);
            logger.info(String.format("Successfully locked FULL cash of %f. The inventory is now: %s", lockedCash, inventory));
            return new InventoryCheckResponse(0, 0, OutcomeType.SUCCESS.toString(), checkCashRequestMessage.getReferenceId(), checkCashRequestMessage.getClientId(),
                    checkCashRequestMessage.getInstrumentId(), lockedCash, 0.0, checkCashRequestMessage.getRequestType(), checkCashRequestMessage.getRequestType());
        }

        if(balance > 0.0 && balance < lockedCash)
        {
            inventory.setReservedCash(inventory.getReservedCash() + balance);
            logger.info(String.format("Successfully locked PARTIAL cash of %f. The inventory is now: %s", balance, inventory));
            return new InventoryCheckResponse(0, 0, OutcomeType.SUCCESS.toString(), checkCashRequestMessage.getReferenceId(), checkCashRequestMessage.getClientId(),
                    checkCashRequestMessage.getInstrumentId(), balance, 0.0, checkCashRequestMessage.getRequestType(), checkCashRequestMessage.getRequestType());
        }

        logger.error(String.format("Failed to lock cash: %d for inventory: %s", lockedCash, inventory));
        return new InventoryCheckResponse(0, 0, OutcomeType.FAILURE.toString(), checkCashRequestMessage.getReferenceId(), checkCashRequestMessage.getClientId(),
                checkCashRequestMessage.getInstrumentId(), 0.0, 0.0, checkCashRequestMessage.getRequestType(), checkCashRequestMessage.getRequestType());
    }

    private InventoryCheckResponse handleCashUnlockRequest(CheckCashRequestMessage checkCashRequestMessage, Inventory inventory)
    {
        double unlockedCash = checkCashRequestMessage.getUnlockCash();
        if(inventory.getReservedCash() >= unlockedCash)
        {
            inventory.setReservedCash(inventory.getReservedCash() - unlockedCash);
            logger.info(String.format("Successfully unlocked FULL cash of %d. The inventory is now: %s", unlockedCash, inventory));
            return new InventoryCheckResponse(0, 0, OutcomeType.SUCCESS.toString(), checkCashRequestMessage.getReferenceId(), checkCashRequestMessage.getClientId(),
                    checkCashRequestMessage.getInstrumentId(), 0.0, unlockedCash, checkCashRequestMessage.getRequestType(), checkCashRequestMessage.getRequestType());
        }
        else
        {
            logger.error(String.format("For inventory: %s, the reserved cash cannot be less than the unlock cash of %d", inventory, unlockedCash));
            return new InventoryCheckResponse(0, 0, OutcomeType.FAILURE.toString(), checkCashRequestMessage.getReferenceId(), checkCashRequestMessage.getClientId(),
                    checkCashRequestMessage.getInstrumentId(), 0.0, 0.0, checkCashRequestMessage.getRequestType(), checkCashRequestMessage.getRequestType());
        }
    }

    private InventoryCheckResponse handlePositionLockRequest(CheckPositionRequestMessage checkPositionRequestMessage, Inventory inventory)
    {
        int balance = 0;
        int lockedQuantity = checkPositionRequestMessage.getLockQuantity();

        if(RequestTypeEnum.valueOf(checkPositionRequestMessage.getRequestSubType()) == RequestTypeEnum.LONG_AND_COVERED_SHORT_SELL
            || RequestTypeEnum.valueOf(checkPositionRequestMessage.getRequestSubType()) == RequestTypeEnum.NAKED_SHORT_SELL)
            balance = (inventory.getStartOfDayQuantity() + inventory.getBorrowedQuantity() + inventory.getExecutedQuantity()) - inventory.getReservedQuantity();

        if(RequestTypeEnum.valueOf(checkPositionRequestMessage.getRequestSubType()) == RequestTypeEnum.LONG_SELL_ONLY)
            balance = (inventory.getStartOfDayQuantity() + inventory.getExecutedQuantity()) - inventory.getReservedQuantity();

        if(balance == 0)
        {
            if(RequestTypeEnum.valueOf(checkPositionRequestMessage.getRequestSubType()) == RequestTypeEnum.NAKED_SHORT_SELL)
            {
                inventory.setReservedQuantity(inventory.getReservedQuantity() + lockedQuantity);
                logger.error(String.format("For inventory: %s, the available position balance is zero but this is a naked short sell request so lock quantity is %d", inventory, lockedQuantity));
                return new InventoryCheckResponse(lockedQuantity, 0, OutcomeType.SUCCESS.toString(), checkPositionRequestMessage.getReferenceId(), checkPositionRequestMessage.getClientId(),
                        checkPositionRequestMessage.getInstrumentId(), 0.0, 0.0, checkPositionRequestMessage.getRequestType(), checkPositionRequestMessage.getRequestSubType());
            }
            else
            {
                logger.error(String.format("For inventory: %s, the available position balance is zero so unable to lock quantity of %d", inventory, lockedQuantity));
                return new InventoryCheckResponse(0, 0, OutcomeType.FAILURE.toString(), checkPositionRequestMessage.getReferenceId(), checkPositionRequestMessage.getClientId(),
                        checkPositionRequestMessage.getInstrumentId(), 0.0, 0.0, checkPositionRequestMessage.getRequestType(), checkPositionRequestMessage.getRequestSubType());
            }

        }

        if(balance >= lockedQuantity)
        {
            inventory.setReservedQuantity(inventory.getReservedQuantity() + lockedQuantity);
            logger.info(String.format("Successfully locked FULL quantity of %d. The inventory is now: %s", lockedQuantity, inventory));
            return new InventoryCheckResponse(lockedQuantity, 0, OutcomeType.SUCCESS.toString(), checkPositionRequestMessage.getReferenceId(), checkPositionRequestMessage.getClientId(),
                    checkPositionRequestMessage.getInstrumentId(), 0.0, 0.0, checkPositionRequestMessage.getRequestType(), checkPositionRequestMessage.getRequestSubType());
        }

        if(balance > 0 && balance < lockedQuantity)
        {
            if(RequestTypeEnum.valueOf(checkPositionRequestMessage.getRequestSubType()) == RequestTypeEnum.NAKED_SHORT_SELL)
            {
                inventory.setReservedQuantity(inventory.getReservedQuantity() + lockedQuantity);
                logger.error(String.format("For inventory: %s, the available position balance is less than the lock quantity but this is a naked short sell request so lock quantity is %d", inventory, lockedQuantity));
                return new InventoryCheckResponse(lockedQuantity, 0, OutcomeType.SUCCESS.toString(), checkPositionRequestMessage.getReferenceId(), checkPositionRequestMessage.getClientId(),
                        checkPositionRequestMessage.getInstrumentId(), 0.0, 0.0, checkPositionRequestMessage.getRequestType(), checkPositionRequestMessage.getRequestSubType());
            }
            else
            {
                inventory.setReservedQuantity(inventory.getReservedQuantity() + balance);
                logger.info(String.format("Successfully locked PARTIAL quantity of %d. The inventory is now: %s", balance, inventory));
                return new InventoryCheckResponse(lockedQuantity, 0, OutcomeType.SUCCESS.toString(), checkPositionRequestMessage.getReferenceId(), checkPositionRequestMessage.getClientId(),
                        checkPositionRequestMessage.getInstrumentId(), 0.0, 0.0, checkPositionRequestMessage.getRequestType(), checkPositionRequestMessage.getRequestSubType());
            }
        }

        logger.error(String.format("Unable to lock %d for inventory: ", lockedQuantity, inventory));
        return new InventoryCheckResponse(0, 0, OutcomeType.FAILURE.toString(), checkPositionRequestMessage.getReferenceId(), checkPositionRequestMessage.getClientId(),
                checkPositionRequestMessage.getInstrumentId(), 0.0, 0.0, checkPositionRequestMessage.getRequestType(), checkPositionRequestMessage.getRequestSubType());
    }

    private InventoryCheckResponse handlePositionUnlockRequest(CheckPositionRequestMessage checkPositionRequestMessage, Inventory inventory)
    {
        int unlockedQuantity = checkPositionRequestMessage.getUnlockQuantity();
        if(inventory.getReservedQuantity() >= unlockedQuantity)
        {
            inventory.setReservedQuantity(inventory.getReservedQuantity() - unlockedQuantity);
            logger.info(String.format("Successfully unlocked FULL quantity of %d. The inventory is now: %s", unlockedQuantity, inventory));
            return new InventoryCheckResponse(0, unlockedQuantity, OutcomeType.SUCCESS.toString(), checkPositionRequestMessage.getReferenceId(), checkPositionRequestMessage.getClientId(),
                    checkPositionRequestMessage.getInstrumentId(), 0.0, 0.0, checkPositionRequestMessage.getRequestType(), checkPositionRequestMessage.getRequestSubType());
        }
        else
        {
            logger.error(String.format("For inventory: %s, the reserved quantity cannot be less than the unlock quantity of %d", inventory, unlockedQuantity));
            return new InventoryCheckResponse(0, 0, OutcomeType.FAILURE.toString(), checkPositionRequestMessage.getReferenceId(), checkPositionRequestMessage.getClientId(),
                    checkPositionRequestMessage.getInstrumentId(), 0.0, 0.0, checkPositionRequestMessage.getRequestType(), checkPositionRequestMessage.getRequestSubType());
        }
    }

    private void processExecution(ExecutionMessage executionMessage)
    {
        String key = String.format("%06d%06d", executionMessage.getInstrumentId(), executionMessage.getClientId());
        Inventory inventory = persistedDisruptorMap.get(key);

        if(executionMessage.getSide() != 'B')
        {
            Optional<FxRate> fxRateOptional = fxService.get(executionMessage.getCurrency());
            if(!fxRateOptional.isPresent())
                logger.warn(String.format("While processing sell execution the FX rate for currency: %s is missing from FX Service. The default FX rate of 1.0 will be used.", executionMessage.getCurrency()));
            inventory.setExecutedCash(inventory.getExecutedCash() + (executionMessage.getExecutedQuantity() * executionMessage.getExecutedPrice() * fxRateOptional.orElse(FxServiceImpl.defaultUSDRate).getFxRateAgainstUSD()));
        }
        else
            inventory.setExecutedQuantity(inventory.getExecutedQuantity() + executionMessage.getExecutedQuantity());


        logger.info(String.format("Processed execution message: %s, the current inventory is updated to: %s", executionMessage, inventory));
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

            // Warm-up step to make the chronicle map get method faster
            persistedDisruptorMap.get("000001000001");
            persistedDisruptorMap.get("999999999999");

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
            List<Inventory> positionInventories = objectMapper.readValue(new File(startOfDayInventoryPositionFilePath), new TypeReference<List<Inventory>>(){});

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

    public List<Inventory> getInventory()
    {
        return persistedDisruptorMap.values().stream().collect(Collectors.toList());
    }

    public void clearInventory()
    {
        persistedDisruptorMap.clear();
    }

    public void updateInventory(Inventory inventory)
    {
        String key = String.format("%06d%06d", inventory.getInstrumentId(), inventory.getClientId());
        persistedDisruptorMap.put(key, inventory);
    }

    public void deleteInventory(Inventory inventory)
    {
        String key = String.format("%06d%06d", inventory.getInstrumentId(), inventory.getClientId());
        persistedDisruptorMap.remove(key);
    }
}
