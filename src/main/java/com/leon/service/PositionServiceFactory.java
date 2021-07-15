package com.leon.service;

import com.leon.service.ClientCashPositionService;
import com.leon.service.ClientStockPositionService;
import com.leon.service.DistruptorEventType;
import com.leon.service.PositionService;

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
