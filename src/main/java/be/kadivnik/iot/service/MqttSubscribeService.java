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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import be.kadivnik.iot.exception.IotException;

@Service
public class MqttSubscribeService implements MqttCallback  {

	private final static Logger LOG = LoggerFactory.getLogger(MqttSubscribeService.class);
	
	@Value("${mqtt.broker}")
	private String mqttBroker;
	@Value("${mqtt.clientid}")
	private String mqttClientId;

	private MemoryPersistence persistence = new MemoryPersistence();
	private int qos = 2;

	@Autowired
	private MqttMessageFactory mqttMessageFactory;

	private MqttClient mqttClient;
	
	@PostConstruct
	public void connect() {
		try {
			mqttClient = new MqttClient(mqttBroker, mqttClientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(Boolean.TRUE);
			LOG.debug("Connecting to broker {}", mqttBroker);
			mqttClient.setCallback(this);
			mqttClient.connect(connOpts);
			LOG.debug("Connected");
			subscribeToTopics();			
		} catch (MqttException mex) {
			LOG.error("MQTT exception when connecting to {}", mqttBroker, mex);
		}
	}
	
	private void subscribeToTopics() throws MqttException {
		LOG.debug("Subscribing to status topic");
		mqttClient.subscribe("status", 0);
	}

	public void publishMessage(String topic, String content) throws IotException {
		try {
			LOG.debug("Publishing message {}", content);
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			mqttClient.publish(topic, message);
			LOG.debug("Message published");
		} catch (MqttException mex) {
			String errorMessage = MessageFormatter.format("Error while publishing : {}", mex.toString()).getMessage();
			LOG.error(errorMessage, mex);
			throw new IotException(errorMessage, mex);
		}
	}
	
	@Override
	public void connectionLost(Throwable throwable) {
		LOG.error("Connection lost to the mqtt server : " + throwable.getStackTrace());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		LOG.error("A message has been published!");
	}

	@Override
	public void messageArrived(String topic, MqttMessage content) throws Exception {
		LOG.error("A new message has arrived!");
		MqttMessageService mqttMessageService = mqttMessageFactory.getMessageService(topic);
		mqttMessageService.handleMessage(content.getPayload().toString());
	}

}
