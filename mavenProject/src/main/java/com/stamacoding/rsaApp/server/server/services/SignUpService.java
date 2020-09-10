package com.stamacoding.rsaApp.server.server.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.rsa.RSA;
import com.stamacoding.rsaApp.server.server.Server;
import com.stamacoding.rsaApp.server.server.ServerConfig;
import com.stamacoding.rsaApp.server.server.managers.UserManager;
import com.stamacoding.rsaApp.server.user.User;

public class SignUpService extends ServerService {
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
	public void onRepeat() {
		super.onRepeat();

		try {
			Socket connectionFromClient = getServerSocket().accept();
			connectionFromClient.setSoTimeout(5000);
			Logger.debug(getServiceName(), "Accepted connection from client");
			
			DataInputStream in = new DataInputStream(connectionFromClient.getInputStream());
			
			int length = in.readInt();
			if(length > 0) {
				Logger.debug(getServiceName(), "Decrypting client's request");
				byte[] encryptedUser = new byte[length];
				in.readFully(encryptedUser, 0, length);
				
				User unregisteredUser = (User) RSA.decryptF(encryptedUser);
				Logger.debug(getServiceName(), "Client wants to register as: " + unregisteredUser.toString());
				
				DataOutputStream out = new DataOutputStream(connectionFromClient.getOutputStream());
				if(UserDatabaseService.getInstance().isUsernameAvailable(unregisteredUser.getName())) {
					UserManager.getInstance().add(unregisteredUser);

					Logger.debug(getServiceName(), "Registered new user (0): " + unregisteredUser.toString());
					out.writeInt(0);
				}else {
					Logger.debug(getServiceName(), "Username is already in use! (-1)");
					out.writeInt(-1);
				}
				out.flush();
				out.close();
			}else {
				Logger.error(this.getClass().getSimpleName(), new RuntimeException("Received invalid data"));
			}
			in.close();
			connectionFromClient.close();
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Connection error");
		}

	}
}
