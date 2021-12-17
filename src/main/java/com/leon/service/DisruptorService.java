package com.leon.service;

import com.leon.model.DisruptorEvent;
import com.leon.model.DisruptorPayload;
import com.lmax.disruptor.EventHandler;

public interface DisruptorService
{
    void start(String name, EventHandler<DisruptorEvent> journalHandler, EventHandler<DisruptorEvent> actionEventHandler);
    void stop();
    void push(DisruptorPayload payLoad);
}
