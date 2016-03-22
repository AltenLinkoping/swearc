package se.alten.swearc.logging;

public class BasicLogMessage implements LogMessage {

	private String message;

	public BasicLogMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		return (message == null) ? 0 : message.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicLogMessage other = (BasicLogMessage) obj;

		if (message == null) {
			return other.message == null;
		}
		return message.equals(other.message);
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "LogMessage(" + message + ")";
	}
}