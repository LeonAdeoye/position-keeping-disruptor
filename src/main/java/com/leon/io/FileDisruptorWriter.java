package com.leon.io;

import com.leon.model.DisruptorPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.FileWriter;
import java.io.IOException;

@Component("FileDisruptorWriter")
public class FileDisruptorWriter implements DisruptorWriter
{
    private static final Logger logger = LoggerFactory.getLogger(FileDisruptorWriter.class);
    @Value("${writer.file.path}")
    private String writerFilePath;
    @Value("${is.primary}")
    private boolean isPrimary;
    FileWriter fileWriter;

    @Override
    public void start()
    {
        try
        {
            FileWriter fileWriter = new FileWriter(writerFilePath);
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            logger.error("Failed to open file because of exception: " +  ioe.getLocalizedMessage());
        }
    }

    @Override
    public void writeAll(Flux<DisruptorPayload> payload)
    {
        payload.subscribe(load -> write(load));
    }

    @Override
    public void write(DisruptorPayload payload)
    {
        try
        {
            if(isPrimary)
                fileWriter.write(String.valueOf(payload));
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            logger.error("Failed to write file because of exception: " +  ioe.getLocalizedMessage());
        }
    }

    @Override
    public void stop()
    {
        try
        {
            fileWriter.flush();
            fileWriter.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            logger.error("Failed to close file because of exception: " +  ioe.getLocalizedMessage());
        }
    }

    @Override
    public boolean togglePrimary()
    {
        isPrimary = !isPrimary;
        logger.info(isPrimary ? "Now running in PRIMARY mode." : "Now running in SECONDARY mode.");
        return isPrimary;
    }
}
