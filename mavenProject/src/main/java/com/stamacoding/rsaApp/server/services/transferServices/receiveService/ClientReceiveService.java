package com.stamacoding.rsaApp.server.services.transferServices.receiveService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.NetworkUtils;
import com.stamacoding.rsaApp.server.config.NetworkConfig;
import com.stamacoding.rsaApp.server.config.NetworkConfig.Client;
import com.stamacoding.rsaApp.server.config.NetworkConfig.Server;
import com.stamacoding.rsaApp.server.message.Message;
import com.stamacoding.rsaApp.server.message.MessageManager;
import com.stamacoding.rsaApp.server.message.data.SendState;
import com.stamacoding.rsaApp.server.services.Service;

/**
 * {@link Service} receiving messages from the server and forwarding them to the {@link MessageManager}.
 */
public class ClientReceiveService extends Service{
	
	/** The only instance of this class */
	private static volatile ClientReceiveService singleton = new ClientReceiveService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ClientReceiveService() {
		super(ClientReceiveService.class.getSimpleName());
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
	public void onRepeat() {
		try {
			Socket connectionToServer = new Socket(NetworkConfig.Server.IP, NetworkConfig.Server.SEND_PORT);
			connectionToServer.setSoTimeout(5000);
			Logger.debug(this.getClass().getSimpleName(), "Successfully connected to the send server");
			
			Logger.debug(this.getClass().getSimpleName(), "Querying messages from the send server using the client id (" + NetworkConfig.Client.ID + ")");
			// Read message from server
			DataOutputStream outputStream = new DataOutputStream(connectionToServer.getOutputStream());
			outputStream.writeByte(NetworkConfig.Client.ID);
			
			DataInputStream inputStream = new DataInputStream(connectionToServer.getInputStream());
			byte[] messages = null;
			try {
				int length = inputStream.readInt();
				if(length!=0) {
					messages = new byte[length];
				    inputStream.readFully(messages, 0, length);
				    
				    ArrayList<Message> messagesAsList = (ArrayList<Message>) NetworkUtils.deserialize(messages);
				    Logger.debug(this.getClass().getSimpleName(), "Successfully received " + messagesAsList.size() + " new message(s) from the send server");
					for(Message m : messagesAsList) {
						m.getLocalData().setSendState(SendState.SENT);
						System.err.println(m.getEncryptedProtectedData());
				    	m.decryptServerData();
				    	m.decryptProtectedData();
				    	System.err.println(m.getEncryptedProtectedData());
				    	Logger.debug(this.getClass().getSimpleName(), "Received message: " + m.toString());
				    	MessageManager.manage(m);
				    }
				}
			}catch(Exception e) {
				Logger.debug(this.getClass().getSimpleName(), "No new messages available");
			}
			connectionToServer.close();
			Logger.debug(this.getClass().getSimpleName(), "Closed connection to the send server");
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error(this.getClass().getSimpleName(), "Failed to connect to the send server");
		}
		try {
			Thread.sleep(NetworkConfig.Client.QUERY_MESSAGES_INTERVAL);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Logger.error(this.getClass().getSimpleName(), "Failed to make the thread sleep");
			setServiceCrashed(true);
		}
	}

}
