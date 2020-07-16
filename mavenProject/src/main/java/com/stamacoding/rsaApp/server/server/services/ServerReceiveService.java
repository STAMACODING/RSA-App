package com.stamacoding.rsaApp.server.server.services;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.message.Message;
import com.stamacoding.rsaApp.server.message.data.LocalData;
import com.stamacoding.rsaApp.server.message.data.SendState;
import com.stamacoding.rsaApp.server.message.data.ServerData;
import com.stamacoding.rsaApp.server.server.Server;
import com.stamacoding.rsaApp.server.server.ServerConfig;
import com.stamacoding.rsaApp.server.server.managers.ServerMessageManager;

/**
 *  {@link ServerService} receiving messages from clients using a {@link ServerSocket}. After receiving a message
 *  the message gets forwarded using the {@link ServerMessageManager} and the {@link ServerSendService}.
 */
public class ServerReceiveService extends ServerService{
	
	/** The only instance of this class */
	private static volatile ServerReceiveService singleton = new ServerReceiveService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ServerReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 *  The server's port is set to {@link Server#RECEIVE_PORT}.
	 */
	private ServerReceiveService() {
		super(ServerReceiveService.class.getSimpleName(), ServerConfig.RECEIVE_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ServerReceiveService getInstance() {
		return singleton;
	}

	/**
	 * <ol>
	 * 	<li>If a clients wants to connect to the server using a {@link Socket}, the server will accept the connection.</li>
	 * 	<li>After that the server reads the message from the socket's {@link DataInputStream}.</li>
	 *  <li>The message's {@link ServerData} gets encrypted to find out the receiver's id.</li>
	 * 	<li>Then the message gets forwarded using the {@link ServerSendService} and the {@link ServerMessageManager}.</li>
	 * </ol>
	 * @see Service#onRepeat()
	 */
	@Override
	public void onRepeat() {
		// Accept message from client and if this is a server forward it to another client
		try {
			Socket connectionFromClient = getServerSocket().accept();
			connectionFromClient.setSoTimeout(5000);
			Logger.debug(this.getClass().getSimpleName(), "Receiving a new message from a client");
			DataInputStream inputStream = new DataInputStream(connectionFromClient.getInputStream());

			 // Read message from client
			int serverDataLength = inputStream.readInt();
			byte[] encryptedServerData = null, encryptedProtectedData = null;
			if(serverDataLength>0) {
				// Read message meta
				encryptedServerData = new byte[serverDataLength];
			    inputStream.readFully(encryptedServerData, 0, serverDataLength);
			    
			    
			    
			    // Read message data
			    int protectedDataLength = inputStream.readInt();
			    if(protectedDataLength > 0) {
			    	encryptedProtectedData = new byte[protectedDataLength];
			    	inputStream.readFully(encryptedProtectedData, 0, protectedDataLength);
			    }else {
			    	Logger.error(this.getClass().getSimpleName(), new RuntimeException("Received invalid data"));
			    }
			    
			    Logger.debug(this.getClass().getSimpleName(), "Successfully received message's meta and data");
				
				Message receivedMessage = new Message(new LocalData(-1, SendState.PENDING), encryptedProtectedData, encryptedServerData);
				receivedMessage.decryptServerData();
				
				Logger.debug(this.getClass().getSimpleName(), "Received message: " + receivedMessage.toString());
				ServerMessageManager.getInstance().manage(receivedMessage);
			}else {
				Logger.error(this.getClass().getSimpleName(), new RuntimeException("Received invalid data"));
			}

			connectionFromClient.close();
			Logger.debug(this.getClass().getSimpleName(), "Closed connection to client");
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to receive a message from a client");
		}
	}
}
