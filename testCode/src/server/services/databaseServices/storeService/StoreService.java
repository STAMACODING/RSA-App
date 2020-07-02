package server.services.databaseServices.storeService;

import server.services.Service;
import server.services.databaseServices.DBManager;
import server.services.databaseServices.DatabaseMessage;

public class StoreService extends Service{
	private static volatile StoreService singleton = new StoreService();

	private StoreService() {
		super("store-service");
	}
	
	public static StoreService getInstance() {
		return singleton;
	}
	
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
