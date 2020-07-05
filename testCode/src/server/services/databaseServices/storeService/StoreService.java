package server.services.databaseServices.storeService;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.NetworkService;
import server.services.Service;
import server.services.databaseServices.DBManager;
import server.services.databaseServices.DatabaseMessage;
import server.services.databaseServices.chatHistoryService.ChatHistoryService;

/**
 * {@link Service} storing new messages in the chat database. Use {@link StoreQueue} to store new messages.
 */
public class StoreService extends Service{
	/**
	 * the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 */
	private static volatile StoreService singleton = new StoreService();

	/**
	 * the object's private constructor (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>)
	 */
	private StoreService() {
		super("store");
	}
	
	/**
	 * Gets the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 * @return the object's only instance
	 */
	public static StoreService getInstance() {
		return singleton;
	}
	
	public static void restart() {
		Logger.debug(StoreService.class.getSimpleName(), "Restarting " + singleton.getName());
		singleton.requestShutdown();
		while(singleton.isRunning()) {}
		singleton = new StoreService();
		Logger.debug(StoreService.class.getSimpleName(), "Restarted " + singleton.getName());
		singleton.start();
	}
	
	/**
	 * Runs the {@link StoreService}. If the {@link StoreQueue} got an entry the message gets polled and stored.
	 */
	@Override
	public void run() {
		super.run();
		while(!isShutDownRequested()) {
			if(!StoreQueue.isEmpty()) {
				DatabaseMessage m = StoreQueue.poll();
				Logger.debug(this.getClass().getSimpleName(), "Polled new message from the StoreQueue");
				DBManager.getInstance().addMessageToDB(m);
				Logger.debug(this.getClass().getSimpleName(), "Stored new message in the StoreQueue");
			}
		}
		Logger.debug(this.getClass().getSimpleName(), "Shut down " + getName());
	}
}
