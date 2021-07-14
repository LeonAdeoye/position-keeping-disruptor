package com.leon.service;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl
{
    private int bufferSize = 4096;

    int getBufferSize()
    {
        return this.bufferSize;
    }

    void setBufferSize(int bufferSize)
    {
        this.bufferSize = bufferSize;
    }
}
