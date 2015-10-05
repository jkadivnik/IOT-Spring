package be.kadivnik.iot.enums;

public enum MessageTopic {

	STATUS ("status"),
	VALUE ("value"),
	KEEPALIVE ("keepalive");
	
	private String topic;
	
	private MessageTopic(String topic) {
		this.topic = topic;
	}
	
	public String getTopic() {
		return topic;
	}
}
