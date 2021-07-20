package com.leon.service;

import com.leon.io.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageServiceImpl implements MessageService
{
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

    @Override
    public void write(Mono<Payload> payload)
    {

    }

    @Override
    public int writeAll(Flux<Payload> payload)
    {
        return 0;
    }
}
