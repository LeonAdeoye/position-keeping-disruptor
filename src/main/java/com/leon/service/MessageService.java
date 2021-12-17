package com.leon.service;

import com.leon.model.DisruptorPayload;
import com.leon.io.DisruptorReader;
import com.leon.io.DisruptorWriter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageService
{
    void setReader(DisruptorReader reader);
    void setWriter(DisruptorWriter writer);
    Flux<DisruptorPayload> readAll();
    Mono<DisruptorPayload> read();
    void write(Mono<DisruptorPayload> payload);
    int writeAll(Flux<DisruptorPayload> payload);
}
