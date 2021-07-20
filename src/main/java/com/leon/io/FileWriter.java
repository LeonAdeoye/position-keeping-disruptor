package com.leon.io;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FileWriter implements OutboundDeliveryMechanism
{
    @Override
    public void initialize()
    {

    }

    @Override
    public int writeAll(Flux<Payload> payload)
    {
        return 0;
    }

    @Override
    public void write(Mono<Payload> payload)
    {

    }
}
