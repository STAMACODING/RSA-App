package com.stamacoding.rsaApp.network.server.manager;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.service.message.ReceiveService;
import com.stamacoding.rsaApp.network.global.service.executor.IndexedMap;
import com.stamacoding.rsaApp.network.global.session.TimedSessionId;
import com.stamacoding.rsaApp.security.sessionId.SessionId;

public class SessionManager {
	private final static long MAX_INACTIVITY_TIME = 20 * 1000;
	/** The only instance of this class */
	private volatile static SessionManager singleton = new SessionManager();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private SessionManager() {
		
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static SessionManager getInstance() {
		return singleton;
	}
	
	private final IndexedMap<String, TimedSessionId> loggedInUsers = new IndexedMap<String, TimedSessionId>();
	
	public String logIn(String username) {
		if(isLoggedIn(username)) {
			L.e(getClass(), "User \"" + username +"\" is already logged in!");
			return null;
		}else {
			String sessionID = SessionId.generateSessionId();
			getLoggedInUsers().put(username, new TimedSessionId(sessionID, System.currentTimeMillis()));
			L.t(getClass(), "User \"" + username +"\" logged in!");
			return sessionID;
		}
	}
	
	public boolean ping(String username) {
		if(isLoggedIn(username)) {
			getLoggedInUsers().getValue(username).setTime(System.currentTimeMillis());
			return true;
		}else {
			L.e(getClass(), "Not logged in user cannot ping!");
			return false;
		}
	}
	
	public boolean logOut(String username) {
		if(isLoggedIn(username)) {
			getLoggedInUsers().remove(username);
			L.i(getClass(), "User \"" + username + "\" logged out!");
			return true;
		}else {
			L.e(getClass(), "Cannot log out user that is not logged in!");
			return false;
		}
	}
	
	public String getUsername(String sessionId) {
		if(getLoggedInUsers().containsValue(new TimedSessionId(sessionId, 0))) {
			return getLoggedInUsers().getKeys(new TimedSessionId(sessionId, 0)).get(0);
		}else {
			L.w(getClass(), "Didn't find any username related to the specified session id (" + sessionId + ")!");
			return null;
		}
	}

	private IndexedMap<String, TimedSessionId> getLoggedInUsers() {
		return loggedInUsers;
	}
	
	public boolean isLoggedIn(String username) {
		return getLoggedInUsers().contains(username);
	}
	
	public boolean isValidId(String sessionId) {
		return getLoggedInUsers().containsValue(new TimedSessionId(sessionId, 0));
	}
	
	public String getInactiveUser() {
		for(int i=0; i<getLoggedInUsers().size(); i++) {
			if(getLoggedInUsers().getValue(i).getInactivityTime() > MAX_INACTIVITY_TIME) return getLoggedInUsers().getKey(i);
		}
		return null;
	}
}
