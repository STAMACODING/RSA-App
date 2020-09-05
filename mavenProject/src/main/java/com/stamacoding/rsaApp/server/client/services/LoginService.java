package com.stamacoding.rsaApp.server.client.services;

import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.session.Session;

public class LoginService extends Service{
	private boolean loggedIn = false;
	private Session session = null;
	
	/** The only instance of this class */
	private volatile static LoginService singleton = new LoginService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link DatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private LoginService() {
		super(LoginService.class.getSimpleName());
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static LoginService getInstance() {
		return singleton;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
	}
}
