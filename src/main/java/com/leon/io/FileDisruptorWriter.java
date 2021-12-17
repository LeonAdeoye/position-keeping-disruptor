package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FileDisruptorWriter implements DisruptorWriter
{
    private String writerFileName;
    @Override
    public void initialize(ConfigurationServiceImpl configurationService)
    {
        this.writerFileName = configurationService.getWriterFileName();
    }

    @Override
    public int writeAll(Flux<DisruptorPayload> payload)
    {
        return 0;
    }

    @Override
    public void write(Mono<DisruptorPayload> payload)
    {

    }
}
