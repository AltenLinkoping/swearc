package se.alten.swearc.webserver;

import java.io.InputStream;
import java.util.Scanner;

public class ClientWebPage {
	private static final String ORIGINAL_WS_ADDR = "127.0.0.1:8889";
	private static final String RESOURCE_PATH = "/logClient.html";

	public static String toString(String websocketTargetAddress) {
		InputStream is = ClientWebPage.class.getResourceAsStream(RESOURCE_PATH);
		String content = resourceToString(is);
		return content.replace(ORIGINAL_WS_ADDR, websocketTargetAddress);
	}

	private static String resourceToString(InputStream is) {
		try (@SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A")) {
			return s.hasNext() ? s.next() : "";
		}
	}
}
