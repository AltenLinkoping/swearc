package se.alten.swearc.webserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

class LogWebsocketServer extends WebSocketServer {

	private static final ArrayList<WebSocket> EMPTY_LIST = new ArrayList<>();
	private Map<String, List<WebSocket>> resourceSockets;

	@Override
	public void stop(int timeout) throws IOException, InterruptedException {
		for (String resourceType : resourceSockets.keySet()) {
			for (WebSocket ws : resourceSockets.get(resourceType)) {
				ws.close(CloseFrame.GOING_AWAY, "Shutting down!");
			}
		}

		super.stop(timeout);
	}

	public LogWebsocketServer(int port, List<String> resources)
			throws UnknownHostException {
		super(new InetSocketAddress(port), Arrays.asList(new Draft_17()));
		initResources(resources);

		InetSocketAddress x = new InetSocketAddress("localhost", port);
		InetAddress address = x.getAddress();
		println("server running on '" + address + ":" + port + "'");
	}

	private void initResources(Iterable<String> resources) {
		resourceSockets = new HashMap<String, List<WebSocket>>();
		for (String resource : resources) {
			resourceSockets.put(resource, new ArrayList<WebSocket>());
		}
	}

	public void sendMessage(String resource, String message) {
		for (WebSocket socket : getSockets(resource)) {
			if (socket.isOpen())
				socket.send(message);
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {

		String resource = asResource(handshake);

		if (isValidResource(resource)) {
			resourceSockets.get(resource).add(conn);
			println("Adding new websocket connection from "
					+ conn.getRemoteSocketAddress().getHostString());
		} else {
			println("Socket trying to connect to unknown resource '" + resource
					+ "'.");
		}
	}

	private void println(String localMsg) {
		System.out.println(localMsg);
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

	private String asResource(ClientHandshake handshake) {
		String trim = handshake.getResourceDescriptor().trim();
		return trim.startsWith("/") ? trim.substring(1) : trim;
	}

	private List<WebSocket> getSockets(String resource) {
		if (!resourceSockets.containsKey(resource)) {
			System.out.println("Resource " + resource + " do not exist");
			return EMPTY_LIST;
		} else {
			List<WebSocket> sockets = resourceSockets.get(resource);
			sockets.removeIf(s -> !s.isOpen());
			return sockets;
		}
	}

	private boolean isValidResource(String resource) {
		return resourceSockets.containsKey(resource);
	}

}
