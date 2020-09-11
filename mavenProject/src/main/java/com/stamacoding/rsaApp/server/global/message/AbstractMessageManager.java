package com.stamacoding.rsaApp.server.global.message;

import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;

/**
 * Manages all stored, received, sending and sent messages. Provides methods to filter messages by their attributes.
 */
public abstract class AbstractMessageManager {

	
	/** {@link ArrayList} containing all messages that have to get stored or updated */
	private volatile ArrayList<Message> messages = new ArrayList<Message>();
	
	
	/**
	 * Gets an {@link ArrayList} containing all messages managed by the message manager.
	 * @return {@link ArrayList} containing all messages managed by the message manager
	 */
	public ArrayList<Message> getCurrentlyManagedMessages() {
		return messages;
	}
	
	/**
	 * Adds a single or multiple {@link Message}(s) to the message manager.
	 * @param messages to get managed
	 */
	public void manage(Message...messages) {
		for(Message m : messages) {
			if(m == null) {
				Logger.warning(this.getClass().getSimpleName(), "Tried to add null message");
				continue;
			}
			if(getCurrentlyManagedMessages().indexOf(m) != -1) {
				Logger.warning(this.getClass().getSimpleName(), "Tried to add already added message: " + m.toString());
				continue;
			}
			getCurrentlyManagedMessages().add(m);
			Logger.debug(this.getClass().getSimpleName(), "Added new message to MessageManager: " + m.toString());
		}
	}
}
