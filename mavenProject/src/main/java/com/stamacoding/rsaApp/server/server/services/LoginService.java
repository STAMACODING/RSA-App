package com.stamacoding.rsaApp.server.server.services;

import java.io.IOException;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.rsa.RSA;
import com.stamacoding.rsaApp.server.global.user.User;
import com.stamacoding.rsaApp.server.server.Server;
import com.stamacoding.rsaApp.server.server.ServerConfig;

public class LoginService extends ServerSocketService{
	public static class AnswerCodes{
		public final static int WRONG_USERNAME_PASSWORD = -1;
		public final static int INVALID_DATA_FROM_CLIENT = -2;
	}
	
	/** The only instance of this class */
	private volatile static LoginService singleton = new LoginService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ServerSendService#getInstance()} to get the
	 *  only instance of this class.
	 *  The server's port is set to {@link Server#SEND_PORT}.
	 */
	private LoginService() {
		super(LoginService.class.getSimpleName(), ServerConfig.LOGIN_PORT);
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
				Logger.debug(getServiceName(), "Decrypting client's request");
				byte[] encryptedUser = new byte[length];
				getInputStream().readFully(encryptedUser, 0, length);
				
				User user = (User) RSA.decryptF(encryptedUser);
				Logger.debug(getServiceName(), "Client wants to login as: " + user.toString());
				
				if(UserDatabaseService.getInstance().isPasswordCorrect(user.getName(), user.getPassword())) {
					Logger.debug(getServiceName(), "User logged in (0): " + UserDatabaseService.getInstance().getUser(user.getName()).toString());
					
					// TODO store session id and mark user as logged in
					long sessionId = (long) (Math.random() * Long.MAX_VALUE);
					
					getOutputStream().writeLong(sessionId);
				}else {
					Logger.debug(getServiceName(), "Wrong username/password!");
					getOutputStream().writeInt(AnswerCodes.WRONG_USERNAME_PASSWORD);
				}

			}else {
				Logger.error(this.getClass().getSimpleName(), new RuntimeException("Received invalid data"));
				getOutputStream().writeInt(AnswerCodes.INVALID_DATA_FROM_CLIENT);
			}
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Connection error");
		}

	}
}
