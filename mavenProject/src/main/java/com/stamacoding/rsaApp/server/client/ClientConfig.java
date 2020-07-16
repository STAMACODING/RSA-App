package com.stamacoding.rsaApp.server.client;

import com.stamacoding.rsaApp.server.NetworkUtils;
import com.stamacoding.rsaApp.server.client.services.ClientMainService;
import com.stamacoding.rsaApp.server.client.services.ClientReceiveService;

/**
 * Configurations concerning the client
 */
public class ClientConfig {
	
	/** The client's unique id */
	public static byte ID = -1;
	
	/**
	 * The time the {@link ClientReceiveService} waits before querying new messages from
	 * the server
	 */
	public static long QUERY_MESSAGES_INTERVAL = -1;
	
	/**
	 * The client sends messages to this port.
	 */
	public static int SEND_PORT = -1;
	/**
	 * The client requests new messages from this port.
	 */
	public static int RECEIVE_PORT = -1;
	
	/** The server's unique public ip-address */
	public static String SERVER_IP = null;
	
	/**
	 * Sets the client's configurations.
	 * @param ID the client's id
	 * @param serverIP the server's ip
	 * @param sendPort the client's send port
	 * @param receivePort the client's receive port
	 * @param queryMessagesInterval The time the {@link ClientReceiveService} waits before querying new messages from the server
	 */
	public static void setup(byte ID, String serverIP, int sendPort, int receivePort, long queryMessagesInterval) {
		ClientConfig.ID = ID;
		ClientConfig.SERVER_IP = serverIP;
		ClientConfig.SEND_PORT = sendPort;
		ClientConfig.RECEIVE_PORT = receivePort;
		ClientConfig.QUERY_MESSAGES_INTERVAL = queryMessagesInterval;
	}

	/**
	 * Checks if the configurations are valid. Attention! This function cannot verify
	 * if the configurations will lead to a correctly working {@link ClientMainService}.
	 * @return if the configurations are valid
	 */
	public static boolean isValid() {	
		if(ID < 0) return false;
		if(!NetworkUtils.isValidInet4Address(SERVER_IP)) return false;
		
		if(SEND_PORT < 0) return false;
		if(RECEIVE_PORT < 0) return false;
		if(RECEIVE_PORT == SEND_PORT) return false;
		
		return true;
	}
}
