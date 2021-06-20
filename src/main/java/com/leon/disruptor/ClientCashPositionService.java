package com.leon.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientCashPositionService implements PositionService
{
    private static final Logger logger = LoggerFactory.getLogger(ClientCashPositionService.class);

    public PositionResponse check(PositionRequest positionRequest)
    {
        return new PositionResponse(100, OutcomeType.SUCCESS, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());
    }
}
