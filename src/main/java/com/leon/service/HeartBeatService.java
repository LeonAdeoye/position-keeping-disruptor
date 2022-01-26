package com.leon.service;

import javax.jms.Message;

public interface HeartBeatService
{
	void ping();
	void receive(Message message);
	void start();
	void stop();
}
