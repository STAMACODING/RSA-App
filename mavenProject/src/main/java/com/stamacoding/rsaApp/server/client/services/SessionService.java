package com.stamacoding.rsaApp.server.client.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.rsa.RSA;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.client.ClientConfig;
import com.stamacoding.rsaApp.server.session.LoginState;
import com.stamacoding.rsaApp.server.user.User;
import com.stamacoding.rsaApp.server.session.Session;

// TODO ServerServices (Login, Signup, Ping) needed, documentation needed, Config should be edited
public class SessionService extends Service{
	private final Session session = new Session(-1);

	/** The only instance of this class */
	private volatile static SessionService singleton = new SessionService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ChatDatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private SessionService() {
		super(SessionService.class.getSimpleName());
		if(ClientConfig.REGISTERED) getSession().setState(LoginState.SIGNED_IN);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static SessionService getInstance() {
		return singleton;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		while(getSession().getState() == LoginState.NONE) {
			Logger.debug(this.getClass().getSimpleName(), "Trying to sign up (" + ClientConfig.USER_NAME + ", " + ClientConfig.USER_PASSWORD + ")");
			getSession().setState(signup());
			
			if(getSession().getState() == LoginState.SIGNED_IN) {
				Logger.debug(this.getClass().getSimpleName(), "Signed up successfull (" + ClientConfig.USER_NAME + ", " + ClientConfig.USER_PASSWORD + ")");
			}else {
				Logger.warning(this.getClass().getSimpleName(), "Failed to sign up (" + ClientConfig.USER_NAME + ", " + ClientConfig.USER_PASSWORD + ")");
				try {
					Thread.sleep(ClientConfig.RETRY_SIGNUP_INTERVAL);
				} catch (InterruptedException e) {
					Logger.error(this.getClass().getSimpleName(), "Failed to wait for retrying to sign up. The used thread interrupted.");
				}
			}
		}
		
		while(getSession().getState() != LoginState.LOGGED_IN) {
			Logger.debug(this.getClass().getSimpleName(), "Trying to log in (" + ClientConfig.USER_NAME + ", " + ClientConfig.USER_PASSWORD + ")");
			getSession().setState(login());
			
			if(getSession().getState() == LoginState.LOGGED_IN) {
				Logger.debug(this.getClass().getSimpleName(), "Logged in successfull (" + ClientConfig.USER_NAME + ", " + ClientConfig.USER_PASSWORD + ")");
			}else {
				Logger.warning(this.getClass().getSimpleName(), "Failed to log in (" + ClientConfig.USER_NAME + ", " + ClientConfig.USER_PASSWORD + ")");
				try {
					Thread.sleep(ClientConfig.RETRY_LOGIN_INTERVAL);
				} catch (InterruptedException e) {
					Logger.error(this.getClass().getSimpleName(), "Failed to wait for retrying to sign up. The used thread interrupted.");
				}
			}
		}
	}
	
	@Override
	public void onRepeat() {
		super.onRepeat();
		ping();
	}

	private void ping() {
		try {
			Socket connectionToServer = new Socket(ClientConfig.SERVER_IP, ClientConfig.PING_PORT);
			
			Logger.debug(this.getClass().getSimpleName(), "Ping");
			DataOutputStream out = new DataOutputStream(connectionToServer.getOutputStream());
			out.writeLong(getSession().getId());
			out.close();
			
			connectionToServer.close();
			
			try {
				Thread.sleep(ClientConfig.PING_INTERVAL);
			} catch (InterruptedException e) {
				Logger.error(this.getClass().getSimpleName(), "Failed to wait for pinging again.");
			}
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Connection error");
		}
	}

	private LoginState signup() {
		try {
			Socket connectionToServer = new Socket(ClientConfig.SERVER_IP, ClientConfig.SIGNUP_PORT);
			Logger.debug(this.getClass().getSimpleName(), "Connected to server successfully");
			
			Logger.debug(this.getClass().getSimpleName(), "Encrypting user information (" + ClientConfig.USER_NAME + ", " + ClientConfig.USER_PASSWORD + ")");
			byte[] you = RSA.encryptF(new User(ClientConfig.USER_NAME, ClientConfig.USER_PASSWORD));
			
			DataOutputStream out = new DataOutputStream(connectionToServer.getOutputStream());
			DataInputStream in = new DataInputStream(connectionToServer.getInputStream());
			
			Logger.debug(this.getClass().getSimpleName(), "Sending user information");
			out.writeInt(you.length);
			out.write(you);
			out.flush();
			
			Logger.debug(this.getClass().getSimpleName(), "Retrieving answer");
			int answer = in.readInt();
			
			Logger.debug(this.getClass().getSimpleName(), "Close connection to server");
			out.close();
			in.close();
			connectionToServer.close();
			
			switch(answer) {
			case -3:
				Logger.error(this.getClass().getSimpleName(), "Failed to sign up! Your password is too short! (-3)");
				return LoginState.NONE;
			case -2:
				Logger.error(this.getClass().getSimpleName(), "Failed to sign up! Your username is too short! (-2)");
				return LoginState.NONE;
			case -1:
				Logger.error(this.getClass().getSimpleName(), "Failed to sign up! Your username is already in use! (-1)");
				return LoginState.NONE;
			}
			return LoginState.SIGNED_IN;
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Connection error");
		}
		
		return LoginState.NONE;
	}
	
	private LoginState login() {
		try {
			Socket connectionToServer = new Socket(ClientConfig.SERVER_IP, ClientConfig.LOGIN_PORT);
			Logger.debug(this.getClass().getSimpleName(), "Connected to server successfully");
			
			Logger.debug(this.getClass().getSimpleName(), "Encrypting user information (" + ClientConfig.USER_NAME + ", " + ClientConfig.USER_PASSWORD + ")");
			byte[] you = RSA.encryptF(new User(ClientConfig.USER_NAME, ClientConfig.USER_PASSWORD));
			
			DataOutputStream out = new DataOutputStream(connectionToServer.getOutputStream());
			DataInputStream in = new DataInputStream(connectionToServer.getInputStream());
			
			Logger.debug(this.getClass().getSimpleName(), "Sending user information");
			out.writeInt(you.length);
			out.write(you);
			out.flush();
			
			Logger.debug(this.getClass().getSimpleName(), "Retrieving answer");
			long answer = in.readLong();
			
			Logger.debug(this.getClass().getSimpleName(), "Close connection to server");
			out.close();
			in.close();
			connectionToServer.close();
			
			if(answer == -1L) {
				Logger.error(this.getClass().getSimpleName(), "Failed to log in! Server returned -1.");
				return LoginState.SIGNED_IN;
			}else {
				getSession().setId(answer);
			}
			return LoginState.LOGGED_IN;
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Connection error");
		}
		
		return LoginState.SIGNED_IN;
	}

	private Session getSession() {
		return session;
	}
}

