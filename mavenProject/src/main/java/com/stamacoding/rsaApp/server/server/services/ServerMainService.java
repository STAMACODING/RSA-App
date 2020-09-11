package com.stamacoding.rsaApp.server.server.services;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.global.service.Service;
import com.stamacoding.rsaApp.server.server.ServerConfig;

/**
 * <p>{@link Service} handling all message transfers on the server.</p>
 * Unites the work of these services:
 * <ul>
 *  <li>{@link ServerReceiveService}</li>
 *  <li>{@link ServerSendService}</li>
 *  <li>Not documentated new ones (coming soon)</li>
 * </ul>
 */
public class ServerMainService extends Service{
	
	/** The only instance of this class */
	private volatile static ServerMainService singleton = new ServerMainService();

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
		
		UserDatabaseService.getInstance().launch();
		ServerSendService.getInstance().launch();
		ServerReceiveService.getInstance().launch();
		
		// TODO: Unfinished
		PingService.getInstance().launch();
		SignUpService.getInstance().launch();
		LoginService.getInstance().launch();
	}
	
	/**
	 * Stops all services.
	 */
	@Override
	public void onStop() {
		ServerReceiveService.getInstance().setStopRequested(true);
		ServerSendService.getInstance().setStopRequested(true);
		
		SignUpService.getInstance().setStopRequested(true);
		LoginService.getInstance().setStopRequested(true);
		PingService.getInstance().setStopRequested(true);
		
		UserDatabaseService.getInstance().setStopRequested(true);
	}
}