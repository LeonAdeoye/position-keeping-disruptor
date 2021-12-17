package com.leon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationServiceImpl
{
    @Value("${bufferSize}")
    private int bufferSize;

    @Value("${writerFileName}")
    private String writerFileName;

    @Value("${readerFileName}")
    private String readerFileName;

    public int getBufferSize()
    {
        return this.bufferSize;
    }

    public String getWriterFileName()
    {
        return this.writerFileName;
    }

    public String getReaderFileName()
    {
        return this.readerFileName;
    }
}
