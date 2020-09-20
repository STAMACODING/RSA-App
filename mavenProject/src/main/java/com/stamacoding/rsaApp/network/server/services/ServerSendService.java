package com.stamacoding.rsaApp.network.server.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.server.Server;
import com.stamacoding.rsaApp.network.server.ServerConfig;
import com.stamacoding.rsaApp.network.server.managers.ServerMessageManager;
import com.stamacoding.rsaApp.security.Security;

/**
 * {@link ServerSocketService} sending messages to requesting clients using a {@link ServerSocket}.
 */
public class ServerSendService extends ServerSocketService {
	
	/** The only instance of this class */
	private volatile static ServerSendService singleton = new ServerSendService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ServerSendService#getInstance()} to get the
	 *  only instance of this class.
	 *  The server's port is set to {@link Server#SEND_PORT}.
	 */
	private ServerSendService() {
		super(ServerSendService.class.getSimpleName(), ServerConfig.SEND_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ServerSendService getInstance() {
		return singleton;
	}
	

	/**
	 * <ol>
	 * 	<li>If a client connects to the sever using a {@link Socket} and queries his messages, he tells the server his unique id.</li>
	 * 	<li>After that using this id the server searches for messages using the {@link ServerMessageManager} that concern the client.</li>
	 * 	<li>If there are any messages the server sends them (fully encrypted) to the client.</li>
	 * </ol>
	 */
	@Override
	public void onAccept() {
		try {
			// 2. If there are messages available
			if(ServerMessageManager.getInstance().getCurrentlyManagedMessages().size() != 0) {

				// 3. Read the client's id
				String username = readUsername();

				// 4. Search for messages concerning the requesting client
				Logger.debug(this.getClass().getSimpleName(), "Searching for messages concerning the requesting client (" + username + ")");
				ArrayList<Message> messages = ServerMessageManager.getInstance().poll(username);
				
				// 5. Send messages to the client
				sendMessages(username, messages);
			}else {
				// 2. If there are no messages
				Logger.debug(this.getClass().getSimpleName(), "No messages available to send");
			}
		} catch (IOException e) {
			// 1. -> If the server failed to accept the client's connection
			Logger.error(this.getClass().getSimpleName(), "Failed to send message(s) to the client");
		}
	}
	
	/**
	 * Read the client's id from the socket's input
	 * @param connectionFromClient the connection to the client
	 * @return the client's id
	 * @throws IOException
	 */
	private String readUsername() throws IOException {
		String username = getInputStream().readUTF();
		Logger.debug(this.getClass().getSimpleName(), "Client logged in as (" + username + ")");
		return username;
	}
	
	/**
	 * Send the message's to the client
	 * @param connectionFromClient the connection to the client
	 * @param clientId the client's id
	 * @param messages the messages to send
	 * @throws IOException
	 */
	private void sendMessages(String username, ArrayList<Message> messages) throws IOException {
		int messageCount = messages.size();
		byte[] messagesToSend = Security.encryptF(messages);
		
		if(messageCount > 0) {
			Logger.debug(this.getClass().getSimpleName(), "Found " + messageCount + " messages belonging to (" + username + ")");
			
			getOutputStream().writeInt(messagesToSend.length);
			getOutputStream().write(messagesToSend);
			
			Logger.debug(this.getClass().getSimpleName(), "Successfully sent " + messageCount + " message(s) to a client");
		}else{
			Logger.debug(this.getClass().getSimpleName(), "Found no messages belongig to (" + username + ")");
		}
	}

}
