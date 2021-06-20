package com.leon.disruptor;

public class PositionServiceFactory
{
    private static PositionService clientCashPositionService = new ClientCashPositionService();
    private static PositionService clientStockPositionService = new ClientStockPositionService();

    private PositionServiceFactory() {}

    public static PositionService getInstance(DistruptorEventType eventType)
    {
        switch(eventType)
        {
            case CASH:
                return clientCashPositionService;
            case STOCK:
                return clientStockPositionService;
            default:
                throw new UnsupportedOperationException(eventType.toString());
        }
    }
}
