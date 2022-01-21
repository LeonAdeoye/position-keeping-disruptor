package com.leon.io;

import com.leon.model.DisruptorPayload;
import reactor.core.publisher.Flux;

public interface DisruptorWriter
{
    void start();
    void writeAll(Flux<DisruptorPayload> payload);
    void write(DisruptorPayload payload);
    void stop();
    void toggleSilentMode();
}
