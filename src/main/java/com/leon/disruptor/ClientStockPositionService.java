package com.leon.disruptor;

public class ClientStockPositionService implements PositionService
{
    public PositionResponse check(PositionRequest positionRequest)
    {
        return new PositionResponse(1000, OutcomeType.SUCCESS, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());
    }
}
