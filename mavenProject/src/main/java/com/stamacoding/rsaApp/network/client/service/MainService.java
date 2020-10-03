package com.stamacoding.rsaApp.network.client.service;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.Config;
import com.stamacoding.rsaApp.network.client.service.message.ChatDatabaseService;
import com.stamacoding.rsaApp.network.client.service.message.ReceiveService;
import com.stamacoding.rsaApp.network.client.service.message.SendService;
import com.stamacoding.rsaApp.network.client.service.user.LoginService;
import com.stamacoding.rsaApp.network.client.service.user.SessionService;
import com.stamacoding.rsaApp.network.client.service.user.SignUpService;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.global.session.LoginState;

/**
 * <p>{@link Service} handling all client message transfers. Additionally this service stores all messages in a chat database.</p>
 * Unites the work of these services: 
 * <ul>
 *  <li>{@link ChatDatabaseService}</li>
 *  <li>{@link ReceiveService}</li>
 *  <li>{@link SendService}</li>
 * </ul>
 */
public class MainService extends Service{
	
	/** The only instance of this class */
	private volatile static MainService singleton = new MainService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link MainService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private MainService() {
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static MainService getInstance() {
		return singleton;
	}

	/**
	 * Starts all client services if the {@link Config} is valid.
	 */
	@Override
	public void onStart() {
		Config.log();
		if(!Config.isValid()) L.f(this.getClass(), new IllegalStateException("Invalid client configuration! Use ClientConfig.setup() to fix"));
		
		L.i(this.getClass(), "Launching subservices...");

		
		ChatDatabaseService.getInstance().launch();
		
		if(Config.REGISTERED) LoginService.getInstance().launch();
		else SignUpService.getInstance().launch();


		ReceiveService.getInstance().launch();
		SendService.getInstance().launch();
		
		L.i(this.getClass(), "Launched subservices!");
	}
	
	/**
	 * Stops all services.
	 */
	@Override
	public void onStop() {		
		ChatDatabaseService.getInstance().setStopRequested(true);
		ReceiveService.getInstance().setStopRequested(true);
		SendService.getInstance().setStopRequested(true);
		
		// Unfinished
		SessionService.getInstance().setStopRequested(true);
	}
}
