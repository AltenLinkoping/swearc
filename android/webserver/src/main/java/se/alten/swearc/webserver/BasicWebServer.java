package se.alten.swearc.webserver;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

class BasicWebServer implements WebServer {

	private static final String COMMAND_RESOURCE = "command";
	private static final String LOG_RESOURCE = "log";

	private static final int PORT = 8889;

	private static final List<String> RESOURCES = Arrays.asList(LOG_RESOURCE,
			COMMAND_RESOURCE);

	private LogWebsocketServer logger;

	private Consumer<String> onCmdHook;
	private String jsonCommands = null;

	{
		try {
			logger = new LogWebsocketServer(PORT, RESOURCES);
			logger.setReceiveHook(this::onReceive);
			logger.setConnectHook(this::onConnectResponse);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		logger.start();
	}

	@Override
	public void broadcastLogMessage(String message) {
		logger.sendMessage(LOG_RESOURCE, message);
	}

	@Override
	public void stop() {
		try {
			logger.stop(200);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setCommands(String... commands) {
		this.jsonCommands = jsonify(commands);
		logger.sendMessage(COMMAND_RESOURCE, jsonCommands);
	}

	private String jsonify(String[] commands) {
		String result = "[";

		for (int i = 0; i < commands.length; i++) {
			char theEnd = i == commands.length - 1 ? ']' : ',';
			result += '\"' + commands[i] + '\"' + theEnd;
		}

		return result;
	}

	@Override
	public void onCommand(Consumer<String> onCmdHook) {
		this.onCmdHook = onCmdHook;

	}

	private void onReceive(String resource, String command) {
		if (onCmdHook != null)
			onCmdHook.accept(command);
	}

	private String onConnectResponse(String resource) {
		if (resource.equals(COMMAND_RESOURCE))
			return jsonCommands;
		else
			return null;
	}

}
