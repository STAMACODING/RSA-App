package com.stamacoding.rsaApp.server.server.services;

import java.io.IOException;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.rsa.RSA;
import com.stamacoding.rsaApp.server.global.user.User;
import com.stamacoding.rsaApp.server.server.Server;
import com.stamacoding.rsaApp.server.server.ServerConfig;
import com.stamacoding.rsaApp.server.server.managers.UserManager;

public class SignUpService extends ServerSocketService {
	public static class AnswerCodes{
		public final static int SIGNED_UP = 0;
		public final static int USERNAME_UNAVAILABLE = -1;
		public final static int INVALID_DATA_FROM_CLIENT = -2;
	}
	
	/** The only instance of this class */
	private volatile static SignUpService singleton = new SignUpService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ServerSendService#getInstance()} to get the
	 *  only instance of this class.
	 *  The server's port is set to {@link Server#SEND_PORT}.
	 */
	private SignUpService() {
		super(SignUpService.class.getSimpleName(), ServerConfig.SIGNUP_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static SignUpService getInstance() {
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
				
				User unregisteredUser = (User) RSA.decryptF(encryptedUser);
				Logger.debug(getServiceName(), "Client wants to register as: " + unregisteredUser.toString());
				
				if(UserDatabaseService.getInstance().isUsernameAvailable(unregisteredUser.getName())) {
					UserManager.getInstance().add(unregisteredUser);

					Logger.debug(getServiceName(), "Registered new user (0): " + unregisteredUser.toString());
					Logger.debug(getServiceName(), "Currently registered users:\n" + UserDatabaseService.getInstance().toString());
					
					getOutputStream().writeInt(AnswerCodes.SIGNED_UP);
				}else {
					Logger.debug(getServiceName(), "Username is already in use!");
					getOutputStream().writeInt(AnswerCodes.USERNAME_UNAVAILABLE);
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
