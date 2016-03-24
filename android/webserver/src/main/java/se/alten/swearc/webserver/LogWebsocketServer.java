package se.alten.swearc.webserver;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import se.alten.swearc.logging.LogMessage;

class LogWebsocketServer extends WebSocketServer {

	Collection<WebSocket> loggerSockets = new LinkedList<WebSocket>();

	public LogWebsocketServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port), Collections
				.singletonList(new Draft_17()));

		InetSocketAddress x = new InetSocketAddress("localhost", port);
		System.out.println("server up and running on address '"
				+ x.getAddress() + ":" + x.getPort() + "'");
	}

	public void sendMessageToAllConnections(LogMessage message) {

		loggerSockets.removeIf(s -> !s.isOpen());

		for (WebSocket socket : loggerSockets) {
			if (socket.isOpen())
				socket.send(message.getMessage());
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("Adding new websocket connection from "
				+ conn.getRemoteSocketAddress().getHostString());

		loggerSockets.add(conn);

	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("close: code=" + code + ", reason=" + reason
				+ ", remote=" + remote);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("message: " + message);
		// conn.send(conn.getLocalSocketAddress().toString());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.out.println("error: " + ex);
	}

}
