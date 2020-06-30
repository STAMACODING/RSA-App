package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;

/**
 * {@link Runnable} to receive new messages from other clients. Works using a socket server that listens on the specified {@link #port}.
 *
 */
public class ReceiveRunnable implements Runnable{
	/** Describes if the used device acts as server or as client. If this attribute is set to <i>true</i> incoming messages get forwarded. **/
	private final boolean server;
	
	/**
	 * Instantiates an object of the {@link ReceiveRunnable} class.
	 * @param port the used port to listen to new messages from other clients
	 * @param server describes if the used device acts as server or as client. If this attribute is set to <i>true</i> incoming messages get forwarded
	 */
	public ReceiveRunnable(boolean server) {
		this.server = server;
	}
	
	/**
	 * Runs the {@link ReceiveRunnable}.
	 * @see ReceiveRunnable
	 */
	public void run() {
		Logger.debug(ReceiveRunnable.class.getSimpleName(), "ReceiveThread is running");
		
		if(isServer()) {
			// Creates server socket listening on specified port (client should send message to this port)
			ServerSocket receiveServer = null;
			try {
				receiveServer = new ServerSocket(Server.RECEIVE_PORT);
				Logger.debug(ReceiveRunnable.class.getSimpleName(), "Successfully started the receive server");
			} catch (IOException e) {
				Logger.error(ReceiveRunnable.class.getSimpleName(), "Failed to start receive server");
			}
			while(true) {
				// Accept message from client and if this is a server forward it to another client
				try {
					Socket connectionFromClient = receiveServer.accept();
					Logger.debug(Server.class.getSimpleName(), "Receiving new message from a client");
					DataInputStream inputStream = new DataInputStream(connectionFromClient.getInputStream());
					
					// Check if message is not empty
					int length = inputStream.readInt();
					
				    // Read message from client
					byte[] messageIncludingMeta = null;
					if(length>0) {
						messageIncludingMeta = new byte[length];
						
					    inputStream.readFully(messageIncludingMeta, 0, messageIncludingMeta.length);
					    Logger.debug(Server.class.getSimpleName(), "Successfully received new message from a client");
					}
						
					// Forward message
					Logger.debug(Server.class.getSimpleName(), "Forwarding message");
					SendQueue.add(messageIncludingMeta);
				} catch (IOException e) {
					Logger.error(ReceiveRunnable.class.getSimpleName(), "Failed to receive message from a client");
				}
			}
		}else {
			Socket connectionToServer = null;
			try {
				connectionToServer = new Socket(Server.IP, Server.SEND_PORT);
				Logger.debug(ReceiveRunnable.class.getSimpleName(), "Successfully connected to the send server");
			} catch (IOException e) {
				Logger.error(ReceiveRunnable.class.getSimpleName(), "Failed to connect to the send server");
			}
			try {
				DataInputStream inputStream = new DataInputStream(connectionToServer.getInputStream());
				while(true) {
					Thread.sleep(10000);
					// Check if there is some input from server
					while(inputStream.readInt() == 0) {}
					// Read message from server
					byte[] messageIncludingMeta = null;
					messageIncludingMeta = new byte[inputStream.readInt()];
				    inputStream.readFully(messageIncludingMeta, 0, messageIncludingMeta.length);
				    
				    Logger.debug(Server.class.getSimpleName(), "Successfully received new message from the send server");
				}
			} catch (IOException | InterruptedException e) {
				Logger.error(ReceiveRunnable.class.getSimpleName(), "Failed to receive message from the send server");
			}
		}
	}

	/**
	 * Describes if the used device acts as server or as client. If this attribute is set to <i>true</i> incoming messages get forwarded.
	 * @return if the used device acts as server or as client
	 * @see #serer
	 */
	public boolean isServer() {
		return server;
	}
}
