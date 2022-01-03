package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class FileDisruptorReader implements DisruptorReader
{
    private static final Logger logger = LoggerFactory.getLogger(FileDisruptorReader.class);
    private String readerFilePath;

    @Override
    public void initialize(ConfigurationServiceImpl configurationService)
    {
        this.readerFilePath = configurationService.getReaderFilePath();
    }

    @Override
    public Flux<DisruptorPayload> readAll()
    {
        try (Stream<String> linesStream = Files.lines(Paths.get(ClassLoader.getSystemResource(this.readerFilePath).toURI())))
        {
            return Flux.fromStream(linesStream.map(DisruptorPayload::new));
        }
        catch(Exception ex)
        {
            logger.error("Failed to read file because of exception: " + ex);
            return Flux.empty();
        }
    }

    @Override
    public void close()
    {

    }
}
