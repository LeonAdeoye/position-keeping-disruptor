package com.leon.disruptor;

import com.lmax.disruptor.EventHandler;

public class PositionEventHandler implements EventHandler<DistruptorEvent>
{
    ClientStockPositionService stockPositionService = new ClientStockPositionService();
    ClientCashPositionService cashPositionService = new ClientCashPositionService();

    public void onEvent(DistruptorEvent event, long sequence, boolean endOfBatch)
    {
        switch(event.getPositionRequest().getEventType())
        {
            case CASH:
                cashPositionService.check(event.getPositionRequest().getClientId(),event.getPositionRequest().getDelta());
                break;
            case STOCK:
                stockPositionService.check(event.getPositionRequest().getClientId(), event.getPositionRequest().getRIC(), event.getPositionRequest().getDelta());
                break;
            default:
                throw new UnsupportedOperationException(event.getPositionRequest().getEventType().toString());
        }

        if(event.getPositionRequest().getClientId() % 5000000 == 0)
            System.out.println(event);
    }
}
