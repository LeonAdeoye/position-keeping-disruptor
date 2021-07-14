package com.leon.service;

import com.leon.disruptor.PositionRequest;
import com.leon.disruptor.PositionResponse;

public interface PositionService
{
    PositionResponse check(PositionRequest positionRequest);
}
