package se.alten.swearc.logging;

import java.util.function.Consumer;

public class BasicLogger implements Logger {

	private Consumer<LogMessage> consumer;

	public BasicLogger(Consumer<LogMessage> consumer) {
		this.consumer = consumer;
	}

	@Override
	public void message(String text) {
		LogMessage logMessage = LogMessage.create(text);

		if (consumer != null)
			consumer.accept(logMessage);
	}
}
