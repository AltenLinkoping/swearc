package se.alten.swearc.webserver.dummy;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class DummyServer extends NanoHTTPD {

	public DummyServer(int port) {
		super(port);
		try {
			start();
			System.out.println("\nDummy web server is running!");
			System.out.println("Point your browers to http://localhost:" + port
					+ "/ \n");
		} catch (IOException e) {
			System.err.println("Couldn't start server:\n" + e);
			e.printStackTrace();
		}
	}

	@Override
	public Response serve(IHTTPSession session) {

		String msg = "<html><body><h1>Dummy server</h1>\n";
		Map<String, String> parms = session.getParms();

		if (parms.get("username") == null) {
			msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n"
					+ "</form>\n";
		} else {
			msg += "<p>Hello, " + parms.get("username") + "!</p>";
		}

		String uri = session.getUri();
		msg += "<p>uri is '" + uri + "'</p>";

		return newFixedLengthResponse(msg + "</body></html>\n");

	}
}
