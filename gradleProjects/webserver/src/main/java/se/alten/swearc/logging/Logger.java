package se.alten.swearc.logging;

import java.util.function.Consumer;

public interface Logger {

	void message(String text);

	static Logger create(Consumer<LogMessage> consumer) {
		return new BasicLogger(consumer);
	}
}
