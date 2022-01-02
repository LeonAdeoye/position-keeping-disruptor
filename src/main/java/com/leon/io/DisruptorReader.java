package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DisruptorReader
{
    void initialize(ConfigurationServiceImpl configurationService);
    Flux<DisruptorPayload> readAll();
    Mono<DisruptorPayload> read();
    void close();
}
