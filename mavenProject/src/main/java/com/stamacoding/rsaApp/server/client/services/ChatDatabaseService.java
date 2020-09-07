package com.stamacoding.rsaApp.server.client.services;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.client.Client;
import com.stamacoding.rsaApp.server.client.managers.ClientMessageManager;
import com.stamacoding.rsaApp.server.client.managers.MessageDatabaseManager;
import com.stamacoding.rsaApp.server.message.Message;

/**
 * {@link Service} storing and updating messages in the chat database using the {@link ClientMessageManager}.
 */
public class ChatDatabaseService extends Service{
	
	/** The only instance of this class */
	private volatile static ChatDatabaseService singleton = new ChatDatabaseService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ChatDatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ChatDatabaseService() {
		super(ChatDatabaseService.class.getSimpleName());
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ChatDatabaseService getInstance() {
		return singleton;
	}

	/**
	 * If {@link Client#getMessageToStoreOrUpdate()} returns a message, this message will get updated/stored.
	 * @see Service#onRepeat()
	 */
	@Override
	public void onRepeat() {
		// Check if there is any message to store or update
		Message m = ClientMessageManager.getInstance().getMessageToStoreOrUpdate();
		
		if(m != null) {
			Logger.debug(this.getClass().getSimpleName(), "Got message to store/update: " + m.toString());
			
			// Update message if is is already stored in the chat database
			if(m.getLocalData().isToUpdate()) updateMessage(m);
			
			// Store new message
			else storeMessage(m);
			
		}
	}
	
	/**
	 * Updates the message in the chat database
	 * @param m the message to update
	 */
	private void updateMessage(Message m) {
		MessageDatabaseManager.getInstance().updateMessage(m);
		m.getLocalData().setUpdateRequested(false);
		Logger.debug(this.getClass().getSimpleName(), "Updated message in the chat database: " + m.toString());
	}
	
	/**
	 * Stores the message in the chat database
	 * @param m the message to store
	 */
	private void storeMessage(Message m) {
		MessageDatabaseManager.getInstance().addMessageToDB(m);
		m.getLocalData().setUpdateRequested(false);
		Logger.debug(this.getClass().getSimpleName(), "Stored new message in the chat database: " + m.toString());
	}
}
