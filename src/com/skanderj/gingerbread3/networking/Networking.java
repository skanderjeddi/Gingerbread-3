package com.skanderj.gingerbread3.networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import com.skanderj.gingerbread3.log.Logger;
import com.skanderj.gingerbread3.log.LogLevel;


/**
*   @author Nim
*   This simple TCP server allows the user to simply manage clients and perform simple network
*   operations with said clients
**/
class SimpleServer {
    private int listenPort;
    private ServerSocket serverSocket;
    private Map<Integer, ClientObj> clients;
    private Integer maxClients;
    private int nextId = 0;

    /**
    *   @param listenPort   The port that the server will open and use to wait for clients
    **/
    public SimpleServer(int listenPort) {
        try{
            this.listenPort = listenPort;
            this.serverSocket = new ServerSocket(listenPort);
        } catch (Exception e) {
            Logger.log(SimpleServer.class, LogLevel.ERROR, e.toString());
        }
    }


    /**
    *   Simple class to organize client attributes
    **/
    private class ClientObj {
        Socket socket;
        boolean alive = true;
        int id;
        String ip;

        public ClientObj(String socket, int id, String ip) {
            this.socket = socket;
            this.id = id;
            this.ip = ip;
        }
    }

    /**
    *   The different types of packets
    **/
    enum PacketType {
        SENDINT,
        SENDDOUBLE,
        SENDRAW,
        SENDSTRINGLEN,
        SENDSTRING,
        DISCONNECT;
    }

    /**
    *   This function is only used internally and doesn't concern the end user
    *   Crafts a packet from a header and data. The @return value is the crafted packet.
    *   @param type     The type of packet, defined in PacketType
    *   @param s        The raw data
    **/
    private byte[] craftPacket(PacketType type, byte[] data) {
        byte[] header = new byte[]{(byte)type.ordinal()};
        byte[] packet = new byte[header.length + data.length];
        System.arraycopy(header, 0, packet, 0, header.length);
        System.arraycopy(data, 0, packet, header.length, data.length);
        return packet;
    }

    /**
    *   Indefinetely waits for a client to connect, and adds them to the client map.
    **/
    public int acceptClient() {
        try {
            Socket newClientSock = this.serverSocket.accept();
            this.clients.put(this.nextId, new this.ClientObj(newClientSock, this.nextId, newClientSock.getInetAddress().getHostAddress()));
            this.nextId += 1;
            return this.nextId-1;
        } catch(Exception e) {
            Logger.log(SimpleServer.class, LogLevel.ERROR, e.toString());
        }
    }

    /**
    *   @param id       The client id of the receipient
    *   @param s        The string to send
    *   TODO: The whole thing lmao
    **/
    public boolean sendString(int id, String s){

        if (!this.clients.get(id).alive) {
            Logger.log(SimpleServer.class, LogLevel.IGNORE_UNLESS_REPEATED, "Trying to send data to a dead client");
            return false;
        }
        return true;
    }




	public int getListenPort() {
		return this.listenPort;
	}

	public Map<Integer, ClientObj> getClients() {
		return this.clients;
	}

	public Integer getMaxClients() {
		return this.maxClients;
	}
}
