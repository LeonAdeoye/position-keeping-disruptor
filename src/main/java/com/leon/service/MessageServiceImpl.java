package com.leon.service;

import com.leon.io.Reader;
import com.leon.io.Writer;
import com.leon.io.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageServiceImpl implements MessageService
{
    private Reader reader;
    private Writer writer;

    @Override
    public void setReader(Reader reader)
    {
        this.reader = reader;
    }

    @Override
    public void setWriter(Writer writer)
    {
        this.writer = writer;
    }

    @Override
    public Flux<Payload> readAll()
    {
        return reader.readAll();
    }

    @Override
    public Mono<Payload> read()
    {
        return reader.read();
    }

    @Override
    public void write(Mono<Payload> payload)
    {
        writer.write(payload);
    }

    @Override
    public int writeAll(Flux<Payload> payload)
    {
        return writer.writeAll(payload);
    }
}
