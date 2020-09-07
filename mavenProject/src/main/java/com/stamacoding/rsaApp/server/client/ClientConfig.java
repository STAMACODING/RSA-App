package com.stamacoding.rsaApp.server.client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.NetworkUtils;
import com.stamacoding.rsaApp.server.TextUtils;
import com.stamacoding.rsaApp.server.client.services.ClientMainService;
import com.stamacoding.rsaApp.server.client.services.ClientReceiveService;

/**
 * Configurations concerning the client
 */
public class ClientConfig {
	private static final String FILE_NAME = "client.config";
	
	/** The user's unique name */
	public static String USER_NAME = null;
	
	/** The user's password */
	public static String USER_PASSWORD = null;
	
	// TODO doc
	public static boolean REGISTERED = false;
	
	/**
	 * The client sends messages to this port.
	 */
	public static int SEND_PORT = -1;
	/**
	 * The client requests new messages from this port.
	 */
	public static int RECEIVE_PORT = -1;
	
	// TODO doc
	public static int SIGNUP_PORT = -1;
	public static int LOGIN_PORT = -1;
	public static int PING_PORT = -1;
	
	/**
	 * The time the {@link ClientReceiveService} waits before querying new messages from
	 * the server
	 */
	public static long QUERY_MESSAGES_INTERVAL = -1;
	
	//TODO doc
	public static long RETRY_SIGNUP_INTERVAL = -1;
	public static long RETRY_LOGIN_INTERVAL = -1;
	public static long PING_INTERVAL = -1;
	
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
	public static void setup(String userName, String userPassword, String serverIP, int sendPort, int receivePort, int signupPort, int loginPort, int pingPort, long queryMessagesInterval, long retrySignUpInterval, long retryLoginInterval, long pingInterval, boolean registered) {
		USER_NAME = userName;
		USER_PASSWORD = userPassword;
		SERVER_IP = serverIP;
		
		SEND_PORT = sendPort;
		RECEIVE_PORT = receivePort;
		SIGNUP_PORT = signupPort;
		LOGIN_PORT = loginPort;
		PING_PORT = pingPort;
		
		QUERY_MESSAGES_INTERVAL = queryMessagesInterval;
		RETRY_SIGNUP_INTERVAL = retrySignUpInterval;
		RETRY_LOGIN_INTERVAL = retryLoginInterval;
		PING_INTERVAL = pingInterval;
		
		REGISTERED = registered;
	}

	/**
	 * Checks if the configurations are valid. Attention! This function cannot verify
	 * if the configurations will lead to a correctly working {@link ClientMainService}.
	 * @return if the configurations are valid
	 */
	public static boolean isValid() {	
		if(USER_PASSWORD == null || USER_PASSWORD.length() == 0) return false;
		if(USER_NAME == null || USER_NAME.length() == 0) return false;
		if(!NetworkUtils.isValidInet4Address(SERVER_IP)) return false;
		
		if(SEND_PORT < 0) return false;
		if(RECEIVE_PORT < 0) return false;
		if(LOGIN_PORT < 0) return false;
		if(SIGNUP_PORT < 0) return false;
		if(PING_PORT < 0) return false;
		int[] ports = {SEND_PORT, RECEIVE_PORT, LOGIN_PORT, SIGNUP_PORT, PING_PORT};
		 for (int i = 0; i < ports.length; i++) {
		     for (int j = i + 1 ; j < ports.length; j++) {
		          if (ports[i] == ports[j]) {
		             return false;
		          }
		     }
		 }
		
		if(ClientConfig.QUERY_MESSAGES_INTERVAL < 0) return false;
		if(ClientConfig.RETRY_LOGIN_INTERVAL < 0) return false;
		if(ClientConfig.RETRY_SIGNUP_INTERVAL < 0) return false;
		if(ClientConfig.PING_INTERVAL < 0) return false;
		
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
		
		// Client Information
		sb.append(TextUtils.fancyParameter("User name", String.valueOf(ClientConfig.USER_NAME)));
		sb.append(TextUtils.fancyParameter("User password", String.valueOf(ClientConfig.USER_PASSWORD)));
		
		// Server IP
		sb.append(TextUtils.fancyParameter("Server IP", ClientConfig.SERVER_IP));
		
		// Send Port
		sb.append(TextUtils.fancyParameter("Send Port", String.valueOf(ClientConfig.SEND_PORT)));
		
		// Receive Port
		sb.append(TextUtils.fancyParameter("Receive Port", String.valueOf(ClientConfig.RECEIVE_PORT)));
		
		// Sign-Up Port
		sb.append(TextUtils.fancyParameter("Sign-Up Port", String.valueOf(ClientConfig.SIGNUP_PORT)));
		
		// Login-In Port
		sb.append(TextUtils.fancyParameter("Log-In Port", String.valueOf(ClientConfig.LOGIN_PORT)));
		
		// Ping Port
		sb.append(TextUtils.fancyParameter("Ping Port", String.valueOf(ClientConfig.PING_PORT)));
		
		// Query Messages Interval
		sb.append(TextUtils.fancyParameter("Query Interval", String.valueOf(ClientConfig.QUERY_MESSAGES_INTERVAL)));
		
		// Retry Sign-Up Interval
		sb.append(TextUtils.fancyParameter("Sign-Up Interval", String.valueOf(ClientConfig.RETRY_SIGNUP_INTERVAL)));
		
		// Retry Log-In Interval
		sb.append(TextUtils.fancyParameter("Log-In Interval", String.valueOf(ClientConfig.RETRY_LOGIN_INTERVAL)));
		
		// Ping Interval
		sb.append(TextUtils.fancyParameter("Ping Interval", String.valueOf(ClientConfig.PING_INTERVAL)));
		
		sb.append(TextUtils.fancyParameter("Registered", String.valueOf(ClientConfig.REGISTERED).toUpperCase()));
		
		sb.append(TextUtils.box(""));
		
		if(isValid()) sb.append(TextUtils.heading("VALID"));
		else sb.append(TextUtils.heading("NOT VALID"));
		
		Logger.debug(ClientConfig.class.getSimpleName(), sb.toString());
	}
	
	public static void save() {
		if(!isValid()) {
			Logger.warning(ClientConfig.class.getSimpleName(), "Storing invalid configuration");
		}
		
	    Properties properties = new Properties();
	    properties.put("SERVER_IP", SERVER_IP == null ? "" : SERVER_IP);
	    properties.put("SEND_PORT", String.valueOf(SEND_PORT));
	    properties.put("RECEIVE_PORT", String.valueOf(RECEIVE_PORT));
	    properties.put("SIGNUP_PORT", String.valueOf(SIGNUP_PORT));
	    properties.put("LOGIN_PORT", String.valueOf(LOGIN_PORT));
	    properties.put("PING_PORT", String.valueOf(PING_PORT));
	    
	    properties.put("USER_NAME", USER_NAME == null ? "" : USER_NAME);
	    properties.put("USER_PASSWORD", USER_PASSWORD == null ? "" : USER_PASSWORD);
	    
	    properties.put("QUERY_MESSAGES_INTERVAL", String.valueOf(QUERY_MESSAGES_INTERVAL));
	    properties.put("RETRY_SIGNUP_INTERVAL", String.valueOf(RETRY_SIGNUP_INTERVAL));
	    properties.put("RETRY_LOGIN_INTERVAL", String.valueOf(RETRY_LOGIN_INTERVAL));
	    properties.put("PING_INTERVAL", String.valueOf(PING_INTERVAL));
	    
	    properties.put("REGISTERED", String.valueOf(REGISTERED));

	    try {
			properties.storeToXML(new FileOutputStream(FILE_NAME), "Client Configurations");
			
		    Logger.debug(ClientConfig.class.getSimpleName(), "Stored preferences");
		} catch (IOException e) {
			Logger.error(ClientConfig.class.getSimpleName(), "Failed to store preferences as .xml file");
		}
	}
	
	public static void read() {
		Properties properties = new Properties();
		try {
			properties.loadFromXML(new FileInputStream(FILE_NAME));
			
			ClientConfig.setup(
					properties.getProperty("USER_NAME").equals("") ? null : properties.getProperty("USER_NAME"), 
					properties.getProperty("USER_PASSWORD").equals("") ? null : properties.getProperty("USER_PASSWORD"), 
					properties.getProperty("SERVER_IP").equals("") ? null : properties.getProperty("SERVER_IP"), 
					Integer.valueOf(properties.getProperty("SEND_PORT")), 
					Integer.valueOf(properties.getProperty("RECEIVE_PORT")), 
					Integer.valueOf(properties.getProperty("SIGNUP_PORT")),  
					Integer.valueOf(properties.getProperty("LOGIN_PORT")),   
					Integer.valueOf(properties.getProperty("PING_PORT")), 
					Long.valueOf(properties.getProperty("QUERY_MESSAGES_INTERVAL")),  
					Long.valueOf(properties.getProperty("RETRY_SIGNUP_INTERVAL")),  
					Long.valueOf(properties.getProperty("RETRY_LOGIN_INTERVAL")),  
					Long.valueOf(properties.getProperty("PING_INTERVAL")),
					Boolean.valueOf(properties.getProperty("REGISTERED"))
					);
			Logger.debug(ClientConfig.class.getSimpleName(), "Read preferences from .xml file");
		} catch (IOException e) {
			Logger.error(ClientConfig.class.getSimpleName(), "Failed to read preferences from .xml file");
			save();
		}
	}
	
	public static void main(String[] args) {
		ClientConfig.read();
		ClientConfig.log();
		ClientConfig.save();
	}


}
