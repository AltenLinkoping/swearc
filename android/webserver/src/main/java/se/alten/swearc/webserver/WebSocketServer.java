package se.alten.swearc.webserver;

import java.util.function.Consumer;

public interface WebSocketServer extends ServerFunction {

	public static WebSocketServer create(String wsIP) {
		WebSocketServer wsServer;
		wsServer = new BasicWebSocketServer();
		wsServer = new WebClientServingSocketServer(wsServer, wsIP);
		return wsServer;
	}

	public static void run(String wsIP, Consumer<ServerFunction> fn) {
		avoidIPv6();
		WebSocketServer server = WebSocketServer.create(wsIP);
		server.start();

		fn.accept(server);

		server.stop();
	}

	public static void avoidIPv6() {
		java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
		java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
	}

	/**
	 * Start the web socket server.
	 *
	 * @return Port number of started server.
	 */
	int start();

	void stop();
}
