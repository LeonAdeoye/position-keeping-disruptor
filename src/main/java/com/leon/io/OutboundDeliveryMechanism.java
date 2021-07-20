package com.leon.io;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OutboundDeliveryMechanism
{
    void initialize();
    int writeAll(Flux<Payload> payload);
    void write(Mono<Payload> payload);
}
