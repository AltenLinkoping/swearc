package se.alten.swearc.logging;


public interface LogMessage {

	public String getMessage();

	static LogMessage create(String message) {
		return new BasicLogMessage(message);
	}
}
