package com.leon.io;

import com.leon.model.DisruptorPayload;
import reactor.core.publisher.Flux;

public interface DisruptorReader
{
    void start();
    Flux<DisruptorPayload> readAll();
    void stop();
}
