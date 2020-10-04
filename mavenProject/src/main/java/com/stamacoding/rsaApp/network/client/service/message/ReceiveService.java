package com.stamacoding.rsaApp.network.client.service.message;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.Client;
import com.stamacoding.rsaApp.network.client.Config;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.global.message.data.SendState;
import com.stamacoding.rsaApp.network.global.service.ClientService;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.server.Server;
import com.stamacoding.rsaApp.network.server.manager.MessageManager;
import com.stamacoding.rsaApp.security.rsa.RSA;

/**
 * {@link Service} receiving messages from the server and forwarding them to the {@link MessageManager}.
 */
public class ReceiveService extends ClientService{
	
	/** The only instance of this class */
	private volatile static ReceiveService singleton = new ReceiveService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ReceiveService() {
		super(Config.SERVER_IP, Config.RECEIVE_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ReceiveService getInstance() {
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
				L.d(this.getClass(), "Successfully received " + messages.size() + " new message(s) from the send server");
				
				// 5. Decrypt the received messages
				decryptMessages(messages);
				
				// 6. Log the received messages
				logMessages(messages);
				
				// 7. Store messages
				ChatDatabaseService.getInstance().execute(new Callable<Object>() {
					
					@Override
					public Object call() throws Exception {
						for(int i=0; i<messages.size(); i++) {
							ChatDatabaseService.getInstance().storeMessage(messages.get(i));
						}
						return null;
					}
				});
			}else {
				// 4. -> When the message ArrayList is null the server didn't respond -> No new messages available
				L.d(this.getClass(), "No new messages available");
			}
		} catch (IOException e) {
			// 1 -> When the client failed to connect to the server
			L.e(this.getClass(), "Failed to connect to the send server", e);
		}
		waitForNextRequest();
	}
	
	/**
	 * Logs in to the send server by sending the client id
	 * @param connectionToServer the socket connection to the send server
	 * @throws IOException
	 */
	private void sendUsername() throws IOException {
		L.d(this.getClass(), "Querying messages from the send server using the username (" + Config.USER_NAME + ")");
		getOutputStream().writeUTF(Config.USER_NAME);
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
			    
			    return (ArrayList<Message>) RSA.decryptF(messages);
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
		L.d(this.getClass(), "Decrypted " + messages.size() +  " message(s)");
	}
	
	/**
	 * Logs the messages
	 * @param messages the messages to log
	 */
	private void logMessages(ArrayList<Message> messages) {
		for(int i=0; i<messages.size(); i++) {
			L.i(this.getClass(), "Received message: " + messages.get(i).toString());
		}
	}
	
	/**
	 * Let the client wait a while before querying new messages again
	 */
	private void waitForNextRequest() {
		try {
			L.d(getClass(), "Waiting for next request...");
			Thread.sleep(Config.QUERY_MESSAGES_INTERVAL);
		} catch (InterruptedException e) {
			L.e(this.getClass(), "Thread failed to sleep", e);
			setServiceCrashed(true);
		}
	}

}
