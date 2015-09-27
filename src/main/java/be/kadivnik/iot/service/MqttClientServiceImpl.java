package be.kadivnik.iot.service;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import be.kadivnik.iot.exception.IotException;

@Service
public class MqttClientServiceImpl implements MqttCallback {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(MqttClientServiceImpl.class);
	
	private MqttClient mqttClient;
	
	@Value("${mqtt.broker}")
	private String mqttBroker;
	@Value("${mqtt.clientid}")
	private String mqttClientId;
	
	private MemoryPersistence persistence = new MemoryPersistence();
	private int qos = 2;
	
	@PostConstruct
	public void connect() {
		try {
			MqttClient mqttClient = new MqttClient(mqttBroker, mqttClientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(Boolean.TRUE);
			LOGGER.debug("Connecting to broker {}", mqttBroker);
			mqttClient.setCallback(this);
			mqttClient.connect(connOpts);
			LOGGER.debug("Connected");
			subscribeToTopics();			
		} catch (MqttException mex) {
			LOGGER.error("MQTT exception when connecting to {}", mqttBroker, mex);
		}
	}

	private void subscribeToTopics() throws MqttException {
		LOGGER.debug("Subscribing to status topic");
		mqttClient.subscribe("status", 0);
	}
	
	public void publish(String topic, String content) throws IotException {
		try {
			LOGGER.debug("Publishing message {}", content);
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			mqttClient.publish(topic, message);
			LOGGER.debug("Message published");
		} catch (MqttException mex) {
			String errorMessage = MessageFormatter.format("Error while publishing : {}", mex.toString()).getMessage();
			LOGGER.error(errorMessage, mex);
			throw new IotException(errorMessage, mex);
		}
	}

	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
	}

	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
	}
}
