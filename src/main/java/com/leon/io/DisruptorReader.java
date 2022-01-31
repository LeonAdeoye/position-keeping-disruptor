package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import reactor.core.publisher.Flux;

public interface DisruptorReader
{
    void start();
    void start(String fileNamePath);
    void start(ConfigurationServiceImpl configurationService);

    Flux<DisruptorPayload> readAll();
    void stop();
}
