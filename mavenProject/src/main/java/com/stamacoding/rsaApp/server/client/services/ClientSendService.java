package com.stamacoding.rsaApp.server.client.services;

import java.io.IOException;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.client.Client;
import com.stamacoding.rsaApp.server.client.ClientConfig;
import com.stamacoding.rsaApp.server.client.managers.ClientMessageManager;
import com.stamacoding.rsaApp.server.global.message.Message;
import com.stamacoding.rsaApp.server.global.message.data.SendState;
import com.stamacoding.rsaApp.server.global.service.Service;
import com.stamacoding.rsaApp.server.server.services.ServerReceiveService;


/**
 *  {@link Service} sending messages from client to server using the {@link ClientMessageManager}.
 */
public class ClientSendService extends ClientSocketService {
	private Message message;
	
	/** The only instance of this class */
	private volatile static ClientSendService singleton = new ClientSendService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientSendService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ClientSendService() {
		super(ClientSendService.class.getSimpleName(), ClientConfig.SERVER_IP, ClientConfig.SEND_PORT);
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
	 * 	<li>If {@link Client#pollToSend()} does not return null, there is a message to send.</li>
	 * 	<li>After that the client connect to the server using a {@link Socket}.</li>
	 * 	<li>Then the message gets encoded and sent.</li>
	 * 	<li>If the server successfully receives the message, the message's {@link SendState} will get updated to {@link SendState#SENT}.</li>
	 * </ol>
	 * @see Service#onRepeat()
	 */
	@Override
	public void onRepeat() {
		// 0. Check if there is any message to send
		setMessage(ClientMessageManager.getInstance().pollToSend());
		
		if(getMessage() != null) {			
			super.onRepeat();
		}
	}
	
	@Override
	protected void onAccept() {
		Message clonedMessage = getMessage().clone();
		
		Logger.debug(this.getClass().getSimpleName(), "Got new message to send from MessageManager");
		Logger.debug(this.getClass().getSimpleName(), "Message to send: " + getMessage().toString());
		
		// 1. Encrypt message
		getMessage().encrypt();
		Logger.debug(this.getClass().getSimpleName(), "Encrypted message");
		try {
			// 2. Send message
			sendMessage();
			
			// 3. Receive answer
			if(receiveAnswer()) {
				// 4. Update message's state
				updateMessageState(clonedMessage);
			}else {
				// 4. Re-add message to message manager
				Logger.error(this.getServiceName(), "Failed to send message");
				ClientMessageManager.getInstance().manage(clonedMessage);
			}
		} catch (IOException e) {
			// 2. -> When failing to send message
			Logger.error(this.getClass().getSimpleName(), "Failed to send message");
			ClientMessageManager.getInstance().manage(clonedMessage);
		}
	}
	
	private boolean receiveAnswer() {
		try {
			int answer = getInputStream().readInt();
			
			switch(answer) {
			case ServerReceiveService.AnswerCodes.RECEIVED_VALID_MESSAGE:
				Logger.debug(this.getServiceName(), "Server received valid message");
				return true;
			case ServerReceiveService.AnswerCodes.RECEIVED_INVALID_DATA:
				Logger.error(this.getServiceName(), "Server received invalid data");
				return false;
			case ServerReceiveService.AnswerCodes.RECEIVED_INVALID_MESSAGE:
				Logger.error(this.getServiceName(), "Server received message from/to unregistered user");
				return false;
			}
		} catch (IOException e) {
			Logger.error(this.getServiceName(), "Error while receiving answer from server");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Sends the message to the receive server by sending two byte arrays containing message's server data and protected data.
	 * @param m the message to send
	 * @param connectionToServer the connection to the receive server
	 * @throws IOException
	 */
	private void sendMessage() throws IOException {
		// Send message meta
		getOutputStream().writeInt(getMessage().getEncryptedServerData().length);
		getOutputStream().write(getMessage().getEncryptedServerData());
		
		// Send message data
		getOutputStream().writeInt(getMessage().getEncryptedProtectedData().length);
		getOutputStream().write(getMessage().getEncryptedProtectedData());

		getOutputStream().flush();
			
		Logger.debug(this.getClass().getSimpleName(), "Successfully sent message to the receive server: " + getMessage().toString());
	}
	
	/**
	 * Update the message's send state
	 * @param m the message to update
	 */
	private void updateMessageState(Message m) {
		Logger.debug(this.getClass().getSimpleName(), "Updating message state");
		
		m.getLocalData().setSendState(SendState.SENT);
		m.getLocalData().setUpdateRequested(true);
		
		ClientMessageManager.getInstance().manage(m);
	}

	private Message getMessage() {
		return message;
	}

	private void setMessage(Message message) {
		this.message = message;
	}
}
