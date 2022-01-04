package com.leon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationServiceImpl
{
    @Value("${bufferSize}")
    private int bufferSize;

    @Value("${writerFilePath}")
    private String writerFilePath;

    @Value("${readerFilePath}")
    private String readerFilePath;

    public int getBufferSize()
    {
        return bufferSize;
    }

    public String getWriterFilePath()
    {
        return writerFilePath;
    }

    public String getReaderFilePath()
    {
        return readerFilePath;
    }
}
