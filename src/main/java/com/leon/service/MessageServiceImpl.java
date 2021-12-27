package com.leon.service;

import com.leon.io.DisruptorReader;
import com.leon.io.DisruptorWriter;
import com.leon.model.DisruptorPayload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageServiceImpl implements MessageService
{
    private DisruptorReader reader;
    private DisruptorWriter writer;

    public MessageServiceImpl(DisruptorReader reader, DisruptorWriter writer)
    {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void setReader(DisruptorReader reader)
    {
        this.reader = reader;
    }

    @Override
    public void setWriter(DisruptorWriter writer)
    {
        this.writer = writer;
    }

    @Override
    public Flux<DisruptorPayload> readAll()
    {
        return reader.readAll();
    }

    @Override
    public Mono<DisruptorPayload> read()
    {
        return reader.read();
    }

    @Override
    public void write(Mono<DisruptorPayload> payload)
    {
        writer.write(payload);
    }

    @Override
    public int writeAll(Flux<DisruptorPayload> payload)
    {
        return writer.writeAll(payload);
    }
}
