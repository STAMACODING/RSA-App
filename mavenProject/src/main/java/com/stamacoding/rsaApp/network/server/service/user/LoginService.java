package com.stamacoding.rsaApp.network.server.service.user;

import java.io.IOException;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.answerCodes.AnswerCodes;
import com.stamacoding.rsaApp.network.global.service.ServerService;
import com.stamacoding.rsaApp.network.global.user.User;
import com.stamacoding.rsaApp.network.server.Config;
import com.stamacoding.rsaApp.network.server.Server;
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
				
				if(UserDatabaseService.getInstance().isPasswordCorrect(user)) {
					User loggedIn = UserDatabaseService.getInstance().getUser(user.getName());
					L.i(this.getClass(), "User successfully logged in: " + loggedIn.toString());
					
					// TODO store session id and mark user as logged in
					long sessionId = (long) (Math.random() * Long.MAX_VALUE);
					
					L.t(this.getClass(), "Sending session id to user: " + loggedIn.toString());
					getOutputStream().writeLong(sessionId);
					L.t(this.getClass(), "Sent session id to user: " + loggedIn.toString());
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
