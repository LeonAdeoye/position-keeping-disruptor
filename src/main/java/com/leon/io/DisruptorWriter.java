package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DisruptorWriter
{
    void start(ConfigurationServiceImpl configurationService);
    int writeAll(Flux<DisruptorPayload> payload);
    void write(Mono<DisruptorPayload> payload);
    void stop();
}
