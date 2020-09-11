package com.stamacoding.rsaApp.server.server.services;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.global.message.Message;
import com.stamacoding.rsaApp.server.global.message.data.LocalData;
import com.stamacoding.rsaApp.server.global.message.data.SendState;
import com.stamacoding.rsaApp.server.global.message.data.ServerData;
import com.stamacoding.rsaApp.server.global.service.Service;
import com.stamacoding.rsaApp.server.server.Server;
import com.stamacoding.rsaApp.server.server.ServerConfig;
import com.stamacoding.rsaApp.server.server.managers.ServerMessageManager;

/**
 *  {@link ServerSocketService} receiving messages from clients using a {@link ServerSocket}. After receiving a message
 *  the message gets forwarded using the {@link ServerMessageManager} and the {@link ServerSendService}.
 */
public class ServerReceiveService extends ServerSocketService{
	public static class AnswerCodes{
		public final static int RECEIVED_VALID_MESSAGE = 0;
		public final static int RECEIVED_INVALID_MESSAGE = -1;
		public final static int RECEIVED_INVALID_DATA = -2;
	}
	
	/** The only instance of this class */
	private volatile static ServerReceiveService singleton = new ServerReceiveService();

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

	// TODO SERVER ANSWER
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
	public void onAccept() {
		try {
			Message m = receiveMessage();
			
			if(m != null) {
				// 3. If the message has been received successfully decrypt its server data
				m.decryptServerData();
				
				// 4. Log the message
				Logger.debug(this.getClass().getSimpleName(), "Received message: " + m.toString());
				
				if(UserDatabaseService.getInstance().isUsernameAvailable(m.getServerData().getReceiving())
						|| UserDatabaseService.getInstance().isUsernameAvailable(m.getServerData().getSending())) {
					Logger.error(this.getServiceName(), "Message's sending and/or receiving user isn't signed up!");
					
					getOutputStream().writeInt(AnswerCodes.RECEIVED_INVALID_MESSAGE);
					return;
				}
				
				// 5. Add the message to the message manager to forward it
				ServerMessageManager.getInstance().manage(m);
				
				// 6. Answer client
				getOutputStream().writeInt(AnswerCodes.RECEIVED_VALID_MESSAGE);
			}else {
				// 3. -> If the server failed to receive the message
				Logger.error(this.getClass().getSimpleName(), new RuntimeException("Received invalid data (failed to receive message)"));
				getOutputStream().writeInt(AnswerCodes.RECEIVED_INVALID_DATA);
			}
		} catch (IOException e) {
			// 1. -> If the server failed to accept the client's connection or couldn't get the socket's input stream
			Logger.error(this.getClass().getSimpleName(), "Failed to receive a message from a client");
		}
	}
	
	/**
	 * Receive the message from the connected client
	 * @param inputStream the socket's input stream
	 * @return the received message (if there is any)
	 */
	private Message receiveMessage() {
		// 1. Read encrypted server data
		int serverDataLength;
		try {
			serverDataLength = getInputStream().readInt();
			byte[] encryptedServerData = null, encryptedProtectedData = null;
			if(serverDataLength>0) {
				encryptedServerData = new byte[serverDataLength];
			    getInputStream().readFully(encryptedServerData, 0, serverDataLength);
			    
			    
			    
			    // 2. Read encrypted protected data
			    int protectedDataLength = getInputStream().readInt();
			    if(protectedDataLength > 0) {
			    	encryptedProtectedData = new byte[protectedDataLength];
			    	getInputStream().readFully(encryptedProtectedData, 0, protectedDataLength);
			    }else {
			    	Logger.error(this.getClass().getSimpleName(), new RuntimeException("Received invalid data"));
			    }
			    
			    Logger.debug(this.getClass().getSimpleName(), "Successfully received message's meta and data");
				
			    // 3. Create message
				return new Message(new LocalData(-1, SendState.PENDING), encryptedProtectedData, encryptedServerData);
			}
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Unexspected error while receiving a message");
			e.printStackTrace();
		}
		return null;
	}
}
