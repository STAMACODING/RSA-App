package com.stamacoding.rsaApp.network.global.message;

import java.util.ArrayList;

import com.stamacoding.rsaApp.logger.L;

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
				L.w(this.getClass(), "Tried to add null message");
				continue;
			}
			if(getCurrentlyManagedMessages().indexOf(messages[i]) != -1) {
				L.w(this.getClass(), "Tried to add already added message: " + messages[i].toString());
				continue;
			}
			getCurrentlyManagedMessages().add(messages[i]);
			L.d(this.getClass(), "Added new message to MessageManager: " + messages[i].toString());
		}
	}
	

	public void manage(ArrayList<Message> pendingMessages) {
		for(int i=0; i<pendingMessages.size(); i++) {
			if(pendingMessages.get(i) == null) {
				L.w(this.getClass(), "Tried to add null message");
				continue;
			}
			if(getCurrentlyManagedMessages().indexOf(pendingMessages.get(i)) != -1) {
				L.w(this.getClass(), "Tried to add already added message: " + pendingMessages.get(i).toString());
				continue;
			}
			getCurrentlyManagedMessages().add(pendingMessages.get(i));
			L.d(this.getClass(), "Added new message to MessageManager: " + pendingMessages.get(i).toString());
		}
	}
}
