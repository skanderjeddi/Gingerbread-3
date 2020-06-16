package com.skanderj.gingerbread3.networking;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nim The Gingerbread Network Manager. Used to create, store and manage
 *         servers and clients.
 **/
public final class NetworkManager {
	private NetworkManager() {
		return;
	}

	private static final Map<String, SimpleServer> serversMap = new HashMap<String, SimpleServer>();

	public static void createSimpleServer(final String identifier, final int listenPort) {

	}
}
