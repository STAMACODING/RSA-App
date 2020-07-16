package com.stamacoding.rsaApp.server.client;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.NetworkUtils;
import com.stamacoding.rsaApp.server.TextUtils;
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

	/**
	 * Logs the client's configuration in a fancy way
	 */
	public static void log() {
		StringBuilder sb = new StringBuilder();
		sb.append("Log client configuration...\n\n");
		sb.append(TextUtils.heading("Client Configuration"));
		sb.append(TextUtils.box(""));
		
		// Client ID
		sb.append(TextUtils.fancyParameter("Client ID", String.valueOf(ClientConfig.ID)));
		
		// Server IP
		sb.append(TextUtils.fancyParameter("Server IP", ClientConfig.SERVER_IP));
		
		// Send Port
		sb.append(TextUtils.fancyParameter("Send Port", String.valueOf(ClientConfig.SEND_PORT)));
		
		// Receive Port
		sb.append(TextUtils.fancyParameter("Receive Port", String.valueOf(ClientConfig.RECEIVE_PORT)));
		
		// Query Interval
		sb.append(TextUtils.fancyParameter("Query interval", String.valueOf(ClientConfig.QUERY_MESSAGES_INTERVAL)));
		
		sb.append(TextUtils.box(""));
		
		if(isValid()) sb.append(TextUtils.heading("VALID"));
		else sb.append(TextUtils.heading("NOT VALID"));
		
		Logger.debug(ClientConfig.class.getSimpleName(), sb.toString());
	}
	
	public static void main(String[] args) {
		ClientConfig.log();
	}
}
