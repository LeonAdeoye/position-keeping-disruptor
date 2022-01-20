package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class FileDisruptorWriter implements DisruptorWriter
{
    private static final Logger logger = LoggerFactory.getLogger(FileDisruptorWriter.class);
    private String writerFilePath;
    private boolean inSilentMode;

    @Override
    public void start(ConfigurationServiceImpl configurationService)
    {
        writerFilePath = configurationService.getWriterFilePath();
        inSilentMode = configurationService.inSilentMode();
    }

    @Override
    public void writeAll(Flux<DisruptorPayload> payload)
    {
    }

    @Override
    public void write(DisruptorPayload payload)
    {

    }

    @Override
    public void stop()
    {

    }

    @Override
    public void toggleSilentMode()
    {
        inSilentMode = !inSilentMode;
        logger.info(inSilentMode ? "Silent mode toggled ON!" : "Silent mode toggled OFF!");
    }
}
