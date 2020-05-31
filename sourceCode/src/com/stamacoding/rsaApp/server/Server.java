package com.stamacoding.rsaApp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Contains server functions to receive data from clients and forward it to other clients.
 */
public class Server {
	
	/**
	 * This function enables the ability to receive and forward data. You only need to invoke this
	 * method to start the server.
	 */
	public static void receive() {
		// Start new thread to not freeze gui
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					// Creates server listening on port 455 (client should send data on this port)
					ServerSocket server = new ServerSocket(455);
					
					while(true) {
						// Accept data package from client (port 455)
						Socket receiver = server.accept();
						
						DataInputStream inputStream = new DataInputStream(receiver.getInputStream());
						
						// Check if data package is not empty
						int length = inputStream.readInt();
						
						if(length>0) {
							byte[] dataPackage = new byte[length];
						    // Read data package
						    inputStream.readFully(dataPackage, 0, dataPackage.length);
						    
						    // Get meta Information
							String targetIp = MetaUtils.getReceiving(dataPackage);
							
							// Forward data to specified client on port 456 (client should listen on this port)
							send(456, targetIp, dataPackage);
						}
					}
					// server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * This function sends data from the server to the client.
	 * @param port used port
	 * @param ip target's IP
	 * @param dataPackage the data to send
	 */
	private static void send(int port, String ip, byte[] dataPackage) {
		try {
			Socket sending = new Socket(ip, port);
			
			DataOutputStream outputStream = new DataOutputStream(sending.getOutputStream());
			
			// Set data length
			outputStream.writeInt(dataPackage.length);
			
			// Send data
			outputStream.write(dataPackage);
			
			// Close socket to enable new socket
			sending.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
