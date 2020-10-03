package com.stamacoding.rsaApp.network.client.service.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.Config;
import com.stamacoding.rsaApp.network.global.answerCodes.AnswerCodes;
import com.stamacoding.rsaApp.network.global.service.ClientSocketService;
import com.stamacoding.rsaApp.network.global.session.LoginState;
import com.stamacoding.rsaApp.network.global.session.Session;
import com.stamacoding.rsaApp.network.global.user.Password;
import com.stamacoding.rsaApp.network.global.user.User;
import com.stamacoding.rsaApp.security.Security;

public class LoginService extends ClientSocketService{

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
	
	private LoginState loginState;

	@Override
	protected void onAccept() {
		L.d(this.getClass(), "Trying to log in (" + Config.USER_NAME + ", ************)");
		setLoginState(login());
		
		if(getLoginState() == LoginState.LOGGED_IN) {
			L.i(this.getClass(), "Logged in successfull (" + Config.USER_NAME + ", ************)");
			L.d(getClass(), "Launching Session service...");
			SessionService.getInstance().launch();
			
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
			byte[] you = Security.encryptF(new User(Config.USER_NAME, new Password(Config.USER_PASSWORD)));
			
			L.d(this.getClass(), "Sending user information");
			getOutputStream().writeInt(you.length);
			getOutputStream().write(you);
			getOutputStream().flush();
			
			L.d(this.getClass(), "Retrieving answer");
			long answer = getInputStream().readLong();
			L.t(getClass(), "Received: " + answer);
			
			if(answer < 0) {
				int answerCode = (int) answer;
				switch(answerCode) {
				case AnswerCodes.LogIn.INVALID_DATA_FROM_CLIENT:
					L.e(this.getClass(), "Failed to log in! Server received invalid data from client.");
					return LoginState.SIGNED_IN;
				case AnswerCodes.LogIn.WRONG_USERNAME_PASSWORD:
					L.e(this.getClass(), "Failed to log in! Invalid password or username.");
					return LoginState.SIGNED_IN;
				}
			}else {
				Session s = new Session(answer, LoginState.LOGGED_IN);
				L.d(getClass(), "Setting session service's session : " + s.toString());
				SessionService.getInstance().setSession(s);
				return LoginState.LOGGED_IN;
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
