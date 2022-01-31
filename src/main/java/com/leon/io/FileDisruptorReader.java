package com.leon.io;

import com.leon.model.DisruptorPayload;
import com.leon.service.ConfigurationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component("FileDisruptorReader")
public class FileDisruptorReader implements DisruptorReader
{
    private static final Logger logger = LoggerFactory.getLogger(FileDisruptorReader.class);
    @Value("${reader.file.path}")
    private String readerFilePath;
    private BufferedReader reader;

    @Override
    public void start()
    {
        try
        {
            reader = new BufferedReader(new FileReader(readerFilePath));
        }
        catch(IOException ioe)
        {
            logger.error("Failed to instantiate buffered reader for file name: " + readerFilePath + " due to exception: " + ioe.getLocalizedMessage());
        }
    }

    @Override
    public void start(ConfigurationServiceImpl configurationService)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void start(String readerFilePath)
    {
        try
        {
            this.readerFilePath = readerFilePath;
            reader = new BufferedReader(new FileReader(readerFilePath));
        }
        catch(IOException ioe)
        {
            logger.error("Failed to instantiate buffered reader for file name: " + readerFilePath + " due to exception: " + ioe.getLocalizedMessage());
        }
    }

    @Override
    public Flux<DisruptorPayload> readAll()
    {
        if(reader == null)
        {
            logger.error("Reader is null so returning empty flux.");
            return Flux.empty();
        }

        Flux<DisruptorPayload> result = Flux.generate(() -> "", (state, sink) ->
        {
            String nextLine = this.readNext();
            if(nextLine == null)
                sink.complete();
            else
            {
                String[] splitInput  = nextLine.split("=");
                if (splitInput.length == 2)
                    sink.next(new DisruptorPayload(splitInput[0], splitInput[1]));
                else
                    logger.error("String not in correct format");
            }
            return state;
        });
        return result;
    }

    public String readNext()
    {
        try
        {
            if(reader != null)
                return reader.readLine();
        }
        catch(IOException ioe)
        {
            logger.error("Failed to read next line due to exception: " + ioe.getLocalizedMessage());;
        }
        return null;
    }

    @Override
    public void stop()
    {
        try
        {
            if(reader != null)
                reader.close();
        }
        catch(IOException ioe)
        {
            logger.error("Failed to close buffered reader due to exception: " + ioe.getLocalizedMessage());
        }
    }
}
