package com.stamacoding.rsaApp.server.client.managers;

import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.client.services.ChatDatabaseService;
import com.stamacoding.rsaApp.server.client.services.ClientReceiveService;
import com.stamacoding.rsaApp.server.message.AbstractMessageManager;
import com.stamacoding.rsaApp.server.message.Message;

/**
 * Manages all stored, received, sending and sent messages. Provides methods to filter messages by their attributes.
 */
public class ClientMessageManager extends AbstractMessageManager{
	
	/** The only instance of this class */
	private volatile static ClientMessageManager singleton = new ClientMessageManager();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ClientMessageManager() {
		init();
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ClientMessageManager getInstance() {
		return singleton;
	}

	
	/**
	 * Queries all messages from the chat database.
	 * @return whether the process succeeded
	 */
	public boolean init() {
		Logger.debug(ClientMessageManager.class.getSimpleName(), "Querying stored messages from chat database");
		ArrayList<Message> storedMessages = null;
		try {
			storedMessages = ChatDatabaseService.getInstance().getMessages();
			manage((Message[]) storedMessages.toArray());
			return true;
		} catch (Exception e) {
			Logger.warning(ClientMessageManager.class.getSimpleName(), "Couldn't get messages from chat database.");
			return false;
		}
	}
	
	/**
	 * Gets a message from the {@link ClientMessageManager} that should get stored or updated in the client's chat database.
	 * Returns {@code null} if there is no message to store or update.
	 * @return the message that should get stored or updated
	 */
	public Message getMessageToStoreOrUpdate() {
		for(int i=0; i<getAllMessages().size(); i++) {
			Message m = getAllMessages().get(i);
			if(m == null || m.getLocalData() == null) return null;
			if(m.getLocalData().isToStore() || m.getLocalData().isToUpdate()) {
				return m;
			}
		}
		return null;
	}
	
	/**
	 * Gets a message from the {@link ClientMessageManager} that should get sent.
	 * Returns {@code null} if there is no message to store or update.
	 * @return the message that should get sent
	 */
	public Message getMessageToSend() {
		for(int i=0; i<getAllMessages().size(); i++) {
			Message m = getAllMessages().get(i);
			if(m == null || m.getLocalData() == null) return null;
			if(m.getLocalData().isToSend()) {
				return m;
			}
		}
		return null;
	}
}
