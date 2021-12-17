package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FileDisruptorReader implements DisruptorReader
{
    private String readerFileName;
    @Override
    public void initialize(ConfigurationServiceImpl configurationService)
    {
        readerFileName = configurationService.getReaderFileName();
    }

    @Override
    public Flux<DisruptorPayload> readAll()
    {
        return Flux.empty();
    }

    @Override
    public Mono<DisruptorPayload> read()
    {
        return Mono.empty();
    }
}
