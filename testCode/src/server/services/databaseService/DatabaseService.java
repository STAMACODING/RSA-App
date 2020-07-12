package server.services.databaseService;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.message.Message;
import server.services.Service;
import server.services.databaseService.MessageManager.Client;
import server.services.transferServices.receiveService.ReceiveService;

/**
 * {@link Service} storing and updating messages in the chat database.
 */
public class DatabaseService extends Service{
	/**
	 * the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 */
	private static volatile DatabaseService singleton = new DatabaseService();

	/**
	 * the object's private constructor (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>)
	 */
	private DatabaseService() {
		super("store");
	}
	
	/**
	 * Gets the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 * @return the object's only instance
	 */
	public static DatabaseService getInstance() {
		return singleton;
	}
	
	/**
	 * Restarts the {@link DatabaseService} safely.
	 */
	public static void restart() {
		Logger.debug(DatabaseService.class.getSimpleName(), "Restarting " + singleton.getName());
		singleton.requestShutdown();
		while(singleton.isRunning()) {}
		singleton = new DatabaseService();
		Logger.debug(DatabaseService.class.getSimpleName(), "Restarted " + singleton.getName());
		singleton.start();
	}
	
	/**
	 * Runs the {@link DatabaseService}. If {@link Client#getMessageToStoreOrUpdate()} returns a message, this message will get updated/stored.
	 */
	@Override
	public void run() {
		super.run();
		while(!isShutDownRequested()) {
			Message m = MessageManager.Client.getMessageToStoreOrUpdate();
			if(m != null) {
				Logger.debug(this.getClass().getSimpleName(), "Got message to store/update");
				if(m.getLocalData().isToUpdate()) {
					DBManager.getInstance().updateMessage(m);
					m.getLocalData().setUpdateRequested(false);
					Logger.debug(this.getClass().getSimpleName(), "Updated message in the chat database");
				}else {
					DBManager.getInstance().addMessageToDB(m);
					m.getLocalData().setUpdateRequested(false);
					Logger.debug(this.getClass().getSimpleName(), "Stored new message in the chat database");
				}
				
			}
		}
		Logger.debug(this.getClass().getSimpleName(), "Shut down " + getName());
	}
}
