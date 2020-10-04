package com.stamacoding.rsaApp.network.server.service.user;

import java.io.IOException;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.answerCodes.AnswerCodes;
import com.stamacoding.rsaApp.network.global.service.ServerService;
import com.stamacoding.rsaApp.network.server.Config;
import com.stamacoding.rsaApp.network.server.manager.SessionManager;
import com.stamacoding.rsaApp.network.server.service.message.SendService;

public class PingService extends ServerService{
	/** The only instance of this class */
	private volatile static PingService singleton = new PingService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link SendService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private PingService() {
		super(Config.PING_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static PingService getInstance() {
		return singleton;
	}
	
	@Override
	public void onAccept() {
		try {
			String sessionId = getInputStream().readUTF();
			L.t(getClass(), "Received session id from client (" + sessionId + ")");
			
			String username = SessionManager.getInstance().getUsername(sessionId);
			if(username != null) {
				SessionManager.getInstance().ping(username);
				L.d(getClass(), "User \"" + username + "\" pinged!");
				getOutputStream().writeInt(AnswerCodes.Ping.STILL_LOGGED_IN);
			}else {
				L.e(getClass(), "Didn't find any logged in user with the specified session id!");
				getOutputStream().writeInt(AnswerCodes.Ping.LOGGED_OUT);
			}
		} catch (IOException e) {
			L.e(this.getClass(), "Connection error", e);
		}

	}
}
