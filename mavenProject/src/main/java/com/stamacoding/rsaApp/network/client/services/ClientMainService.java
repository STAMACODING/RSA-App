package com.stamacoding.rsaApp.network.client.services;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.network.client.ClientConfig;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.global.session.LoginState;

/**
 * <p>{@link Service} handling all client message transfers. Additionally this service stores all messages in a chat database.</p>
 * Unites the work of these services: 
 * <ul>
 *  <li>{@link ChatDatabaseService}</li>
 *  <li>{@link ClientReceiveService}</li>
 *  <li>{@link ClientSendService}</li>
 * </ul>
 */
public class ClientMainService extends Service{
	
	/** The only instance of this class */
	private volatile static ClientMainService singleton = new ClientMainService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientMainService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ClientMainService() {
		super(ClientMainService.class.getSimpleName());
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ClientMainService getInstance() {
		return singleton;
	}

	/**
	 * Starts all client services if the {@link ClientConfig} is valid.
	 */
	@Override
	public void onStart() {
		ClientConfig.log();
		if(!ClientConfig.isValid()) Logger.error(this.getClass().getSimpleName(), new IllegalStateException("Invalid client configuration! Use ClientConfig.setup() to fix"));
		
		Logger.debug(this.getClass().getSimpleName(), "Launching subservices...");

		
		ChatDatabaseService.getInstance().launch();
		SessionService.getInstance().launch();
		// Waiting for being online
		while(SessionService.getInstance().getSession().getState() != LoginState.LOGGED_IN) {
			if(isStopRequested() || isServiceCrashed()) return;
		}

		ClientReceiveService.getInstance().launch();
		ClientSendService.getInstance().launch();
	}
	
	/**
	 * Stops all services.
	 */
	@Override
	public void onStop() {
		ChatDatabaseService.getInstance().setStopRequested(true);
		ClientReceiveService.getInstance().setStopRequested(true);
		ClientSendService.getInstance().setStopRequested(true);
		
		// Unfinished
		SessionService.getInstance().setStopRequested(true);
	}
}