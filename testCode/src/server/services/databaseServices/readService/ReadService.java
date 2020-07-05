package server.services.databaseServices.readService;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.config.NetworkConfig;
import server.config.NetworkConfig.Client;
import server.services.Service;
import server.services.databaseServices.DBManager;

/**
 * {@link Service} synchronizing with the chat database to offer a clear representation of the chat history.
 * The messages get updated in an interval of {@link Client#UPDATE_CHAT_HISTORY_INTERVAL} milliseconds.
 */
public class ReadService extends Service{
	/**
	 * the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 */
	private static volatile ReadService singleton = new ReadService();

	/**
	 * the object's private constructor (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>)
	 */
	private ReadService() {
		super("chat-history");
	}
	
	/**
	 * Gets the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 * @return the object's only instance
	 */
	public static ReadService getInstance() {
		return singleton;
	}
	
	public static void restart() {
		Logger.debug(ReadService.class.getSimpleName(), "Restarting " + singleton.getName());
		singleton.requestShutdown();
		while(singleton.isRunning()) {}
		singleton = new ReadService();
		Logger.debug(ReadService.class.getSimpleName(), "Restarted " + singleton.getName());
		singleton.start();
	}

	
	/**
	 * Runs the {@link ReadService}. The messages get synchronized in an interval of {@link Client#UPDATE_CHAT_HISTORY_INTERVAL} milliseconds.
	 */
	@Override
	public void run() {
		super.run();
		while(!isShutDownRequested()) {
			MessageList.updateMessages(DBManager.getInstance().getMessagesFromDB());
			Logger.debug(this.getClass().getSimpleName(), "Successfully synchronized message array with database");
			try {
				Thread.sleep(NetworkConfig.Client.UPDATE_CHAT_HISTORY_INTERVAL);
			} catch (InterruptedException e) {
				Logger.error(this.getClass().getSimpleName(), getName() + " interrupted");
			}
			throw new RuntimeException();
		}
		Logger.debug(this.getClass().getSimpleName(), "Shut down " + getName());
	}

}
