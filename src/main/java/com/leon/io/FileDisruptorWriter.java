package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FileDisruptorWriter implements DisruptorWriter
{
    private static final Logger logger = LoggerFactory.getLogger(FileDisruptorWriter.class);
    private String writerFilePath;

    @Override
    public void start(ConfigurationServiceImpl configurationService)
    {
        writerFilePath = configurationService.getWriterFilePath();
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

    @Override
    public void stop()
    {

    }
}
