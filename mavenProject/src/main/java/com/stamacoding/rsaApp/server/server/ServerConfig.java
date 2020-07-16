package com.stamacoding.rsaApp.server.server;

import com.stamacoding.rsaApp.server.server.services.ServerMainService;

/**
 * Configurations concerning the server
 */
public class ServerConfig {
	/**
	 * On this port the server sends messages. The clients request messages from this port.
	 */
	public static int SEND_PORT = -1;
	/**
	 * On this port the server listens to receive messages from clients. The clients send messages to this port.
	 */
	public static int RECEIVE_PORT = -1;
	
	/**
	 * Sets the server's configurations.
	 * @param sendPort the server's send port
	 * @param receivePort the server's receive port
	 */
	public static void setup(int sendPort, int receivePort) {
		ServerConfig.SEND_PORT = sendPort;
		ServerConfig.RECEIVE_PORT = receivePort;
	}

	/**
	 * Checks if the configurations are valid. Attention! This function cannot verify
	 * if the configurations will lead to a correctly working {@link ServerMainService}.
	 * @return if the configurations are valid
	 */
	public static boolean isValid() {	
		if(SEND_PORT < 0) return false;
		if(RECEIVE_PORT < 0) return false;
		if(RECEIVE_PORT == SEND_PORT) return false;
		
		return true;
	}
}
