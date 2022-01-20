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

    @Value("${writerFilePath}")
    private String writerFilePath;

    @Value("${readerFilePath}")
    private String readerFilePath;

    @Value("${startOfDayInventoryPositionFilePath}")
    private String startOfDayInventoryPositionFilePath;

    @Value("${chronicleMapFilePath}")
    private String chronicleMapFilePath;

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.position.check.response.topic}")
    private String positionCheckResponseTopic;

    @Value("${in.silent.mode}")
    private boolean inSilentMode;

    public String getBrokerUrl()
    {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl)
    {
        this.brokerUrl = brokerUrl;
    }

    public String getPositionCheckResponseTopic()
    {
        return positionCheckResponseTopic;
    }

    public void setPositionCheckResponseTopic(String positionCheckResponseTopic)
    {
        this.positionCheckResponseTopic = positionCheckResponseTopic;
    }

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

    public String getStartOfDayInventoryPositionFilePath() { return startOfDayInventoryPositionFilePath; }

    public String getChronicleMapFilePath() { return chronicleMapFilePath; }

    public boolean inSilentMode()
    {
        return inSilentMode;
    }

    public void setSilentMode(boolean inSilentMode)
    {
        this.inSilentMode = inSilentMode;
    }

    public boolean isPrimary()
    {
        return isPrimary;
    }

    public void setPrimary(boolean primary)
    {
        isPrimary = primary;
    }
}
