package se.alten.swearc.webserver;

import java.util.function.Consumer;

public interface ServerFunction {
	void broadcastLogMessage(String message);

	void setCommands(String... commands);

	void onCommand(Consumer<String> onCmdHook);
}
