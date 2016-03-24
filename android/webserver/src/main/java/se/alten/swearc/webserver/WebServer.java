package se.alten.swearc.webserver;

import java.util.function.Consumer;

public interface WebServer extends ServerFunction {

	public static WebServer create() {
		return new BasicWebServer();
	}

	public static void run(Consumer<ServerFunction> fn) {
		avoidIPv6();
		WebServer server = WebServer.create();
		server.start();

		fn.accept(server);

		server.stop();
	}

	public static void avoidIPv6() {
		java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
		java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
	}

	void start();

	void stop();
}
