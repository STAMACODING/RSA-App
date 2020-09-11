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
		for(int i=0; i<messages.length; i++) {
			if(messages[i] == null) {
				Logger.warning(this.getClass().getSimpleName(), "Tried to add null message");
				continue;
			}
			if(getCurrentlyManagedMessages().indexOf(messages[i]) != -1) {
				Logger.warning(this.getClass().getSimpleName(), "Tried to add already added message: " + messages[i].toString());
				continue;
			}
			getCurrentlyManagedMessages().add(messages[i]);
			Logger.debug(this.getClass().getSimpleName(), "Added new message to MessageManager: " + messages[i].toString());
		}
	}
	

	public void manage(ArrayList<Message> pendingMessages) {
		for(int i=0; i<pendingMessages.size(); i++) {
			if(pendingMessages.get(i) == null) {
				Logger.warning(this.getClass().getSimpleName(), "Tried to add null message");
				continue;
			}
			if(getCurrentlyManagedMessages().indexOf(pendingMessages.get(i)) != -1) {
				Logger.warning(this.getClass().getSimpleName(), "Tried to add already added message: " + pendingMessages.get(i).toString());
				continue;
			}
			getCurrentlyManagedMessages().add(pendingMessages.get(i));
			Logger.debug(this.getClass().getSimpleName(), "Added new message to MessageManager: " + pendingMessages.get(i).toString());
		}
	}
}
