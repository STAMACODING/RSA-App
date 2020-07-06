package server;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.config.NetworkConfig;
import server.config.NetworkType;
import server.services.Service;
import server.services.databaseServices.DatabaseService;
import server.services.transferServices.receiveService.ReceiveService;
import server.services.transferServices.sendService.SendService;

/**
 * <p>{@link Service} handling all message transfers. Additionally this service stores all messages in a chat database when
 * running on a client.</p>
 * Unites the work of these services: 
 * <ul>
 *  <li>{@link DatabaseService} (client)</li>
 *  <li>{@link ReceiveService} (server, client)</li>
 *  <li>{@link SendService} (server, client)</li>
 * </ul>
 */
public class MessageService extends Service{
	/**
	 * the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 */
	private static volatile MessageService singleton = new MessageService();

	/**
	 * the object's private constructor (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>)
	 */
	private MessageService() {
		super("network");
	}
	
	/**
	 * Gets the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 * @return the object's only instance
	 */
	public static MessageService getInstance() {
		return singleton;
	}
	
	/**
	 * Restarts the {@link MessageService} safely.
	 */
	public static void restart() {
		Logger.debug(MessageService.class.getSimpleName(), "Restarting " + singleton.getName());
		singleton.requestShutdown();
		while(singleton.isRunning()) {}
		singleton = new MessageService();
		Logger.debug(MessageService.class.getSimpleName(), "Restarted " + singleton.getName());
		singleton.start();
	}
	
	/**
	 * Starts the {@link MessageService}. The service will only stay alive if your {@link NetworkConfig} is valid.
	 */
	@Override
	public synchronized void start() {
		super.start();
	}
	
	/**
	 * Runs the {@link MessageService}. The service will only stay alive if your {@link NetworkConfig} is valid.
	 */
	@Override
	public void run() {
		if(!NetworkConfig.isValid()) throw new RuntimeException("Invalid network configuration! Use NetworkConfig.setup() to fix");
		
		if(NetworkConfig.TYPE == NetworkType.CLIENT) DatabaseService.getInstance().start();
		ReceiveService.getInstance().start();
		SendService.getInstance().start();
		
		while(!isShutDownRequested()) {
			if(NetworkConfig.TYPE == NetworkType.CLIENT) if(DatabaseService.getInstance().isCrashed()) DatabaseService.restart();
			if(ReceiveService.getInstance().isCrashed()) ReceiveService.restart();
			if(SendService.getInstance().isCrashed()) SendService.restart();
		}
		
		Logger.debug(this.getClass().getSimpleName(), "Shutting down " + getName());
		
		SendService.getInstance().requestShutdown();
		ReceiveService.getInstance().requestShutdown();
		if(NetworkConfig.TYPE == NetworkType.CLIENT) DatabaseService.getInstance().requestShutdown();
		
		Logger.debug(this.getClass().getSimpleName(), "Shut down " + getName());
	}
	
	public static void main(String[] args) throws InterruptedException {
		NetworkConfig.setup(NetworkType.CLIENT, (byte) 12, "127.0.0.1", 1001, 1002);
		MessageService.getInstance().start();
	}

}
