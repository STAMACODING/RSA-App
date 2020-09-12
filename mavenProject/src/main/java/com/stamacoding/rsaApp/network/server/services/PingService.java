package com.stamacoding.rsaApp.network.server.services;

import java.io.IOException;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.network.server.ServerConfig;

public class PingService extends ServerSocketService{

	
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
	public void onAccept() {
		try {
			long sessionId = getInputStream().readLong();
			// TODO process sessionId
		} catch (IOException e) {
			Logger.error(this.getClass().getSimpleName(), "Connection error");
		}

	}
}
