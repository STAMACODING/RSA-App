package com.stamacoding.rsaApp.server.server.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.NetworkUtils;
import com.stamacoding.rsaApp.server.message.Message;
import com.stamacoding.rsaApp.server.server.Server;
import com.stamacoding.rsaApp.server.server.ServerConfig;
import com.stamacoding.rsaApp.server.server.managers.ServerMessageManager;

/**
 * {@link ServerService} sending messages to requesting clients using a {@link ServerSocket}.
 */
public class ServerSendService extends ServerService {
	
	/** The only instance of this class */
	private static volatile ServerSendService singleton = new ServerSendService();

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
	public void onRepeat() {
		try {
			// 1. Accept connection from client
			Socket connectionFromClient = acceptClient();

			// 2. If there are messages available
			if(ServerMessageManager.getInstance().getAllMessages().size() != 0) {

				// 3. Read the client's id
				byte clientId = readClientsId(connectionFromClient);

				// 4. Search for messages concerning the requesting client
				Logger.debug(this.getClass().getSimpleName(), "Searching for messages concerning the requesting client (" + clientId + ")");
				ArrayList<Message> messages = ServerMessageManager.getInstance().poll(clientId);
				
				// 5. Send messages to the client
				sendMessages(connectionFromClient, clientId, messages);
			}else {
				// 2. If there are no messages
				Logger.debug(this.getClass().getSimpleName(), "No messages available to send");
			}
			// 6. Close connection to client
			connectionFromClient.close();
			Logger.debug(this.getClass().getSimpleName(), "Closed connection to client");
		} catch (IOException e) {
			// 1. -> If the server failed to accept the client's connection
			Logger.error(this.getClass().getSimpleName(), "Failed to send message(s) to the client");
		}
	}
	
	/**
	 * Accept a client connecting to the send server.
	 * @return the socket connection to the client
	 * @throws IOException
	 */
	private Socket acceptClient() throws IOException {
		Socket connectionFromClient = getServerSocket().accept();
		connectionFromClient.setSoTimeout(5000);
		Logger.debug(this.getClass().getSimpleName(), "Received new client request");
		return connectionFromClient;
	}
	
	/**
	 * Read the client's id from the socket's input
	 * @param connectionFromClient the connection to the client
	 * @return the client's id
	 * @throws IOException
	 */
	private byte readClientsId(Socket connectionFromClient) throws IOException {
		DataInputStream inputStream = new DataInputStream(connectionFromClient.getInputStream());

		byte clientId = inputStream.readByte();
		Logger.debug(this.getClass().getSimpleName(), "Client logged in as (" + clientId + ")");
		return clientId;
	}
	
	/**
	 * Send the message's to the client
	 * @param connectionFromClient the connection to the client
	 * @param clientId the client's id
	 * @param messages the messages to send
	 * @throws IOException
	 */
	private void sendMessages(Socket connectionFromClient, byte clientId, ArrayList<Message> messages) throws IOException {
		int messageCount = messages.size();
		byte[] messagesToSend = NetworkUtils.serialize(messages);
		DataOutputStream outputStream = new DataOutputStream(connectionFromClient.getOutputStream());
		
		if(messageCount > 0) {
			Logger.debug(this.getClass().getSimpleName(), "Found " + messageCount + " messages belonging to (" + clientId + ")");
			
			outputStream.writeInt(messagesToSend.length);
			outputStream.write(messagesToSend);
			
			Logger.debug(this.getClass().getSimpleName(), "Successfully sent " + messageCount + " message(s) to a client");
		}else{
			Logger.debug(this.getClass().getSimpleName(), "Found no messages belongig to (" + clientId + ")");
		}
	}

}
