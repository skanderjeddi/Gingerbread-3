package com.skanderj.gingerbread3.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.skanderj.gingerbread3.logging.Logger;
import com.skanderj.gingerbread3.logging.Logger.LogLevel;

/**
 * @author Nim
 *
 *         This simple TCP server allows the user to simply manage clients and
 *         perform simple network operations with said clients
 **/
public class SimpleServer {
	private int listenPort;
	private ServerSocket serverSocket;
	private Map<Integer, ClientObj> clients;
	private int maxClients;
	private int nextId = 0;
	private boolean isActive = true;

	/**
	 * @param listenPort The port that the server will open and use to wait for
	 *                   clients
	 **/
	protected SimpleServer(final int listenPort) {
		try {
			this.listenPort = listenPort;
			this.serverSocket = new ServerSocket(listenPort);
			this.clients = new HashMap<>();
		} catch (final Exception e) {
			Logger.log(SimpleServer.class, LogLevel.SEVERE, "Networking exception: %s", e.getMessage());
		}
	}

	/**
	 * Simple class to organize client attributes
	 **/
	private class ClientObj {
		public Socket socket;
		public boolean isAlive = true;

		private ClientObj(final Socket socket, final int id, final String ip) {
			this.socket = socket;
		}
	}

	/**
	 * This function is only used internally and doesn't concern the end user Crafts
	 * a packet from a header and data. The @return value is the crafted packet.
	 *
	 * @param type The type of packet, defined in PacketType
	 * @param s    The raw data
	 **/
	private byte[] craftPacket(final PacketType type, final byte[] data) {
		final byte[] header = new byte[] { (byte) type.ordinal() };
		final byte[] packet = new byte[header.length + data.length];
		System.arraycopy(header, 0, packet, 0, header.length);
		System.arraycopy(data, 0, packet, header.length, data.length);
		return packet;
	}

	/**
	 * Indefinetely waits for a client to connect, and adds them to the client map.
	 **/
	public int acceptClient() {
		try {
			final Socket newClientSock = this.serverSocket.accept();
			this.clients.put(this.nextId, new ClientObj(newClientSock, this.nextId, newClientSock.getInetAddress().getHostAddress()));
			this.nextId += 1;
			return this.nextId - 1;
		} catch (final Exception e) {
			Logger.log(SimpleServer.class, LogLevel.SEVERE, "Networking exception: %s", e.getMessage());
			return -1;
		}
	}

	/**
	 * @param id The client id of the receipient
	 * @param s  The string to send TODO: The whole thing lmao
	 **/
	public NetworkingError sendString(final int id, final String s) {
		final ClientObj thisClient = this.clients.get(id);
		if (!this.clients.get(id).isAlive) {
			Logger.log(SimpleServer.class, LogLevel.WARNING, "Trying to send data to a dead client");
			return NetworkingError.DEAD_CLIENT;
		}
		try {
			thisClient.socket.getOutputStream().write(this.craftPacket(PacketType.SENDSTRING, s.getBytes()));
			return NetworkingError.SUCCESS;
		} catch (final Exception e) {
			Logger.log(SimpleServer.class, LogLevel.SEVERE, "Networking exception: %s", e.getMessage());
			return NetworkingError.GENERIC_SEND_ERROR;
		}
	}

	protected NetworkingError stop() {
		for (final Map.Entry<Integer, SimpleServer.ClientObj> client : this.clients.entrySet()) {
			try {
				client.getValue().socket.close();
			} catch (final IOException e) {
				return NetworkingError.SOCKET_CLOSE_ERROR;
			}
			client.getValue().isAlive = false;
		}
		this.isActive = false;
		return NetworkingError.SUCCESS;
	}

	public int getListenPort() {
		return this.listenPort;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public Map<Integer, ClientObj> getClients() {
		return this.clients;
	}

	public Integer getMaxClients() {
		return this.maxClients;
	}
}
