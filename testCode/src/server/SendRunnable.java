package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;


/**
 * {@link Runnable} to send messages to other clients. Works using the {@link SendQueue} and sockets.
 *
 */
public class SendRunnable implements Runnable{
	/** Describes if the used device acts as server or as client. If this attribute is set to <i>true</i> incoming messages get forwarded. **/
	private final boolean server;
	
	
	/**
	 * Instantiates an object of the {@link SendRunnable} class.
	 * @param port the used port to send messages
	 */
	public SendRunnable(boolean server) {
		this.server = server;
	}

	/**
	 * Runs the {@link SendRunnable}.
	 * @see SendRunnable
	 */
	public void run() {
		Logger.debug(SendRunnable.class.getSimpleName(), "SendThread is running");
		
		if(isServer()) {
			ServerSocket sendServer = null;
			try {
				sendServer = new ServerSocket(Server.SEND_PORT);
				Logger.debug(SendRunnable.class.getSimpleName(), "Successfully started send server");
			} catch (IOException e) {
				e.printStackTrace();
				Logger.error(SendRunnable.class.getSimpleName(), "Failed to start send server");
			}
			while(true) {
				try {
					Socket connectionFromClient = sendServer.accept();
					Logger.debug(SendRunnable.class.getSimpleName(), "Received client request");
					if(!SendQueue.isEmpty()) {
						String receivingIp = connectionFromClient.getLocalAddress().getHostAddress();
						ArrayList<byte[]> messagesToSend = SendQueue.getMessages(receivingIp);
						Logger.debug(SendRunnable.class.getSimpleName(), "Searching for messages that belong to the client (" + receivingIp + ")");
						DataOutputStream outputStream = new DataOutputStream(connectionFromClient.getOutputStream());
						
						// Send one message to the receiver TODO send multiple messages to the receiver
						if(messagesToSend.size() > 0) {
							outputStream.writeInt(messagesToSend.get(0).length);
							outputStream.write(messagesToSend.get(0));
							
							Logger.debug(SendRunnable.class.getSimpleName(), "Successfully sent message to a client");
						}{
							Logger.debug(SendRunnable.class.getSimpleName(), "Found no message!");
						}

					}
					connectionFromClient.close();
				} catch (IOException e) {
					e.printStackTrace();
					Logger.error(SendRunnable.class.getSimpleName(), "Failed to send message to a client");
				}
			}
		}else {
			while(true) {
				// If there is a message to be sent
				if(!SendQueue.isEmpty()) {
					Logger.debug(SendRunnable.class.getSimpleName(), "SendQueue is not empty");
					byte[] message = SendQueue.poll();
						
					Socket connectionToServer = null;
					try {
						connectionToServer = new Socket(Server.IP, Server.RECEIVE_PORT);
						Logger.debug(SendRunnable.class.getSimpleName(), "Successfully conntected to the receive server");
					} catch (IOException e) {
						e.printStackTrace();
						Logger.error(SendRunnable.class.getSimpleName(), "Failed to connect to receive server");
					}
					try {
						DataOutputStream outputStream = new DataOutputStream(connectionToServer.getOutputStream());
						
						// Send message to the receiver
						outputStream.writeInt(message.length);
						outputStream.write(message);
							
						Logger.debug(SendRunnable.class.getSimpleName(), "Successfully sent message to the receive server");
						
						// Close connection to the receiver
						connectionToServer.close();
					} catch (IOException e) {
						e.printStackTrace();
						Logger.error(SendRunnable.class.getSimpleName(), "Failed to send message to the receive server");
					}

				}
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
