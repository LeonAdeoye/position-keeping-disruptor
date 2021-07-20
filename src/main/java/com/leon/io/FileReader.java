package com.leon.io;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FileReader implements InboundDeliveryMechanism
{
    @Override
    public void initialize()
    {

    }

    @Override
    public Flux<Payload> readAll()
    {
        return Flux.empty();
    }

    @Override
    public Mono<Payload> read()
    {
        return Mono.empty();
    }
}
