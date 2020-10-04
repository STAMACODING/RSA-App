package com.stamacoding.rsaApp.network.client.service.user;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.Config;
import com.stamacoding.rsaApp.network.global.answerCodes.AnswerCodes;
import com.stamacoding.rsaApp.network.global.service.ClientService;
import com.stamacoding.rsaApp.network.global.session.LoginState;
import com.stamacoding.rsaApp.network.global.user.Password;
import com.stamacoding.rsaApp.network.global.user.User;
import com.stamacoding.rsaApp.security.rsa.KeyPair;
import com.stamacoding.rsaApp.security.rsa.RSA;

public class SignUpService extends ClientService{
	/** The only instance of this class */
	private volatile static SignUpService singleton = new SignUpService();
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static SignUpService getInstance() {
		return singleton;
	}
	
	private volatile LoginState loginState;

	protected SignUpService() {
		super(Config.SERVER_IP, Config.SIGNUP_PORT);
	}

	@Override
	protected void onAccept() {
		L.d(this.getClass(), "Trying to sign up (" + Config.USER_NAME + ", ************)");
		setLoginState(signup());
		
		if(getLoginState() == LoginState.SIGNED_IN) {
			L.i(this.getClass(), "Signed up successfully (" + Config.USER_NAME + ", ************)");
			Config.REGISTERED = true;
			Config.save();
			
			L.d(getClass(), "Launching LoginService...");
			LoginService.getInstance().launch();
			
			this.setStopRequested(true);
		}else {
			L.w(this.getClass(), "Failed to sign up (" + Config.USER_NAME + ", ************)");
			try {
				Thread.sleep(Config.RETRY_SIGNUP_INTERVAL);
			} catch (InterruptedException e) {
				L.e(this.getClass(), "Failed to wait for retrying to sign up. The used thread was interrupted.", e);
			}
		}
	}
	
	private LoginState signup() {
		try {
			L.d(this.getClass(), "Connected to server successfully");
			
			L.d(this.getClass(), "Encrypting user information (" + Config.USER_NAME + ", ************)");
			byte[] you = RSA.encryptF(new User(Config.USER_NAME, new Password(Config.USER_PASSWORD)));
			
			L.d(this.getClass(), "Sending user information");
			getOutputStream().writeInt(you.length);
			getOutputStream().write(you);
			getOutputStream().flush();
			
			transferNewKeyPair();
			
			L.d(this.getClass(), "Retrieving answer");
			int answer = getInputStream().readInt();
			
			switch(answer) {
			case AnswerCodes.SignUp.FAILED_TO_STORE:
				L.e(this.getClass(), "Failed to sign up! Server database error!");
				return LoginState.NONE;
			case AnswerCodes.SignUp.USERNAME_UNAVAILABLE:
				L.e(this.getClass(), "Failed to sign up! Your username is already in use!");
				return LoginState.NONE;
			case AnswerCodes.SignUp.INVALID_DATA_FROM_CLIENT:
				L.e(this.getClass(), "Failed to sign up! Received invalid data from client!");
				return LoginState.NONE;
			case AnswerCodes.SignUp.SIGNED_UP:
				L.d(this.getClass(), "Signed-up successfully!");
				return LoginState.SIGNED_IN;
			}	
		} catch (IOException e) {
			L.e(this.getClass(), "Connection error", e);
		}
		
		return LoginState.NONE;
	}
	
	/**
	 * creates a RSA-KeyPair and stores the private key at /mavenProject/PrivateKey.txt
	 */
	private boolean transferNewKeyPair() {
		KeyPair clientKey = new KeyPair();
		L.d(this.getClass(), "Writing private key of client to 'PrivateKey.txt'");
		File dest = new File("PrivateKey.txt");
		if (!dest.exists())
			try {
				dest.createNewFile();
			} catch (IOException e) {
				L.e(this.getClass(), "Failed to store private key. Couldn't create private key file.", e);
				return false;
			}
		FileWriter destWrit = null;
		try {
			destWrit = new FileWriter(dest);
		} catch (IOException e) {
			L.e(this.getClass(), "Failed to store private key. Couldn't open private key file for writing.", e);
			return false;
		}
		int exp = clientKey.getPrivateKey().getExp();
		int mod = clientKey.getPrivateKey().getMod();
		try {
			destWrit.write("(" + exp + ", " + mod + ")");
		} catch (IOException e) {
			L.e(this.getClass(), "Failed to store private key. Couldn't write to private key file.", e);
			return false;
		}finally {
			try {
				destWrit.close();
			} catch (IOException e) {
				L.w(this.getClass(), "Failed to close private key file.");
			}
		}
		
		L.d(this.getClass(), "Transferring public key of client to server.");
		L.t(this.getClass(), "Encrypting public key to transfer to server...");
		
		byte[] publicKeyByte = RSA.encryptF(clientKey.getPublicKey());
		try {
			getOutputStream().writeInt(publicKeyByte.length);
			getOutputStream().write(publicKeyByte);
			getOutputStream().flush();
			L.d(this.getClass(), "Sent public key to server.");
			return true;
		} catch (IOException e) {
			L.e(this.getClass(), "Failed to transfer public key to server!");
			return false;
		}
	}

	public LoginState getLoginState() {
		return loginState;
	}

	public void setLoginState(LoginState loginState) {
		this.loginState = loginState;
	}

}
