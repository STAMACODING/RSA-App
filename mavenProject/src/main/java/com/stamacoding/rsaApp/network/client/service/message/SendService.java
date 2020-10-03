package com.stamacoding.rsaApp.network.client.service.message;

import java.io.IOException;
import java.net.Socket;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.Client;
import com.stamacoding.rsaApp.network.client.Config;
import com.stamacoding.rsaApp.network.client.manager.MessageManager;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.global.message.data.SendState;
import com.stamacoding.rsaApp.network.global.service.ClientSocketService;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.server.service.message.ReceiveService;


/**
 *  {@link Service} sending messages from client to server using the {@link MessageManager}.
 */
public class SendService extends ClientSocketService {
	private Message message;
	
	/** The only instance of this class */
	private volatile static SendService singleton = new SendService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link SendService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private SendService() {
		super(Config.SERVER_IP, Config.SEND_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static SendService getInstance() {
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
		setMessage(MessageManager.getInstance().pollToSend());
		
		if(getMessage() != null) {			
			super.onRepeat();
		}
	}
	
	@Override
	protected void onAccept() {		
		L.d(this.getClass(), "Got new message to send: " + getMessage().toString());
		Message messageToSend = getMessage().clone();
		
		// 1. Encrypt message
		messageToSend.encrypt();
		L.d(this.getClass(), "Encrypted message: " + messageToSend.toString());
		try {
			// 2. Send message
			sendMessage(messageToSend);
			
			// 3. Receive answer
			if(receiveAnswer()) {
				// 4. Update message's state
				updateMessageState(getMessage(), SendState.SENT);
			}else {
				// 4. Changes messages send state
				L.e(this.getClass(), "Failed to send message: " + getMessage().toString());
				
				updateMessageState(getMessage(), SendState.FAILED);
			}
		} catch (IOException e) {
			// 2. -> When failing to send message
			L.e(this.getClass(), "Failed to send message: " + getMessage().toString(), e);
			MessageManager.getInstance().manage(getMessage());
		}
	}
	
	private boolean receiveAnswer() {
		try {
			int answer = getInputStream().readInt();
			
			switch(answer) {
			case ReceiveService.AnswerCodes.RECEIVED_VALID_MESSAGE:
				L.i(this.getClass(), "Successfully sent message to the server: " + getMessage().toString());
				return true;
			case ReceiveService.AnswerCodes.RECEIVED_INVALID_DATA:
				L.e(this.getClass(), "Server received invalid data (failed to send message)");
				return false;
			case ReceiveService.AnswerCodes.RECEIVED_INVALID_MESSAGE:
				L.e(this.getClass(), "Server received message from/to unregistered user (failed to send message)");
				return false;
			}
		} catch (IOException e) {
			L.e(this.getClass(), "Error while receiving answer from server", e);
		}
		return false;
	}
	
	/**
	 * Sends the message to the receive server by sending two byte arrays containing message's server data and protected data.
	 * @param m the message to send
	 * @param connectionToServer the connection to the receive server
	 * @throws IOException
	 */
	private void sendMessage(Message messageToSend) throws IOException {
		// Send message meta
		getOutputStream().writeInt(messageToSend.getEncryptedServerData().length);
		getOutputStream().write(messageToSend.getEncryptedServerData());
		
		// Send message data
		getOutputStream().writeInt(messageToSend.getEncryptedProtectedData().length);
		getOutputStream().write(messageToSend.getEncryptedProtectedData());

		getOutputStream().flush();
			
		L.d(this.getClass(), "Sending message to the receive server: " + messageToSend.toString());
	}
	
	/**
	 * Update the message's send state
	 * @param m the message to update
	 */
	private void updateMessageState(Message m, SendState s) {
		L.d(this.getClass(), "Updating message state");
		m.getLocalData().setSendState(s);
		m.getLocalData().setUpdateRequested(true);
		if(MessageManager.getInstance().getCurrentlyManagedMessages().indexOf(m) < 0) MessageManager.getInstance().manage(m);
	}

	private Message getMessage() {
		return message;
	}

	private void setMessage(Message message) {
		this.message = message;
	}
}
