package com.stamacoding.rsaApp.network.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.TextUtils;
import com.stamacoding.rsaApp.network.server.service.MainService;

/**
 * Configurations concerning the server
 */
public class Config {
	private static final String FILE_NAME = "server.config";
	/**
	 * On this port the server sends messages. The clients request messages from this port.
	 */
	public static int SEND_PORT = -1;
	/**
	 * On this port the server listens to receive messages from clients. The clients send messages to this port.
	 */
	public static int RECEIVE_PORT = -1;
	
	/**
	 * Using this port an user can sign-up.
	 */
	public static int SIGNUP_PORT = -1;
	
	/**
	 * Using this port an user can login.
	 */
	public static int LOGIN_PORT = -1;
	
	/**
	 * Using this port the activity of an user gets checked.
	 */
	public static int PING_PORT = -1;
	
	/**
	 * Sets the server's configurations.
	 * @param sendPort the server's send port
	 * @param receivePort the server's receive port
	 */
	public static void setup(int sendPort, int receivePort, int signupPort, int loginPort, int pingPort) {
		SEND_PORT = sendPort;
		RECEIVE_PORT = receivePort;
		SIGNUP_PORT = signupPort;
		LOGIN_PORT = loginPort;
		PING_PORT = pingPort;
	}

	/**
	 * Checks if the configurations are valid. Attention! This function cannot verify
	 * if the configurations will lead to a correctly working {@link MainService}.
	 * @return if the configurations are valid
	 */
	public static boolean isValid() {	
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
		return true;
	}
	
	/**
	 * Logs the server's configuration in a fancy way.
	 */
	public static void log() {
		StringBuilder sb = new StringBuilder();
		sb.append("Log server configuration...\n\n");
		sb.append(TextUtils.heading("Server Configuration"));
		sb.append(TextUtils.box(""));
		
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
		
		sb.append(TextUtils.box(""));
		
		if(isValid()) sb.append(TextUtils.heading("VALID"));
		else sb.append(TextUtils.heading("NOT VALID"));
		
		L.i(Config.class, sb.toString());
	}
	
	
	/**
	 * Save the server's configuration to a .xml-file.
	 */
	public static void save() {
		if(!isValid()) {
			L.w(Config.class, "Storing invalid configuration");
		}
		
	    Properties properties = new Properties();
	    properties.put("SEND_PORT", String.valueOf(SEND_PORT));
	    properties.put("RECEIVE_PORT", String.valueOf(RECEIVE_PORT));
	    properties.put("SIGNUP_PORT", String.valueOf(SIGNUP_PORT));
	    properties.put("LOGIN_PORT", String.valueOf(LOGIN_PORT));
	    properties.put("PING_PORT", String.valueOf(PING_PORT));

	    try {
			properties.storeToXML(new FileOutputStream(FILE_NAME), "Server Configurations");
			
		    L.i(Config.class, "Stored preferences");
		} catch (IOException e) {
			L.e(Config.class, "Failed to store preferences as .xml file", e);
		}
	}
	
	/**
	 * Read the server's configuration from the server config file.
	 */
	public static void read() {
		Properties properties = new Properties();
		try {
			properties.loadFromXML(new FileInputStream(FILE_NAME));
			
			Config.setup(
					Integer.valueOf(properties.getProperty("SEND_PORT")), 
					Integer.valueOf(properties.getProperty("RECEIVE_PORT")), 
					Integer.valueOf(properties.getProperty("SIGNUP_PORT")),  
					Integer.valueOf(properties.getProperty("LOGIN_PORT")),   
					Integer.valueOf(properties.getProperty("PING_PORT"))
					);
			L.i(Config.class, "Read preferences from .xml file");
		} catch (IOException e) {
			L.e(Config.class, "Failed to read preferences from .xml file", e);
			save();
		}
	}
	public static void main(String[] args) {
		Config.log();
	}
}
