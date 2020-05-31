package com.stamacoding.rsaApp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;

/**
 * Contains server functions to receive data from clients and forward it to other clients.
 */
public class Server {
	
	/**
	 * This function enables the ability to receive and forward data. You only need to invoke this
	 * method to start the server.
	 */
	public static void receive() {
		Logger.debug(Server.class.getSimpleName(), "Starting server");
		// Start new thread to not freeze gui
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					// Creates server listening on port 455 (client should send data on this port)
					ServerSocket server = new ServerSocket(455);
					Logger.debug(Server.class.getSimpleName(), "Server started successfully");
					while(true) {
						// Accept data package from client (port 455)
						Socket receiver = server.accept();
						Logger.debug(Server.class.getSimpleName(), "Receiving new data package from a client");
						DataInputStream inputStream = new DataInputStream(receiver.getInputStream());
						
						// Check if data package is not empty
						int length = inputStream.readInt();
						
						if(length>0) {
							byte[] dataPackage = new byte[length];
						    // Read data package
						    inputStream.readFully(dataPackage, 0, dataPackage.length);
						    Logger.debug(Server.class.getSimpleName(), "Received new data package from a client");
						    
						    // Get meta Information
							String targetIp = MetaUtils.getReceiving(dataPackage);
							
							// Forward data to specified client on port 456 (client should listen on this port)
							Logger.debug(Server.class.getSimpleName(), "Trying to send data package to " + targetIp);
							send(456, targetIp, dataPackage);
						}
					}
					// server.close();
				} catch (IOException e) {
					Logger.error(Server.class.getSimpleName(), "Server couldn't start");
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
			
			Logger.debug(Server.class.getSimpleName(), "Sending data package to " + ip);
			// Set data length
			outputStream.writeInt(dataPackage.length);
			// Send data
			outputStream.write(dataPackage);
			Logger.debug(Server.class.getSimpleName(), "Sent data package successfully to " + ip);
			// Close socket to enable new socket
			sending.close();
		} catch (IOException e) {
			Logger.error(Server.class.getSimpleName(), "Failed to send data package to " + ip);
			e.printStackTrace();
		}
	}
}
