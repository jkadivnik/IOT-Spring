package be.kadivnik.iot.service;

import be.kadivnik.iot.exception.MqttMessageException;

public interface MqttMessageService {

	void handleMessage(String message) throws MqttMessageException;
	String getTopicToHandle();
}
