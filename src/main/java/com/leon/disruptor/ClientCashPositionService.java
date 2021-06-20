package com.leon.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ClientCashPositionService implements PositionService
{
    private static final Logger logger = LoggerFactory.getLogger(ClientCashPositionService.class);

    private Map<Integer, Double> mapOfCash = new HashMap<>();

    public PositionResponse check(PositionRequest positionRequest)
    {
        if(mapOfCash.containsKey(positionRequest.getClientId()))
        {
            // TODO refine logic
            if(positionRequest.getDelta() <= mapOfCash.get(positionRequest.getClientId()))
                return new PositionResponse(mapOfCash.get(positionRequest.getClientId()) - positionRequest.getDelta(), OutcomeType.SUCCESS, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());
        }
        else
            return new PositionResponse(0, OutcomeType.SUCCESS, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());
    }
}
