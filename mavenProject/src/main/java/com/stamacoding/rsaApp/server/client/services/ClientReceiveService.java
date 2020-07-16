package com.stamacoding.rsaApp.server.client.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.NetworkUtils;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.client.Client;
import com.stamacoding.rsaApp.server.client.ClientConfig;
import com.stamacoding.rsaApp.server.client.managers.ClientMessageManager;
import com.stamacoding.rsaApp.server.message.Message;
import com.stamacoding.rsaApp.server.message.data.SendState;
import com.stamacoding.rsaApp.server.server.Server;

/**
 * {@link Service} receiving messages from the server and forwarding them to the {@link ClientMessageManager}.
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
			Socket connectionToServer = connectToSendServer();
			
			loginUsingClientID(connectionToServer);

			ArrayList<Message> messages = receiveMessages(connectionToServer);
			
			if(messages != null) {
				Logger.debug(this.getClass().getSimpleName(), "Successfully received " + messages.size() + " new message(s) from the send server");
				decryptMessages(messages);
				logMessages(messages);
				ClientMessageManager.getInstance().manage(messages.toArray(new Message[messages.size()]));
			}else {
				Logger.debug(this.getClass().getSimpleName(), "No new messages available");
			}
			connectionToServer.close();
			Logger.debug(this.getClass().getSimpleName(), "Closed connection to the send server");
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to connect to the send server");
		}
		
		waitForNextRequest();
	}
	
	/**
	 * Connects to the send server
	 * @return the created socket connection
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private Socket connectToSendServer() throws UnknownHostException, IOException {
		Socket connectionToServer = new Socket(ClientConfig.SERVER_IP, ClientConfig.RECEIVE_PORT);
		connectionToServer.setSoTimeout(5000);
		Logger.debug(this.getClass().getSimpleName(), "Connected to the send server");
		return connectionToServer;
	}
	
	/**
	 * Logs in to the send server by sending the client id
	 * @param connectionToServer the socket connection to the send server
	 * @throws IOException
	 */
	private void loginUsingClientID(Socket connectionToServer) throws IOException {
		Logger.debug(this.getClass().getSimpleName(), "Querying messages from the send server using the client id (" + ClientConfig.ID + ")");
		DataOutputStream outputStream = new DataOutputStream(connectionToServer.getOutputStream());
		outputStream.writeByte(ClientConfig.ID);
	}
	
	/**
	 * Reads the socket's input stream to receive new messages
	 * @param connectionToServer the socket connection to the send server
	 * @return the received messages (the server's response)
	 * @throws IOException
	 */
	private ArrayList<Message> receiveMessages(Socket connectionToServer) throws IOException {
		DataInputStream inputStream = new DataInputStream(connectionToServer.getInputStream());
		byte[] messages = null;
		int length = inputStream.readInt();
		if(length!=0) {
			messages = new byte[length];
		    inputStream.readFully(messages, 0, length);
		    
		    return (ArrayList<Message>) NetworkUtils.deserialize(messages);
		}
		return null;
	}
	
	/**
	 * Decrypts the messages and cleans their local data
	 * @param messages the messages to decrypt
	 */
	private void decryptMessages(ArrayList<Message> messages) {
		for(Message m : messages) {
			m.getLocalData().setId(-1);
			m.getLocalData().setSendState(SendState.SENT);
			
	    	m.decryptServerData();
	    	m.decryptProtectedData();
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
