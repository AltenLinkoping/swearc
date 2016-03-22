package se.alten.swearc.webserver.dummy;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import se.alten.swearc.logging.Logger;
import se.alten.swearc.webserver.LogWebsocketServer;

public class Main {

	private static final int PORT = 8889;
	private static final int LOG_TEXT_DELAY_SECONDS = 2;

	public static void main(String[] args) {
		avoidIPv6();
		try {
			RandomLogGenerator logGenerator = new RandomLogGenerator(
					LOG_TEXT_DELAY_SECONDS);
			LogWebsocketServer lws;
			lws = new LogWebsocketServer(PORT);
			lws.start();
			Logger logger = Logger.create(lws::sendMessageToAllConnections);
			logGenerator.startLogging(logger);

			blockUntilEnterPress();
			lws.stop(100);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void blockUntilEnterPress() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Press enter to stop server.");
		scanner.nextLine();
		scanner.close();
		System.out.println("Bye!");
	}

	public static void avoidIPv6() {
		// if ("google_sdk".equals( Build.PRODUCT )) {
		java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
		java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
		// }
	}
}
