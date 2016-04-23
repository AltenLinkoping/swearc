package se.alten.swearc.webserver;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

public class FixedRespWebServer extends NanoHTTPD {

	private String content;

	public FixedRespWebServer(int port, String content) {
		super(port);
		this.content = content;
	}

	@Override
	public Response serve(IHTTPSession session) {
		return newFixedLengthResponse(content);
	}

	public static FixedRespWebServer startTryPorts(String content) {
		int defaultPort = 80;
		int backupPort = 1024;

		FixedRespWebServer ds = new FixedRespWebServer(defaultPort, content);

		while (!ds.isAlive()) {
			try {
				ds.start();
			} catch (IOException e) {
				ds.stop();
				ds = new FixedRespWebServer(backupPort++, content);
			}
		}
		return ds;
	}

}
