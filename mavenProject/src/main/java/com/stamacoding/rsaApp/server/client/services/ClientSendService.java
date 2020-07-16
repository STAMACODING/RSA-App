package com.stamacoding.rsaApp.server.client.services;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.client.Client;
import com.stamacoding.rsaApp.server.client.ClientConfig;
import com.stamacoding.rsaApp.server.client.managers.ClientMessageManager;
import com.stamacoding.rsaApp.server.message.Message;
import com.stamacoding.rsaApp.server.message.data.SendState;


/**
 *  {@link Service} sending messages from client to server using the {@link ClientMessageManager}.
 */
public class ClientSendService extends Service {
	
	/** The only instance of this class */
	private static volatile ClientSendService singleton = new ClientSendService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientSendService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ClientSendService() {
		super(ClientSendService.class.getSimpleName());
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ClientSendService getInstance() {
		return singleton;
	}

	/**
	 * Sends a message from a client to the server if there is any message to send.
	 *  <ol>
	 * 	<li>If {@link Client#getMessageToSend()} does not return null, there is a message to send.</li>
	 * 	<li>After that the client connect to the server using a {@link Socket}.</li>
	 * 	<li>Then the message gets encoded and sent.</li>
	 * 	<li>If the server successfully receives the message, the message's {@link SendState} will get updated to {@link SendState#SENT}.</li>
	 * </ol>
	 * @see Service#onRepeat()
	 */
	@Override
	public void onRepeat() {
		Message m = ClientMessageManager.getInstance().getMessageToSend();
		if(m != null) {
			Logger.debug(this.getClass().getSimpleName(), "Got new message to send from MessageManager");
			Logger.debug(this.getClass().getSimpleName(), "Message to send: " + m.toString());
			
			encryptMessage(m);
			try {
				Socket connectionToServer = connectToReceiveServer();
				try {
					sendMessage(m, connectionToServer);
					updateMessageState(m);

					connectionToServer.close();
					Logger.debug(this.getClass().getSimpleName(), "Closed connection to the receive server");
				} catch (IOException e) {
					Logger.error(this.getClass().getSimpleName(), "Failed to sent message");
				}
			} catch (IOException e) {
				Logger.error(this.getClass().getSimpleName(), "Failed to connect to the receive server");
			}
			
		}
	}
	
	/**
	 * Encrypt the message that should get sent
	 * @param m the message to encrypt
	 */
	private void encryptMessage(Message m) {
		m.encryptProtectedData();
		m.encryptServerData();
		Logger.debug(this.getClass().getSimpleName(), "Encrypted message");
	}
	
	/**
	 * Connects to the receive server
	 * @return the created socket connection
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private Socket connectToReceiveServer() throws UnknownHostException, IOException {
		Socket connectionToServer = new Socket(ClientConfig.SERVER_IP, ClientConfig.SEND_PORT);
		connectionToServer.setSoTimeout(5000);
		Logger.debug(this.getClass().getSimpleName(), "Successfully connected to the receive server");
		return connectionToServer;
	}
	
	/**
	 * Sends the message to the receive server
	 * @param m the message to send
	 * @param connectionToServer the connection to the receive server
	 * @throws IOException
	 */
	private void sendMessage(Message m, Socket connectionToServer) throws IOException {
		DataOutputStream out = new DataOutputStream(connectionToServer.getOutputStream());
		// Send message meta
		out.writeInt(m.getEncryptedServerData().length);
		out.write(m.getEncryptedServerData());
		
		// Send message data
		out.writeInt(m.getEncryptedProtectedData().length);
		out.write(m.getEncryptedProtectedData());
		
		out.flush();
			
		Logger.debug(this.getClass().getSimpleName(), "Successfully sent message to the receive server");
	}
	
	/**
	 * Update the message's send state
	 * @param m the message to update
	 */
	private void updateMessageState(Message m) {
		Logger.debug(this.getClass().getSimpleName(), "Updating message status");
		
		m.getLocalData().setSendState(SendState.SENT);
		m.getLocalData().setUpdateRequested(true);
	}

}
