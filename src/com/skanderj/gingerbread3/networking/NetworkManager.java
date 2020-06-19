package com.skanderj.gingerbread3.networking;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nim
 *
 *         The Gingerbread Network Manager. Used to create, store and manage
 *         servers and clients.
 **/
public final class NetworkManager {
	private NetworkManager() {
		return;
	}

	private static final Map<String, SimpleServer> serversMap = new HashMap<String, SimpleServer>();

	public static NetworkingError createSimpleServer(final String identifier, final int listenPort) {
		if (serversMap.containsKey(identifier))
			return NetworkingError.IDENTIFIER_ALREADY_TAKEN;
		if (1 > listenPort || 65535 < listenPort)
			return NetworkingError.INVALID_PORT_RANGE;
		SimpleServer newServer = new SimpleServer(listenPort);
		serversMap.put(identifier, newServer);
		return NetworkingError.SUCCESS;
	}

	/**
	 * Removes a server from the serversMap if it was previously stopped with
	 * stopServer
	 *
	 * @param type The type of packet, defined in PacketType
	 * @param s    The raw data
	 **/
	public static NetworkingError deleteServer(final String identifier) {
		if (!serversMap.containsKey(identifier))
			return NetworkingError.IDENTIFIER_DOESNT_EXIST;
		SimpleServer serverToDelete = serversMap.get(identifier);
		if (serverToDelete.isActive())
			return NetworkingError.SERVER_IS_RUNNING;
		serversMap.remove(identifier);
		return NetworkingError.SUCCESS;
	}

	/**
	 * Closes all open active connections with all clients of a server, and set it
	 * to "inactive" state, allowing to safely remove it from the serversMap.
	 *
	 * @param identifier The server identifier
	 **/
	public static NetworkingError stopSimpleServer(final String identifier) {
		if (!serversMap.containsKey(identifier))
			return NetworkingError.IDENTIFIER_DOESNT_EXIST;
		SimpleServer serverToStop = serversMap.get(identifier);
		if (!serverToStop.isActive())
			return NetworkingError.SERVER_NOT_RUNNING;
		return serverToStop.stop();
	}

	/**
	 * Returns the instance of the SimpleServer with provided identifier if it
	 * exists, or null if it doesn't.
	 *
	 * @param identifier The server identifier
	 **/
	public static SimpleServer getSimpleServer(final String identifier) {
		if (!serversMap.containsKey(identifier))
			return null;
		return serversMap.get(identifier);
	}
}
