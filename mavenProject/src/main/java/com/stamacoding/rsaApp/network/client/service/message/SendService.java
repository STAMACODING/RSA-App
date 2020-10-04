package com.stamacoding.rsaApp.network.client.service.message;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.Config;
import com.stamacoding.rsaApp.network.client.service.user.SessionService;
import com.stamacoding.rsaApp.network.global.answerCodes.AnswerCodes;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.global.message.data.SendState;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.global.service.executor.ClientExecutorService;
import com.stamacoding.rsaApp.network.server.manager.MessageManager;
import com.stamacoding.rsaApp.security.rsa.RSA;


/**
 *  {@link Service} sending messages from client to server using the {@link MessageManager}.
 */
public class SendService extends ClientExecutorService {
	
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
	
	public boolean send(Message m) {
		validateThread();
		L.d(this.getClass(), "New message to send: " + m.toString());
		
		Message encryptedClone = m.clone();
		encryptedClone.encrypt();
		L.d(this.getClass(), "Encrypted message: " + encryptedClone.toString());
		try {
			if(sendSessionId()) {
				transferMessage(encryptedClone);

				if(receiveAnswer(encryptedClone)) {
					updateMessageState(m, SendState.SENT);
					return true;
				}else {
					L.e(this.getClass(), "Failed to send message: " + m.toString());
					updateMessageState(m, SendState.FAILED);
					return false;
				}
			}else {
				L.e(this.getClass(), "Couln't send message!");
				return false;
			}
		} catch (IOException e) {
			L.e(this.getClass(), "Failed to send message: " + m.toString(), e);
			updateMessageState(m, SendState.FAILED);
			return false;
		}
	}
	
	private boolean sendSessionId() {
		String id = SessionService.getInstance().getSession().getId();
		if(id == null) L.f(getClass(), new IllegalStateException("Cannot send message! You aren't logged in!"));
		
		try {
			L.t(getClass(), "Encrypting session id...");
			byte[] encryptedId = RSA.encryptF(id);
			L.t(getClass(), "Sending session id...");
			getOutputStream().writeInt(encryptedId.length);
			getOutputStream().write(encryptedId);
			getOutputStream().flush();
			L.d(getClass(), "Sent encrypted session id!");
			return true;
		} catch (IOException e) {
			L.e(getClass(), "Failed to send session id to server");
			return false;
		}
	}

	/**
	 * Sends the message to the receive server by sending two byte arrays containing message's server data and protected data.
	 * @param m the message to send
	 * @param connectionToServer the connection to the receive server
	 * @throws IOException
	 */
	private void transferMessage(Message m) throws IOException {
		// Send message meta
		getOutputStream().writeInt(m.getEncryptedServerData().length);
		getOutputStream().write(m.getEncryptedServerData());
		
		// Send message data
		getOutputStream().writeInt(m.getEncryptedProtectedData().length);
		getOutputStream().write(m.getEncryptedProtectedData());

		getOutputStream().flush();
			
		L.d(this.getClass(), "Sending message to the receive server: " + m.toString());
	}
	
	private boolean receiveAnswer(Message m) {
		try {
			int answer = getInputStream().readInt();
			
			switch(answer) {
			case AnswerCodes.SendMessageToServer.RECEIVED_VALID_MESSAGE:
				L.i(this.getClass(), "Successfully sent message to the server: " + m.toString());
				return true;
			case AnswerCodes.SendMessageToServer.RECEIVED_INVALID_DATA:
				L.e(this.getClass(), "Server received invalid data (failed to send message)");
				return false;
			case AnswerCodes.SendMessageToServer.RECEIVED_INVALID_MESSAGE:
				L.e(this.getClass(), "Server received message from/to unregistered user (failed to send message)");
				return false;
			}
		} catch (IOException e) {
			L.e(this.getClass(), "Error while receiving answer from server", e);
		}
		return false;
	}
	
	/**
	 * Update the message's send state
	 * @param m the message to update
	 */
	private void updateMessageState(Message m, SendState s) {
		L.d(this.getClass(), "Updating message state");
		m.getLocalData().setSendState(s);
	
		ChatDatabaseService.getInstance().execute(new Callable<Object>() {
			
			@Override
			public Object call() throws Exception {
				if(m.isStored()) {
					return ChatDatabaseService.getInstance().updateMessage(m);
				}else {
					return ChatDatabaseService.getInstance().storeMessage(m);
				}
			}
		});

	}
}
