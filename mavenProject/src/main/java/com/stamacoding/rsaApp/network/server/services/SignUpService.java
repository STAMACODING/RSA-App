package com.stamacoding.rsaApp.network.server.services;

import java.io.IOException;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.user.User;
import com.stamacoding.rsaApp.network.server.Server;
import com.stamacoding.rsaApp.network.server.ServerConfig;
import com.stamacoding.rsaApp.network.server.managers.UserManager;
import com.stamacoding.rsaApp.security.Security;

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
		super(ServerConfig.SIGNUP_PORT);
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
				L.d(this.getClass(), "Decrypting client's request");
				byte[] encryptedUser = new byte[length];
				getInputStream().readFully(encryptedUser, 0, length);
				
				User unregisteredUser = (User) Security.decryptF(encryptedUser);
				L.d(this.getClass(), "Client wants to register as: " + unregisteredUser.toString());
				
				if(UserDatabaseService.getInstance().isUsernameAvailable(unregisteredUser.getName())) {
					// Hash password
					unregisteredUser.getPassword().setSalt(Security.generatePasswordSalt(), false);
					
					UserManager.getInstance().add(unregisteredUser);

					L.d(this.getClass(), "Registered new user (0): " + unregisteredUser.toString());
					L.d(this.getClass(), "Currently registered users:\n" + UserDatabaseService.getInstance().toString());
					
					getOutputStream().writeInt(AnswerCodes.SIGNED_UP);
				}else {
					L.d(this.getClass(), "Username is already in use!");
					getOutputStream().writeInt(AnswerCodes.USERNAME_UNAVAILABLE);
				}

			}else {
				L.e(this.getClass(), "Received invalid data");
				getOutputStream().writeInt(AnswerCodes.INVALID_DATA_FROM_CLIENT);
			}
		} catch (IOException e) {
			L.e(this.getClass(), "Connection error", e);
		}

	}
}
