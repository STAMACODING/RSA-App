package com.stamacoding.rsaApp.network.server.service;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.server.Config;
import com.stamacoding.rsaApp.network.server.service.database.PublicKeyDBService;
import com.stamacoding.rsaApp.network.server.service.database.UserDatabaseService;
import com.stamacoding.rsaApp.network.server.service.message.ReceiveService;
import com.stamacoding.rsaApp.network.server.service.message.SendService;
import com.stamacoding.rsaApp.network.server.service.user.InactiveUserService;
import com.stamacoding.rsaApp.network.server.service.user.LoginService;
import com.stamacoding.rsaApp.network.server.service.user.PingService;
import com.stamacoding.rsaApp.network.server.service.user.SignUpService;

/**
 * <p>{@link Service} handling all message transfers on the server.</p>
 * Unites the work of these services:
 * <ul>
 *  <li>{@link ReceiveService}</li>
 *  <li>{@link SendService}</li>
 *  <li>Not documented new ones (coming soon)</li>
 * </ul>
 */
public class MainService extends Service{
	
	/** The only instance of this class */
	private volatile static MainService singleton = new MainService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link MessageService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private MainService() {}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static MainService getInstance() {
		return singleton;
	}

	/**
	 * Starts all services if the {@link Config} is valid.
	 */
	@Override
	public void onStart() {
		Config.log();
		if(!Config.isValid()) L.f(this.getClass(), "Invalid server configuration! Use ServerConfig.setup() to fix");
		
		L.i(getClass(), "Launching subservices...");
		
		PublicKeyDBService.getInstance().launch();
		UserDatabaseService.getInstance().launch();
		SendService.getInstance().launch();
		ReceiveService.getInstance().launch();
		
		InactiveUserService.getInstance().launch();
		PingService.getInstance().launch();
		SignUpService.getInstance().launch();
		LoginService.getInstance().launch();
	}
	
	/**
	 * Stops all services.
	 */
	@Override
	public void onStop() {
		ReceiveService.getInstance().setStopRequested(true);
		SendService.getInstance().setStopRequested(true);
		
		SignUpService.getInstance().setStopRequested(true);
		LoginService.getInstance().setStopRequested(true);
		PingService.getInstance().setStopRequested(true);
		InactiveUserService.getInstance().setStopRequested(true);
		
		UserDatabaseService.getInstance().setStopRequested(true);
	}
}