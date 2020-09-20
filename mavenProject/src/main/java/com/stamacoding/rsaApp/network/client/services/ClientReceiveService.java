package com.stamacoding.rsaApp.network.client.services;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.network.client.Client;
import com.stamacoding.rsaApp.network.client.ClientConfig;
import com.stamacoding.rsaApp.network.client.managers.ClientMessageManager;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.global.message.data.SendState;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.server.Server;
import com.stamacoding.rsaApp.security.Security;

/**
 * {@link Service} receiving messages from the server and forwarding them to the {@link ClientMessageManager}.
 */
public class ClientReceiveService extends ClientSocketService{
	
	/** The only instance of this class */
	private volatile static ClientReceiveService singleton = new ClientReceiveService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ClientReceiveService() {
		super(ClientReceiveService.class.getSimpleName(), ClientConfig.SERVER_IP, ClientConfig.RECEIVE_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ClientReceiveService getInstance() {
		return singleton;
	}

	/**
	 * Creates a {@link Socket} connection to query new messages from the server in an interval of {@link Client#QUERY_MESSAGES_INTERVAL} milliseconds.
	 * <ol>
	 * 	<li>Connects to the service using a {@link Socket} at port {@link Server#SEND_PORT}.</li>
	 * 	<li>Queries new messages by sending its {@link Client#ID}.</li>
	 * 	<li>Receives new messages if there are any.</li>
	 * 	<li>Waits {@link Client#QUERY_MESSAGES_INTERVAL} milliseconds and goes back to step 1.</li>
	 * </ol>
	 * @see Service#onRepeat()
	 */
	@Override
	public void onAccept() {
		try {
			// 2. Tell the server the client's id
			sendUsername();

			// 3. Receive the messages as response from the server
			ArrayList<Message> messages = receiveMessages();
			
			if(messages != null) {
				// 4. The server responded -> there are new messages available
				Logger.debug(this.getClass().getSimpleName(), "Successfully received " + messages.size() + " new message(s) from the send server");
				
				// 5. Decrypt the received messages
				decryptMessages(messages);
				
				// 6. Log the received messages
				logMessages(messages);
				
				// 7. Add the message to the message manager (message will automatically get stored in the chat database)
				ClientMessageManager.getInstance().manage(messages.toArray(new Message[messages.size()]));
			}else {
				// 4. -> When the message ArrayList is null the server didn't respond -> No new messages available
				Logger.debug(this.getClass().getSimpleName(), "No new messages available");
			}
		} catch (IOException e) {
			// 1 -> When the client failed to connect to the server
			Logger.error(this.getClass().getSimpleName(), "Failed to connect to the send server");
		}
		
		waitForNextRequest();
	}
	
	/**
	 * Logs in to the send server by sending the client id
	 * @param connectionToServer the socket connection to the send server
	 * @throws IOException
	 */
	private void sendUsername() throws IOException {
		Logger.debug(this.getClass().getSimpleName(), "Querying messages from the send server using the username (" + ClientConfig.USER_NAME + ")");
		getOutputStream().writeUTF(ClientConfig.USER_NAME);
		getOutputStream().flush();
	}
	
	/**
	 * Reads the socket's input stream to receive new messages
	 * @param connectionToServer the socket connection to the send server
	 * @return the received messages (the server's response)
	 * @throws IOException
	 */
	private ArrayList<Message> receiveMessages()  {
		try {
			byte[] messages = null;
			int length = getInputStream().readInt();
			if(length!=0) {
				messages = new byte[length];
			    getInputStream().readFully(messages, 0, length);
			    
			    return (ArrayList<Message>) Security.decryptF(messages);
			}
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Decrypts the messages and cleans their local data
	 * @param messages the messages to decrypt
	 */
	private void decryptMessages(ArrayList<Message> messages) {
		for(Message m : messages) {
			m.getLocalData().setId(-1);
			m.getLocalData().setSendState(SendState.SENT);
			
	    	m.decrypt();
		}
		Logger.debug(this.getClass().getSimpleName(), "Decrypted " + messages.size() +  " message(s)");
	}
	
	/**
	 * Logs the messages
	 * @param messages the messages to log
	 */
	private void logMessages(ArrayList<Message> messages) {
		for(int i=0; i<messages.size(); i++) {
			Logger.debug(this.getClass().getSimpleName(), "Message " + i + ": " + messages.get(i).toString());
		}
	}
	
	/**
	 * Let the client wait a while before querying new messages again
	 */
	private void waitForNextRequest() {
		try {
			Thread.sleep(ClientConfig.QUERY_MESSAGES_INTERVAL);
		} catch (InterruptedException e) {
			Logger.error(this.getClass().getSimpleName(), "Thread failed to sleep");
			setServiceCrashed(true);
		}
	}

}
