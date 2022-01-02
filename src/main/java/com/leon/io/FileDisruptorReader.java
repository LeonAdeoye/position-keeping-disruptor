package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.BaseStream;

@Component
public class FileDisruptorReader implements DisruptorReader
{
    private static final Logger logger = LoggerFactory.getLogger(FileDisruptorReader.class);
    private String readerFileName;

    @Override
    public void initialize(ConfigurationServiceImpl configurationService)
    {
        this.readerFileName = configurationService.getReaderFileName();
    }

    @Override
    public Flux<DisruptorPayload> readAll()
    {
        return Flux.empty();
    }

    @Override
    public Mono<DisruptorPayload> read()
    {
        return Mono.just(new DisruptorPayload(""));
    }

    @Override
    public void close()
    {

    }
}
