package com.stamacoding.rsaApp.network.client.service.user;

import java.io.IOException;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.Config;
import com.stamacoding.rsaApp.network.client.service.message.ChatDatabaseService;
import com.stamacoding.rsaApp.network.global.service.ClientService;
import com.stamacoding.rsaApp.network.global.session.LoginState;
import com.stamacoding.rsaApp.network.global.session.Session;

public class SessionService extends ClientService{
	/** The only instance of this class */
	private volatile static SessionService singleton = new SessionService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ChatDatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private SessionService() {
		super(Config.SERVER_IP, Config.PING_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static SessionService getInstance() {
		return singleton;
	}
	
	private volatile Session session = null;
	
	@Override
	public void onAccept() {
		if(getSession() == null || getSession().getState() != LoginState.LOGGED_IN) {
			L.w(getClass(), "Not logged in. Cannot ping sth. to the server!");
		}else {
			try {
				L.d(this.getClass(), "Pinging");
				getOutputStream().writeLong(getSession().getId());
			} catch (IOException e) {
				L.e(this.getClass(), "Connection error", e);
			}
		}
		
		try {
			Thread.sleep(Config.PING_INTERVAL);
		} catch (InterruptedException e) {
			L.e(this.getClass(), "Failed to wait for pinging.", e);
		}
	}

	public Session getSession() {
		return session;
	}
	
	void setSession(Session s) {
		this.session = s;
	}
}

