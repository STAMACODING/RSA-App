package com.stamacoding.rsaApp.server.server.services;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.client.services.ClientReceiveService;
import com.stamacoding.rsaApp.server.client.services.ClientSendService;
import com.stamacoding.rsaApp.server.client.services.DatabaseService;
import com.stamacoding.rsaApp.server.exceptions.InvalidValueException;
import com.stamacoding.rsaApp.server.server.ServerConfig;

/**
 * <p>{@link Service} handling all message transfers. Additionally this service stores all messages in a chat database when
 * running on a client.</p>
 * Unites the work of these services when running on client: 
 * <ul>
 *  <li>{@link DatabaseService}</li>
 *  <li>{@link ClientReceiveService}</li>
 *  <li>{@link ClientSendService}</li>
 * </ul>
 * Unites the work of these services when running on server: 
 * <ul>
 *  <li>{@link ServerReceiveService}</li>
 *  <li>{@link ServerSendService}</li>
 * </ul>
 */
public class ServerMainService extends Service{
	
	/** The only instance of this class */
	private static volatile ServerMainService singleton = new ServerMainService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link MessageService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ServerMainService() {
		super(ServerMainService.class.getSimpleName());
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ServerMainService getInstance() {
		return singleton;
	}

	/**
	 * Starts all services if the {@link ServerConfig} is valid.
	 */
	@Override
	public void onStart() {
		if(!ServerConfig.isValid()) Logger.error(this.getClass().getSimpleName(), new InvalidValueException("Invalid server configuration! Use ServerConfig.setup() to fix"));
		
		ServerReceiveService.getInstance().launch();
		ServerSendService.getInstance().launch();
	}
	
	/**
	 * Stops all services.
	 */
	@Override
	public void onStop() {
		ServerReceiveService.getInstance().setStopRequested(true);
		ServerSendService.getInstance().setStopRequested(true);
	}
	
	public static void main(String[] args) {
		ServerConfig.setup(1002, 1001);
		ServerMainService.getInstance().launch();
	}
}