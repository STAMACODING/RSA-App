package com.stamacoding.rsaApp.network.server.services;

import java.io.IOException;

import com.stamacoding.rsaApp.network.client.ClientConfig;
import com.stamacoding.rsaApp.network.client.services.ClientReceiveService;
import com.stamacoding.rsaApp.network.server.ServerConfig;
import com.stamacoding.rsaApp.security.Security;
import com.stamacoding.rsaApp.security.rsa.Key;
import com.sun.media.jfxmedia.logging.Logger;

public class KeyTransferService extends ServerSocketService {

	/** The only instance of this class */
	private volatile static KeyTransferService singleton = new KeyTransferService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private KeyTransferService() {
		super(KeyTransferService.class.getSimpleName(), ServerConfig.LOGIN_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static KeyTransferService getInstance() {
		return singleton;
	}
	
	byte[] publicKey = null;

	@Override
	protected void onAccept() {
		try {
			int length = getInputStream().readInt();
			publicKey = new byte[length];
			getInputStream().readFully(publicKey, 0, length);
			
			Key publicKeyClient = (Key) Security.decryptF(publicKey);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
