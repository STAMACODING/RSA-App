package server.New;

import java.io.*;
import java.net.*;

/*
 * this class creates everything needed to communicate, 
 * useful for clients and server
 * but creates no server socket!!!!
 */

public class NetworkingRunnable implements Runnable{

	
	Socket sock;
	PrintWriter writer;
	
	String SERVER_IP;
	int SERVER_PORT;
	
	public NetworkingRunnable(String ip, int port) {
		this.SERVER_IP = ip;
		this.SERVER_PORT = port;
	}
	
	public void run () {
		setUpNetworking(SERVER_IP, SERVER_PORT);
	}
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * 
	 * creates socket and printer
	 */
	public void setUpNetworking(String ip, int port) {
		
		try {
			sock = new Socket(ip, port);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("connection established");
			
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * 
	 * test connection by sending and receiving test message
	 */
	public void testConnection(String ip, int port) {
		
		String testMessage = "test";
		writer.println(testMessage);
		writer.close();
		
		System.out.println("connection succesfully tested");
		
	}
}

