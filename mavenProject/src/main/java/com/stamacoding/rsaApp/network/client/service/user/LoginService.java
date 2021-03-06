package com.stamacoding.rsaApp.network.client.service.user;

import java.io.IOException;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.Config;
import com.stamacoding.rsaApp.network.client.service.message.ReceiveService;
import com.stamacoding.rsaApp.network.client.service.message.SendService;
import com.stamacoding.rsaApp.network.global.answerCodes.AnswerCodes;
import com.stamacoding.rsaApp.network.global.service.ClientService;
import com.stamacoding.rsaApp.network.global.session.LoginState;
import com.stamacoding.rsaApp.network.global.session.Session;
import com.stamacoding.rsaApp.network.global.user.Password;
import com.stamacoding.rsaApp.network.global.user.User;
import com.stamacoding.rsaApp.security.rsa.RSA;

public class LoginService extends ClientService{

	/** The only instance of this class */
	private volatile static LoginService singleton = new LoginService();
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static LoginService getInstance() {
		return singleton;
	}
	
	protected LoginService() {
		super(Config.SERVER_IP, Config.LOGIN_PORT);
	}
	
	private volatile LoginState loginState;

	@Override
	protected void onAccept() {
		L.d(this.getClass(), "Trying to log in (" + Config.USER_NAME + ", ************)");
		setLoginState(login());
		
		if(getLoginState() == LoginState.LOGGED_IN) {
			L.i(this.getClass(), "Logged in successfull (" + Config.USER_NAME + ", ************)");
			L.d(getClass(), "Launching session, send and receive service...");
			SessionService.getInstance().launch();
			ReceiveService.getInstance().launch();
			SendService.getInstance().launch();
			
			this.setStopRequested(true);
		}else {
			L.w(this.getClass(), "Failed to log in (" + Config.USER_NAME + ", ************)");
			try {
				Thread.sleep(Config.RETRY_LOGIN_INTERVAL);
			} catch (InterruptedException e) {
				L.e(this.getClass(), "Failed to wait for retrying to sign up. The used thread was interrupted.", e);
			}
		}
	}
	
	private LoginState login() {
		try {
			L.d(this.getClass(), "Connected to server successfully");
			
			L.d(this.getClass(), "Encrypting user information (" + Config.USER_NAME + ", ************)");
			byte[] you = RSA.encryptF(new User(Config.USER_NAME, new Password(Config.USER_PASSWORD)));
			
			L.d(this.getClass(), "Sending user information");
			getOutputStream().writeInt(you.length);
			getOutputStream().write(you);
			getOutputStream().flush();
			
			L.d(this.getClass(), "Retrieving answer");
			int answer = getInputStream().readInt();
			L.t(getClass(), "Received: " + answer);
			
			switch(answer) {
			case AnswerCodes.LogIn.INVALID_DATA_FROM_CLIENT:
				L.e(this.getClass(), "Failed to log in! Server received invalid data from client.");
				return LoginState.SIGNED_IN;
			case AnswerCodes.LogIn.WRONG_USERNAME_PASSWORD:
				L.e(this.getClass(), "Failed to log in! Invalid password or username.");
				return LoginState.SIGNED_IN;
			case AnswerCodes.LogIn.ALREADY_LOGGED_IN:
				L.e(this.getClass(), "Failed to log in! There is already someone logged in with your username!");
				return LoginState.SIGNED_IN;
			case AnswerCodes.LogIn.LOGGED_IN:
				int encryptedSessionIdSize = getInputStream().readInt();
				if(encryptedSessionIdSize > 0) {
					byte[] encryptedSessionId = new byte[encryptedSessionIdSize];
					getInputStream().readFully(encryptedSessionId, 0, encryptedSessionIdSize);
					
					String sessionID = (String) RSA.decryptF(encryptedSessionId);
					L.t(getClass(), "Received and decrypted session id : " + sessionID);
		
					Session s = new Session(sessionID, LoginState.LOGGED_IN);
					L.d(getClass(), "Setting session service's session : " + s.toString());
					SessionService.getInstance().setSession(s);
					return LoginState.LOGGED_IN;
				}else {
					L.e(getClass(), "Received invalid data! Failed to receive session id!");
					return LoginState.SIGNED_IN;
				}

			}
		} catch (IOException e) {
			L.e(this.getClass(), "Connection error", e);
		}
		
		return LoginState.SIGNED_IN;
	}
	
	public LoginState getLoginState() {
		return loginState;
	}

	public void setLoginState(LoginState loginState) {
		this.loginState = loginState;
	}
}
