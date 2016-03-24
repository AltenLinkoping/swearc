package se.alten.swearc.webserver;

import java.io.IOException;
import java.net.UnknownHostException;

import se.alten.swearc.logging.LogMessage;

class BasicWebServer implements WebServer {

	private static final int PORT = 8889;

	LogWebsocketServer logger;

	{
		try {
			logger = new LogWebsocketServer(PORT);
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
		logger.sendMessageToAllConnections(LogMessage.create(message));
	}

	@Override
	public void stop() {
		try {
			logger.stop(100);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
