package com.leon.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClientCashPositionService implements PositionService
{
    private static final Logger logger = LoggerFactory.getLogger(ClientCashPositionService.class);

    private Map<Integer, Double> mapOfCash = new HashMap<>();

    public ClientCashPositionService()
    {
        // TODO remove and rely on SOD upload.
        int max = 100000, min = 5000;
        double randomValue = Math.floor(Math.random()*(max-min+1)+min);
        for(int index = 0; index < 30; ++index)
        mapOfCash.put(index, randomValue);
    }

    public void uploadCashPositions()
    {
        // TODO read from an SOD file instead.
        mapOfCash.clear();
    }

    public PositionResponse check(PositionRequest positionRequest)
    {
        if(mapOfCash.containsKey(positionRequest.getClientId()))
        {
            // TODO refine reserve logic.
            if(positionRequest.getDelta() <= mapOfCash.get(positionRequest.getClientId()))
                return new PositionResponse(mapOfCash.get(positionRequest.getClientId()) - positionRequest.getDelta(), OutcomeType.SUCCESS, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());

            if(positionRequest.getDelta() > mapOfCash.get(positionRequest.getClientId()))
                return new PositionResponse(positionRequest.getDelta(), OutcomeType.SUCCESS, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());

            return new PositionResponse(0.0, OutcomeType.SUCCESS, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());
        }
        else
            return new PositionResponse(0.0, OutcomeType.FAILURE, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());
    }
}
