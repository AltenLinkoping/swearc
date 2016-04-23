package se.alten.swearc.webserver.dummy;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Util {

	public static String getMyIP() {

		try {
			Enumeration<NetworkInterface> nifs = NetworkInterface
					.getNetworkInterfaces();

			while (nifs.hasMoreElements()) {
				NetworkInterface nif = nifs.nextElement();

				if (nif.isLoopback() || !nif.isUp())
					continue;

				Enumeration<InetAddress> addresses = nif.getInetAddresses();
				while (addresses.hasMoreElements()) {
					String address = addresses.nextElement().getHostAddress();
					if (address.contains("."))
						return address;
				}
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "<no ip>";
	}

}
