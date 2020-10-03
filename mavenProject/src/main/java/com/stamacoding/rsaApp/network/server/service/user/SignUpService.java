package com.stamacoding.rsaApp.network.server.service.user;

import java.io.IOException;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.answerCodes.AnswerCodes;
import com.stamacoding.rsaApp.network.global.service.ServerSocketService;
import com.stamacoding.rsaApp.network.global.user.User;
import com.stamacoding.rsaApp.network.server.Server;
import com.stamacoding.rsaApp.network.server.Config;
import com.stamacoding.rsaApp.network.server.manager.UserManager;
import com.stamacoding.rsaApp.network.server.service.message.SendService;
import com.stamacoding.rsaApp.security.Security;

public class SignUpService extends ServerSocketService {
	
	/** The only instance of this class */
	private volatile static SignUpService singleton = new SignUpService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link SendService#getInstance()} to get the
	 *  only instance of this class.
	 *  The server's port is set to {@link Server#SEND_PORT}.
	 */
	private SignUpService() {
		super(Config.SIGNUP_PORT);
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
			L.t(getClass(), "Reading encrypted user data...");
			int length = getInputStream().readInt();
			if(length > 0) {
				
				byte[] encryptedUser = new byte[length];
				getInputStream().readFully(encryptedUser, 0, length);

				L.t(this.getClass(), "Decrypting client's user data...");
				User unregisteredUser = (User) Security.decryptF(encryptedUser);
				
				L.d(this.getClass(), "Client wants to register as: " + unregisteredUser.toString());
				
				if(UserDatabaseService.getInstance().isUsernameAvailable(unregisteredUser.getName())) {
					// Hash password
					unregisteredUser.getPassword().setSalt(Security.generatePasswordSalt(), false);
					
					UserManager.getInstance().add(unregisteredUser);

					L.i(this.getClass(), "Registered new user (0): " + unregisteredUser.toString());
					L.d(this.getClass(), "Currently registered users:\n" + UserDatabaseService.getInstance().toString());
					
					getOutputStream().writeInt(AnswerCodes.SignUp.SIGNED_UP);
				}else {
					L.d(this.getClass(), "Username is already in use!");
					
					L.t(getClass(), "Sending error code to client...");
					getOutputStream().writeInt(AnswerCodes.SignUp.USERNAME_UNAVAILABLE);
					L.t(getClass(), "Sent error code to client!");
				}

			}else {
				L.e(this.getClass(), "Received invalid data");
				
				L.t(getClass(), "Sending error code to client...");
				getOutputStream().writeInt(AnswerCodes.SignUp.INVALID_DATA_FROM_CLIENT);
				L.t(getClass(), "Sent error code to client!");
			}
		} catch (IOException e) {
			L.e(this.getClass(), "Connection error", e);
		}

	}
}
