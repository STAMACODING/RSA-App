package com.stamacoding.rsaApp.network.client;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.service.MainService;
import com.stamacoding.rsaApp.network.client.service.message.ReceiveService;
import com.stamacoding.rsaApp.network.global.NetworkUtils;
import com.stamacoding.rsaApp.network.global.TextUtils;

/**
 * Configurations concerning the client
 */
public class Config {
	private static final String FILE_NAME = "client.config";
	
	/** The user's unique name */
	public static String USER_NAME = null;
	
	/** The user's password */
	public static char[] USER_PASSWORD = null;
	
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
	 * The time the {@link ReceiveService} waits before querying new messages from
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
	 * @param queryMessagesInterval The time the {@link ReceiveService} waits before querying new messages from the server
	 */
	public static void setup(String userName, char[] userPassword, String serverIP, int sendPort, int receivePort, int signupPort, int loginPort, int pingPort, long queryMessagesInterval, long retrySignUpInterval, long retryLoginInterval, long pingInterval, boolean registered) {
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
	 * if the configurations will lead to a correctly working {@link MainService}.
	 * @return if the configurations are valid
	 */
	public static boolean isValid() {	
		if(USER_PASSWORD == null || USER_PASSWORD.length == 0 || USER_PASSWORD.length > 30) return false;
		if(USER_NAME == null || USER_NAME.length() == 0 || USER_NAME.length() > 15) return false;
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
		
		if(Config.QUERY_MESSAGES_INTERVAL < 0) return false;
		if(Config.RETRY_LOGIN_INTERVAL < 0) return false;
		if(Config.RETRY_SIGNUP_INTERVAL < 0) return false;
		if(Config.PING_INTERVAL < 0) return false;
		
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
		sb.append(TextUtils.fancyParameter("User name", String.valueOf(Config.USER_NAME)));
		sb.append(TextUtils.fancyParameter("User password", USER_PASSWORD == null ? null : String.valueOf(Config.USER_PASSWORD)));
		
		// Server IP
		sb.append(TextUtils.fancyParameter("Server IP", Config.SERVER_IP));
		
		// Send Port
		sb.append(TextUtils.fancyParameter("Send Port", String.valueOf(Config.SEND_PORT)));
		
		// Receive Port
		sb.append(TextUtils.fancyParameter("Receive Port", String.valueOf(Config.RECEIVE_PORT)));
		
		// Sign-Up Port
		sb.append(TextUtils.fancyParameter("Sign-Up Port", String.valueOf(Config.SIGNUP_PORT)));
		
		// Login-In Port
		sb.append(TextUtils.fancyParameter("Log-In Port", String.valueOf(Config.LOGIN_PORT)));
		
		// Ping Port
		sb.append(TextUtils.fancyParameter("Ping Port", String.valueOf(Config.PING_PORT)));
		
		// Query Messages Interval
		sb.append(TextUtils.fancyParameter("Query Interval", String.valueOf(Config.QUERY_MESSAGES_INTERVAL)));
		
		// Retry Sign-Up Interval
		sb.append(TextUtils.fancyParameter("Sign-Up Interval", String.valueOf(Config.RETRY_SIGNUP_INTERVAL)));
		
		// Retry Log-In Interval
		sb.append(TextUtils.fancyParameter("Log-In Interval", String.valueOf(Config.RETRY_LOGIN_INTERVAL)));
		
		// Ping Interval
		sb.append(TextUtils.fancyParameter("Ping Interval", String.valueOf(Config.PING_INTERVAL)));
		
		sb.append(TextUtils.fancyParameter("Registered", String.valueOf(Config.REGISTERED).toUpperCase()));
		
		sb.append(TextUtils.box(""));
		
		if(isValid()) sb.append(TextUtils.heading("VALID"));
		else sb.append(TextUtils.heading("NOT VALID"));
		
		L.i(Config.class, sb.toString());
	}
	
	public static void save() {
		if(!isValid()) {
			L.w(Config.class, "Storing invalid configuration");
		}
		
	    Properties properties = new Properties();
	    properties.put("SERVER_IP", SERVER_IP == null ? "" : SERVER_IP);
	    properties.put("SEND_PORT", String.valueOf(SEND_PORT));
	    properties.put("RECEIVE_PORT", String.valueOf(RECEIVE_PORT));
	    properties.put("SIGNUP_PORT", String.valueOf(SIGNUP_PORT));
	    properties.put("LOGIN_PORT", String.valueOf(LOGIN_PORT));
	    properties.put("PING_PORT", String.valueOf(PING_PORT));
	    
	    properties.put("USER_NAME", USER_NAME == null ? "" : USER_NAME);
	    properties.put("USER_PASSWORD", USER_PASSWORD == null ? "" : String.valueOf(USER_PASSWORD));
	    
	    properties.put("QUERY_MESSAGES_INTERVAL", String.valueOf(QUERY_MESSAGES_INTERVAL));
	    properties.put("RETRY_SIGNUP_INTERVAL", String.valueOf(RETRY_SIGNUP_INTERVAL));
	    properties.put("RETRY_LOGIN_INTERVAL", String.valueOf(RETRY_LOGIN_INTERVAL));
	    properties.put("PING_INTERVAL", String.valueOf(PING_INTERVAL));
	    
	    properties.put("REGISTERED", String.valueOf(REGISTERED));

	    try {
			properties.storeToXML(new FileOutputStream(FILE_NAME), "Client Configurations");
			
		    L.i(Config.class, "Stored preferences");
		} catch (IOException e) {
			L.e(Config.class, "Failed to store preferences as .xml file", e);
		}
	}
	
	public static void read() {
		Properties properties = new Properties();
		try {
			properties.loadFromXML(new FileInputStream(FILE_NAME));
			
			Config.setup(
					properties.getProperty("USER_NAME").equals("") ? null : properties.getProperty("USER_NAME"), 
					properties.getProperty("USER_PASSWORD").equals("") ? null : properties.getProperty("USER_PASSWORD").toCharArray(), 
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
			L.i(Config.class, "Read preferences from .xml file");
		} catch (IOException e) {
			L.e(Config.class, "Failed to read preferences from .xml file", e);
			save();
		}
	}
	
	public static void main(String[] args) {
		Config.read();
		Config.log();
		Config.save();
	}


}
