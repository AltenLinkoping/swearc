package se.alten.swearc.webserver.dummy;

import java.io.IOException;

import se.alten.swearc.webserver.ServerFunction;
import se.alten.swearc.webserver.WebServer;

public class Main {

	public static void main(String[] args) {

		WebServer.run(server -> {
			startLogging(server);
			blockUntilEnterPress();
		});

	}

	private static void startLogging(ServerFunction server) {
		final int logDelaySeconds = 2;
		RandomLogGenerator generator = new RandomLogGenerator(logDelaySeconds);
		generator.startLogging(server::broadcastLogMessage);
	}

	public static void blockUntilEnterPress() {
		System.out.println("Press enter to stop server.");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Bye!");
	}
}
