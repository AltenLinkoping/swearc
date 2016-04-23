package se.alten.swearc.webserver.dummy;

import se.alten.swearc.webserver.ClientWebPage;
import se.alten.swearc.webserver.FixedRespWebServer;

public class DummyServer {

	public static void main(String[] args) {

		String ip = Util.getMyIP();

		String websocketTargetAddress = ip + ":1234";

		String content = ClientWebPage.toString(websocketTargetAddress);

		FixedRespWebServer webServer = FixedRespWebServer.startTryPorts(content);

		System.out.println("\nDummy web server is running!");

		Main.blockUntilEnterPress();

		webServer.stop();
	}

}
