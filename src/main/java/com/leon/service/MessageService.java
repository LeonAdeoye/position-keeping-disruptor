package com.leon.service;

import com.leon.io.Payload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageService
{
    Flux<Payload> readAll();
    Mono<Payload> read();
    void write(Mono<Payload> payload);
    int writeAll(Flux<Payload> payload);
}
