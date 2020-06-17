package com.skanderj.gingerbread3.networking;

/**
 * @author Nim
 *
 * Useful enums for the networking package
 **/

enum NetworkingError {
    SUCCESS,
    INVALID_PORT_RANGE,
    IDENTIFIER_ALREADY_TAKEN,
    IDENTIFIER_DOESNT_EXIST,
    SERVER_IS_RUNNING,
    SERVER_NOT_RUNNING,
    SOCKET_CLOSE_ERROR,
    DEAD_CLIENT,
    GENERIC_SEND_ERROR;
}


enum PacketType {
    SENDINT, SENDDOUBLE, SENDRAW, SENDSTRINGLEN, SENDSTRING, DISCONNECT;
}
