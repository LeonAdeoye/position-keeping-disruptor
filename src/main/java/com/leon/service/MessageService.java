package com.leon.service;

import com.leon.io.Payload;
import com.leon.io.Reader;
import com.leon.io.Writer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageService
{
    void setReader(Reader reader);
    void setWriter(Writer writer);
    Flux<Payload> readAll();
    Mono<Payload> read();
    void write(Mono<Payload> payload);
    int writeAll(Flux<Payload> payload);
}
