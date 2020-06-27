package server.New;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerRunnable implements Runnable {

	String SERVER_IP;
	int SERVER_PORT;
	
	public ServerRunnable(String ip, int port) {
		this.SERVER_IP = ip;
		this.SERVER_PORT = port;
	}
	
	public void run() {
		createServer(SERVER_IP, SERVER_PORT);
		
	}
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * creates Server socket
	 */
	public void createServer(String ip, int port) {
		
		try {
		ServerSocket serverSock = new ServerSocket(port);
		System.out.println("server Sock on: " + port + " created");
		
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void threadHandler() {
		
	}
}
