package com.stamacoding.rsaApp.server.server.services;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.server.ServerConfig;

/**
 * <p>{@link Service} handling all message transfers on the server.</p>
 * Unites the work of these services:
 * <ul>
 *  <li>{@link ServerReceiveService}</li>
 *  <li>{@link ServerSendService}</li>
 * </ul>
 */
public class ServerMainService extends Service{
	
	/** The only instance of this class */
	private static ServerMainService singleton = new ServerMainService();

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
		ServerConfig.log();
		if(!ServerConfig.isValid()) Logger.error(this.getClass().getSimpleName(), new IllegalStateException("Invalid server configuration! Use ServerConfig.setup() to fix"));
		
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
}