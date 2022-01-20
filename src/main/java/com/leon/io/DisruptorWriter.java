package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import reactor.core.publisher.Flux;

public interface DisruptorWriter
{
    void start(ConfigurationServiceImpl configurationService);
    void writeAll(Flux<DisruptorPayload> payload);
    void write(DisruptorPayload payload);
    void stop();
    void toggleSilentMode();
}
