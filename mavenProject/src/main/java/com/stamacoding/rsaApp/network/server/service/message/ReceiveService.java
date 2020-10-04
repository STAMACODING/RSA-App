package com.stamacoding.rsaApp.network.server.service.message;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.global.message.data.LocalData;
import com.stamacoding.rsaApp.network.global.message.data.SendState;
import com.stamacoding.rsaApp.network.global.message.data.ServerData;
import com.stamacoding.rsaApp.network.global.service.ServerService;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.server.Config;
import com.stamacoding.rsaApp.network.server.Server;
import com.stamacoding.rsaApp.network.server.manager.MessageManager;
import com.stamacoding.rsaApp.network.server.service.user.UserDatabaseService;

/**
 *  {@link ServerService} receiving messages from clients using a {@link ServerSocket}. After receiving a message
 *  the message gets forwarded using the {@link MessageManager} and the {@link SendService}.
 */
public class ReceiveService extends ServerService{
	public static class AnswerCodes{
		public final static int RECEIVED_VALID_MESSAGE = 0;
		public final static int RECEIVED_INVALID_MESSAGE = -1;
		public final static int RECEIVED_INVALID_DATA = -2;
	}
	
	/** The only instance of this class */
	private volatile static ReceiveService singleton = new ReceiveService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 *  The server's port is set to {@link Server#RECEIVE_PORT}.
	 */
	private ReceiveService() {
		super(Config.RECEIVE_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ReceiveService getInstance() {
		return singleton;
	}

	// TODO SERVER ANSWER
	/**
	 * <ol>
	 * 	<li>If a clients wants to connect to the server using a {@link Socket}, the server will accept the connection.</li>
	 * 	<li>After that the server reads the message from the socket's {@link DataInputStream}.</li>
	 *  <li>The message's {@link ServerData} gets encrypted to find out the receiver's id.</li>
	 * 	<li>Then the message gets forwarded using the {@link SendService} and the {@link MessageManager}.</li>
	 * </ol>
	 * @see Service#onRepeat()
	 */
	@Override
	public void onAccept() {
		try {
			Message m = receiveMessage();
			
			if(m != null) {
				L.t(getClass(), "Decrypting message's server data...");
				m.decryptServerData();
				L.t(getClass(), "Decrypted message's server data!");
				
				// 4. Log the message
				L.i(this.getClass(), "Received message: " + m.toString());
				
				boolean userNamesAvailable = (boolean) UserDatabaseService.getInstance().executeAndWait(new Callable<Object>() {
					
					@Override
					public Boolean call() throws Exception {
						return UserDatabaseService.getInstance().isUsernameAvailable(m.getServerData().getReceiving()) ||
							UserDatabaseService.getInstance().isUsernameAvailable(m.getServerData().getSending());
					}
				});
				
				if(userNamesAvailable) {
					L.e(this.getClass(), "Message's sending and/or receiving user isn't signed up!");
					
					L.t(getClass(), "Sending error code to client...");
					getOutputStream().writeInt(AnswerCodes.RECEIVED_INVALID_MESSAGE);
					L.t(getClass(), "Sent error code to client!");
					return;
				}
				
				L.t(this.getClass(), "Adding message to the message manager to forward it...");
				// 5. Add the message to the message manager to forward it
				// TODO FORWARD USING SEND SERVICE
				MessageManager.getInstance().manage(m);
				
				// 6. Answer client
				L.t(getClass(), "Sending success code to client...");
				getOutputStream().writeInt(AnswerCodes.RECEIVED_VALID_MESSAGE);
				L.t(getClass(), "Sent success code to client!");
			}else {
				// 3. -> If the server failed to receive the message
				L.e(this.getClass(), "Received invalid data (failed to receive message), sending error code to client...");
				getOutputStream().writeInt(AnswerCodes.RECEIVED_INVALID_DATA);
				L.t(getClass(), "Sent error code to client!");
			}
		} catch (IOException e) {
			// 1. -> If the server failed to accept the client's connection or couldn't get the socket's input stream
			L.e(this.getClass(), "Failed to receive a message from a client", e);
		}
	}
	
	/**
	 * Receive the message from the connected client
	 * @param inputStream the socket's input stream
	 * @return the received message (if there is any)
	 */
	private Message receiveMessage() {
		L.t(getClass(), "Reading encrypted server data...");
		int serverDataLength;
		try {
			serverDataLength = getInputStream().readInt();
			byte[] encryptedServerData = null, encryptedProtectedData = null;
			if(serverDataLength>0) {
				encryptedServerData = new byte[serverDataLength];
			    getInputStream().readFully(encryptedServerData, 0, serverDataLength);
			    L.d(getClass(), "Read encrypted server data successfully!");
			    
			    
			    L.t(getClass(), "Reading encrypted protected data...");
			    int protectedDataLength = getInputStream().readInt();
			    if(protectedDataLength > 0) {
			    	encryptedProtectedData = new byte[protectedDataLength];
			    	getInputStream().readFully(encryptedProtectedData, 0, protectedDataLength);
				    L.d(getClass(), "Read encrypted protected data successfully!");
			    }else {
			    	L.e(this.getClass(), "Received invalid data");
			    	return null;
			    }
			    L.d(this.getClass(), "Successfully received message's meta and server data");
				
			    // 3. Create message
				return new Message(new LocalData(-1, SendState.PENDING), encryptedProtectedData, encryptedServerData);
			}
		} catch (IOException e) {
			L.e(this.getClass(), "Unexspected error while receiving a message", e);
		}
		return null;
	}
}
