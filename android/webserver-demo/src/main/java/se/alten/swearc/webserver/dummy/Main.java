package se.alten.swearc.webserver.dummy;

import java.io.IOException;

import se.alten.swearc.webserver.ServerFunction;
import se.alten.swearc.webserver.WebSocketServer;

public class Main {

	private static RandomLogGenerator generator;

	public static void main(String[] args) {

		String IP = Util.getMyIP();

		WebSocketServer.run(IP, server -> {

			startRandomLogging(server);

			setControlCommands(server);
			logReceivedCommands(server);

			blockUntilEnterPress();

			stopRandomLogging();
		});

		System.out.println("Bye!");
	}

	private static void stopRandomLogging() {
		generator.stop();
	}

	private static void startRandomLogging(ServerFunction server) {
		final int logDelaySeconds = 2;
		generator = new RandomLogGenerator(logDelaySeconds);
		generator.startLogging(server::broadcastLogMessage);
	}

	private static void setControlCommands(ServerFunction server) {
		server.setCommands("LEFT", "FWD", "BACK", "RIGHT", "nuts");
	}

	private static void logReceivedCommands(final ServerFunction server) {
		server.onCommand(command -> server
				.broadcastLogMessage("Robot is going " + command));
	}

	public static void blockUntilEnterPress() {
		System.out.println("Press enter to stop server.\n");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
