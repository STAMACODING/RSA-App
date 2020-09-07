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

public class PingService extends ServerService{

	
	/** The only instance of this class */
	private volatile static PingService singleton = new PingService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ServerSendService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private PingService() {
		super(PingService.class.getSimpleName(), ServerConfig.PING_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static PingService getInstance() {
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
			
			long sessionId = in.readLong();
			// TODO process sessionId
			
			in.close();
			connectionFromClient.close();
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Connection error");
		}

	}
}
