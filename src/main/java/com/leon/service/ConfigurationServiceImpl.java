package com.leon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationServiceImpl
{
    @Value("${is.primary}")
    private boolean isPrimary;

    @Value("${bufferSize}")
    private int bufferSize;

    @Value("${chronicleMapFilePath}")
    private String chronicleMapFilePath;

    public int getBufferSize()
    {
        return bufferSize;
    }

    public String getChronicleMapFilePath() { return chronicleMapFilePath; }

    public boolean isPrimary()
    {
        return isPrimary;
    }

    public void setPrimary(boolean primary)
    {
        isPrimary = primary;
    }
}
