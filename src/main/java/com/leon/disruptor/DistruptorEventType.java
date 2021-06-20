package com.leon.disruptor;

enum DistruptorEventType
{
    ORDER,
    EXECUTION,
    CASH,
    STOCK
}

enum DistruptorEventRequestType
{
    LOCK,
    UNLOCK
}
