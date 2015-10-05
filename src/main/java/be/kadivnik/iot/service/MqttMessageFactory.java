package be.kadivnik.iot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kadivnik.iot.exception.MqttMessageException;

@Service
public class MqttMessageFactory {

	private final static Logger LOG = LoggerFactory.getLogger(MqttMessageFactory.class);
	
	@Autowired
	private MqttMessageService mqttSensorStateMessageService;

	public MqttMessageService getMessageService(String topic) throws MqttMessageException {
		if (topic.equalsIgnoreCase(mqttSensorStateMessageService.getTopicToHandle())) {
			return mqttSensorStateMessageService;
		} else {
			String message = topic + " is not a topic I can handle!";
			LOG.error(message);
			throw new MqttMessageException(message);
		}
	}
	
}
