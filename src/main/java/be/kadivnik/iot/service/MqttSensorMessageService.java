package be.kadivnik.iot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import be.kadivnik.iot.enums.MessageTopic;
import be.kadivnik.iot.exception.MqttMessageException;

/**
 * @author johnny
 *
 * Handles the SensorState messages being sent to the MQTT broker.
 * The message should contain 4 parts
 * 
 * <pre>
 *  devicename|sensorname|sensortype|sensorvalue
 * </pre>
 *
 */
@Service
public class MqttSensorMessageService implements MqttMessageService {

	private final static Logger LOG = LoggerFactory.getLogger(MqttSensorMessageService.class);
	
//	@Autowired
//	private SensorStateService sensorStateService;
	
	@Override
	public void handleMessage(String message) throws MqttMessageException {
		LOG.info("Checking the message correctness");
		if (hasMessageCorrectNumberOfParts(message)) {
			LOG.debug("Sensorstate message has the correct number of parts");
			// TODO : create factory to handle the different messages
		}
	}

	@Override
	public String getTopicToHandle() {
		return MessageTopic.STATUS.getTopic();
	}

	/**
	 * The message should contain 4 parts divided by InternetOfThingsConstants.MESSAGE_SEPERATOR.
	 * 
	 * @param message
	 * @return true if the message is correct
	 */
	private boolean hasMessageCorrectNumberOfParts(String message) {
		boolean isMessageCorrect = Boolean.FALSE;

		if (message.split("|").length == 4) {
			isMessageCorrect = Boolean.TRUE;
		}
		
		return isMessageCorrect;
	}

}
