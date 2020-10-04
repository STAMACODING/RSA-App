package com.stamacoding.rsaApp.network.server.service.user;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.server.manager.SessionManager;
import com.stamacoding.rsaApp.network.server.service.message.SendService;

public class InactiveUserService extends Service{
	/** The only instance of this class */
	private volatile static InactiveUserService singleton = new InactiveUserService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link SendService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private InactiveUserService() {
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static InactiveUserService getInstance() {
		return singleton;
	}
	
	@Override
	public void onRepeat() {
		String user = SessionManager.getInstance().getInactiveUser();
		if(user != null) {
			L.d(getClass(), "Found inactive user: " + user);
			SessionManager.getInstance().logOut(user);
			L.d(getClass(), "Inactive user logged out successfully: " + user);
		}
	}
}
