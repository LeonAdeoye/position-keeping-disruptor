package com.leon.service;

import com.leon.processor.ProcessorService;

public class PayloadServiceFactory
{
    private PayloadServiceFactory() {}

    public static ProcessorService getInstance(String payloadType)
    {
        switch(payloadType)
        {
            default:
                throw new UnsupportedOperationException(payloadType);
        }
    }
}
