package server.services.databaseServices.storeService;

import server.services.Service;
import server.services.databaseServices.DBManager;
import server.services.databaseServices.DatabaseMessage;

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
	
	/**
	 * Runs the {@link StoreService}. If the {@link StoreQueue} got an entry the message gets polled and stored.
	 */
	@Override
	public void run() {
		super.run();
		while(!requestedShutDown()) {
			if(!StoreQueue.isEmpty()) {
				DatabaseMessage m = StoreQueue.poll();
				DBManager.getInstance().addMessageToDB(m);
			}
		}
	}
}
