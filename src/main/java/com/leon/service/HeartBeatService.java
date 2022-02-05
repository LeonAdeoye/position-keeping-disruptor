package com.leon.service;

public interface HeartBeatService
{
	void start(boolean isPrimary);
	void stop();
}
