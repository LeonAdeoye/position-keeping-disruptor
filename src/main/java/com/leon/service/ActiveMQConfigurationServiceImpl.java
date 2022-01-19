package com.leon.service;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import javax.jms.ConnectionFactory;

@Configuration
public class ActiveMQConfigurationServiceImpl
{
	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;
	@Value("${spring.activemq.position.check.response.topic}")
	private String positionCheckResponseTopic;

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

	@Bean
	public ConnectionFactory connectionFactory(){
		ActiveMQConnectionFactory activeMQConnectionFactory  = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		return  activeMQConnectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate(){
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory());
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}
}