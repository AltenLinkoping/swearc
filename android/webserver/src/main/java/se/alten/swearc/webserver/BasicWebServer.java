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

	LogWebsocketServer logger;

	{
		try {
			logger = new LogWebsocketServer(PORT, RESOURCES);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommand(Consumer<String> onCmdHook) {
		// TODO Auto-generated method stub
		
	}

}
