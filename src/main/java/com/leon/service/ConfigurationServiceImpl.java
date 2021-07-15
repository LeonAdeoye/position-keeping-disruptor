package com.leon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationServiceImpl
{
    @Value("bufferSize")
    private int bufferSize;

    int getBufferSize()
    {
        return this.bufferSize;
    }

    void setBufferSize(int bufferSize)
    {
        this.bufferSize = bufferSize;
    }
}
