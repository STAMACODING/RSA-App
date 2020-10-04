package com.stamacoding.rsaApp.network.client.service.user;

import java.io.IOException;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.Config;
import com.stamacoding.rsaApp.network.client.service.message.ChatDatabaseService;
import com.stamacoding.rsaApp.network.global.answerCodes.AnswerCodes;
import com.stamacoding.rsaApp.network.global.service.ClientService;
import com.stamacoding.rsaApp.network.global.session.LoginState;
import com.stamacoding.rsaApp.network.global.session.Session;
import com.stamacoding.rsaApp.security.rsa.RSA;

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
				L.d(this.getClass(), "Active session: " + getSession().toString());
				
				L.t(getClass(), "Encrypting session id...");
				byte[] encryptedSessionId = RSA.encryptF(getSession().getId());
				getOutputStream().writeInt(encryptedSessionId.length);
				getOutputStream().write(encryptedSessionId);
				getOutputStream().flush();
				L.t(getClass(), "Sent encrypted session id!");
				
				int answer = getInputStream().readInt();
				switch(answer) {
				case AnswerCodes.Ping.LOGGED_OUT:
					L.e(getClass(), "Unexspected but true: You are logged out!");
					getSession().setState(LoginState.SIGNED_IN);
					
					L.i(getClass(), "Launching login service to login again...");
					LoginService.getInstance().setLoginState(LoginState.SIGNED_IN);
					LoginService.getInstance().launch();
					
					L.d(getClass(), "Stopping session service...");
					setStopRequested(true);
					break;
				case AnswerCodes.Ping.STILL_LOGGED_IN:
					L.t(getClass(), "Still logged in :)");
					break;
				}
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

