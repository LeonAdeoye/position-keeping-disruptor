package com.leon.io;

import com.leon.model.DisruptorPayload;
import reactor.core.publisher.Flux;

public interface DisruptorReader
{
    void start(String readerFilePath);
    Flux<DisruptorPayload> readAll();
    void stop();
}
