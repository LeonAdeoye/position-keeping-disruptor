package com.leon.disruptor;

public class ClientCashPositionService implements PositionService
{
    public PositionResponse check(PositionRequest positionRequest)
    {
        return new PositionResponse(100, OutcomeType.SUCCESS, positionRequest.getClientId(), positionRequest.getRIC(), positionRequest.getEventType(), positionRequest.getRequestType());
    }
}
