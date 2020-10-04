package com.stamacoding.rsaApp.network.server.service.user;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.answerCodes.AnswerCodes;
import com.stamacoding.rsaApp.network.global.service.ServerService;
import com.stamacoding.rsaApp.network.global.user.User;
import com.stamacoding.rsaApp.network.server.Config;
import com.stamacoding.rsaApp.network.server.Server;
import com.stamacoding.rsaApp.network.server.manager.SessionManager;
import com.stamacoding.rsaApp.network.server.service.database.UserDatabaseService;
import com.stamacoding.rsaApp.network.server.service.message.SendService;
import com.stamacoding.rsaApp.security.rsa.RSA;

public class LoginService extends ServerService{
	
	/** The only instance of this class */
	private volatile static LoginService singleton = new LoginService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link SendService#getInstance()} to get the
	 *  only instance of this class.
	 *  The server's port is set to {@link Server#SEND_PORT}.
	 */
	private LoginService() {
		super(Config.LOGIN_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static LoginService getInstance() {
		return singleton;
	}
	
	@Override
	public void onAccept() {
		try {
			
			int length = getInputStream().readInt();
			if(length > 0) {
				L.d(this.getClass(), "Decrypting client's request");
				byte[] encryptedUser = new byte[length];
				getInputStream().readFully(encryptedUser, 0, length);
				
				User user = (User) RSA.decryptF(encryptedUser);
				L.d(this.getClass(), "Client wants to login as: " + user.toString());
				
				boolean passwordCorrect = (boolean) UserDatabaseService.getInstance().executeAndWait(new Callable<Object>() {
					
					@Override
					public Boolean call() throws Exception {
						return UserDatabaseService.getInstance().isPasswordCorrect(user);
					}
				});
				if(passwordCorrect) {
					
					String sessionId = SessionManager.getInstance().logIn(user.getName());
					
					if(sessionId != null) {
						L.i(this.getClass(), "User successfully logged in: " + user.getName());
						
						L.t(this.getClass(), "Sending success answer code to user: " + user.getName());
						getOutputStream().writeInt(AnswerCodes.LogIn.LOGGED_IN);
						L.t(this.getClass(), "Sent success answer code to user: " + user.getName());
						
						byte[] encryptedSessionId = RSA.encryptF(sessionId);
						L.t(this.getClass(), "Sending encrypted session id(" + sessionId + ") to user: " + user.getName());
						getOutputStream().writeInt(encryptedSessionId.length);
						getOutputStream().write(encryptedSessionId);
						getOutputStream().flush();
						L.t(this.getClass(), "Sent encrypted session id to user: " + user.getName());
					}else {
						L.i(this.getClass(), "Already logged in: " + user.getName());
					}
				}else {
					L.w(this.getClass(), "Wrong username/password!: " + user.toString());
					getOutputStream().writeInt(AnswerCodes.LogIn.WRONG_USERNAME_PASSWORD);
				}

			}else {
				L.e(this.getClass(), "Received invalid data");
				getOutputStream().writeInt(AnswerCodes.LogIn.INVALID_DATA_FROM_CLIENT);
			}
		} catch (IOException e) {
			L.e(this.getClass(), "Connection error", e);
		}

	}
}
