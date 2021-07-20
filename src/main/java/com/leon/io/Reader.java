package com.leon.io;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Reader
{
    void initialize();
    Flux<Payload> readAll();
    Mono<Payload> read();
}
