package com.stamacoding.rsaApp.network.client.services;

import java.io.IOException;

import com.stamacoding.rsaApp.network.client.ClientConfig;
import com.stamacoding.rsaApp.security.Security;
import com.stamacoding.rsaApp.security.rsa.Key;

public class KeyTransferService extends ClientSocketService {

	/** The only instance of this class */
	private volatile static KeyTransferService singleton = new KeyTransferService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private KeyTransferService() {
		super(KeyTransferService.class.getSimpleName(), ClientConfig.SERVER_IP, ClientConfig.RECEIVE_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static KeyTransferService getInstance() {
		return singleton;
	}
	
	
	private Key publicKey = null;
	
	public Key getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(Key publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	protected void onAccept() {
		byte[] publicKeyByte = Security.encryptF(publicKey);
		try {
			getOutputStream().writeInt(publicKeyByte.length);
			getOutputStream().write(publicKeyByte);
			setStopRequested(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
