package com.leon.service;

import com.leon.disruptor.PositionRequest;
import com.leon.disruptor.PositionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientStockPositionService implements PositionService
{
    private static final Logger logger = LoggerFactory.getLogger(ClientStockPositionService.class);

    public PositionResponse check(PositionRequest positionRequest)
    {
        return new PositionResponse(1000, OutcomeType.SUCCESS, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());
    }
}
