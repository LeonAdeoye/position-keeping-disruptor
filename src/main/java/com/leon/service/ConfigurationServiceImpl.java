package com.leon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class ConfigurationServiceImpl
{
    @Autowired
    private ConnectionFactory connectionFactory;

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

    @Bean
    public JmsListenerContainerFactory<?> containerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(new JmsErrorHandler());
        return factory;
    }
}
