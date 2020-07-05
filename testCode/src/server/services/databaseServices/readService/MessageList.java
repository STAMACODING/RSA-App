package server.services.databaseServices.readService;

import java.util.Date;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.services.databaseServices.DatabaseMessage;

public class MessageList {
	/**
	 * array containing all messages stored in the chat database
	 */
	private static volatile DatabaseMessage[] messages = null;
	
	/**
	 * the most recent time the messages were synchronized with the database
	 */
	private static volatile Date recentUpdate = null;
	
	
	/**
	 * Gets an array containing all messages stored in the chat database.
	 * @return array containing all messages stored in the chat database
	 */
	public static DatabaseMessage[] getMessages() {
		return messages;
	}

	/**
	 * Sets the array containing all messages stored in the chat database.
	 * @param messages array containing all messages stored in the chat database
	 */
	protected static void updateMessages(DatabaseMessage[] messages) {
		MessageList.messages = messages;
		MessageList.updateRecentUpdate();
		Logger.debug(MessageList.class.getSimpleName(), "Updated history's list messages");
	}

	/**
	 * Gets the most recent time the messages were synchronized with the database.
	 * @return the most recent time the messages were synchronized with the database
	 */
	public Date getRecentUpdate() {
		return recentUpdate;
	}

	/**
	 * Sets the most recent time the messages were synchronized with the database.
	 * @param recentUpdate the most recent time the messages were synchronized with the database
	 */
	private static void updateRecentUpdate() {
		MessageList.recentUpdate = new Date();
	}
}
