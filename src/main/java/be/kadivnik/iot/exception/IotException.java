package be.kadivnik.iot.exception;

public class IotException extends Exception {

	private static final long serialVersionUID = -3942221252421785579L;

	public IotException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public IotException(String message) {
		super(message);
	}
}
