package com.stamacoding.rsaApp.server.client.services;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.client.managers.ClientMessageManager;
import com.stamacoding.rsaApp.server.client.managers.DatabaseManager;
import com.stamacoding.rsaApp.server.message.Message;

/**
 * {@link Service} storing and updating messages in the chat database using the {@link ClientMessageManager}.
 */
public class DatabaseService extends Service{
	
	/** The only instance of this class */
	private static volatile DatabaseService singleton = new DatabaseService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link DatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private DatabaseService() {
		super(DatabaseService.class.getSimpleName());
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static DatabaseService getInstance() {
		return singleton;
	}

	/**
	 * If {@link Client#getMessageToStoreOrUpdate()} returns a message, this message will get updated/stored.
	 * @see Service#onRepeat()
	 */
	@Override
	public void onRepeat() {
		Message m = ClientMessageManager.getInstance().getMessageToStoreOrUpdate();
		if(m != null) {
			Logger.debug(this.getClass().getSimpleName(), "Got message to store/update");
			if(m.getLocalData().isToUpdate()) {
				DatabaseManager.getInstance().updateMessage(m);
				m.getLocalData().setUpdateRequested(false);
				Logger.debug(this.getClass().getSimpleName(), "Updated message in the chat database");
			}else {
				DatabaseManager.getInstance().addMessageToDB(m);
				m.getLocalData().setUpdateRequested(false);
				Logger.debug(this.getClass().getSimpleName(), "Stored new message in the chat database");
			}
			
		}
	}
}
