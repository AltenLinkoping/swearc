package se.alten.swearc.webserver;

import java.util.function.Consumer;

import fi.iki.elonen.NanoHTTPD;

class WebClientServingSocketServer implements WebSocketServer {

	private WebSocketServer wsServer;
	private String wsIp;
	private NanoHTTPD webServer;

	WebClientServingSocketServer(WebSocketServer wsServer, String wsIp) {
		this.wsServer = wsServer;
		this.wsIp = wsIp;
		this.webServer = null;
	}

	@Override
	public void broadcastLogMessage(String message) {
		wsServer.broadcastLogMessage(message);

	}

	@Override
	public void setCommands(String... commands) {
		wsServer.setCommands(commands);
	}

	@Override
	public void onCommand(Consumer<String> onCmdHook) {
		wsServer.onCommand(onCmdHook);
	}

	@Override
	public int start() {
		int wsPort = wsServer.start();
		String wsAddress = wsIp + ':' + wsPort;
		String webResponse = ClientWebPage.toString(wsAddress);
		webServer = FixedRespWebServer.startTryPorts(webResponse);

		System.out.println();
		System.out.println("WebSocket are on port " + wsPort);
		System.out.println("Point your browers to http://" + wsIp + ":"
				+ webServer.getListeningPort() + "");

		System.out.println();

		return wsPort;
	}

	@Override
	public void stop() {
		wsServer.stop();
		webServer.stop();
	}
}
