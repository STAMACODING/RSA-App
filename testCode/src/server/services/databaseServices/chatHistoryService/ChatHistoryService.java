package server.services.databaseServices.chatHistoryService;

import java.util.Date;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.config.NetworkConfig;
import server.config.NetworkConfig.Client;
import server.services.Service;
import server.services.databaseServices.DBManager;
import server.services.databaseServices.DatabaseMessage;

/**
 * {@link Service} synchronizing with the chat database to offer a clear representation of the chat history.
 * The messages get updated in an interval of {@link Client#UPDATE_CHAT_HISTORY_INTERVAL} milliseconds.
 */
public class ChatHistoryService extends Service{
	/**
	 * the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 */
	private static volatile ChatHistoryService singleton = new ChatHistoryService();

	/**
	 * the object's private constructor (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>)
	 */
	private ChatHistoryService() {
		super("chat-history");
	}
	
	/**
	 * Gets the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 * @return the object's only instance
	 */
	public static ChatHistoryService getInstance() {
		return singleton;
	}
	
	/**
	 * array containing all messages stored in the chat database
	 */
	private volatile DatabaseMessage[] messages = null;
	
	/**
	 * the most recent time the messages were synchronized with the database
	 */
	private volatile Date recentUpdate = null;
	
	/**
	 * Runs the {@link ChatHistoryService}. The messages get synchronized in an interval of {@link Client#UPDATE_CHAT_HISTORY_INTERVAL} milliseconds.
	 */
	@Override
	public void run() {
		super.run();
		while(!requestedShutDown()) {
			setMessages(DBManager.getInstance().getMessagesFromDB());
			setRecentUpdate(new Date(System.currentTimeMillis()));
			
			Logger.debug(this.getClass().getSimpleName(), "Successfully synchonized message array with database");
			try {
				Thread.sleep(NetworkConfig.Client.UPDATE_CHAT_HISTORY_INTERVAL);
			} catch (InterruptedException e) {
				Logger.error(this.getClass().getSimpleName(), "chat-history-service interrupted");
			}
		}
	}
	
	/**
	 * Gets an array containing all messages stored in the chat database.
	 * @return array containing all messages stored in the chat database
	 */
	public DatabaseMessage[] getMessages() {
		if(!isRunning()) throw new RuntimeException("Cannot access messages if the ChatHistoryService is not running.");
		return messages;
	}

	/**
	 * Sets the array containing all messages stored in the chat database.
	 * @param messages array containing all messages stored in the chat database
	 */
	private void setMessages(DatabaseMessage[] messages) {
		this.messages = messages;
	}

	/**
	 * Gets the most recent time the messages were synchronized with the database.
	 * @return the most recent time the messages were synchronized with the database
	 */
	public Date getRecentUpdate() {
		if(!isRunning()) throw new RuntimeException("Cannot access the last update if the ChatHistoryService is not running.");
		return recentUpdate;
	}

	/**
	 * Sets the most recent time the messages were synchronized with the database.
	 * @param recentUpdate the most recent time the messages were synchronized with the database
	 */
	private void setRecentUpdate(Date recentUpdate) {
		this.recentUpdate = recentUpdate;
	}
}
