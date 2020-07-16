package com.stamacoding.rsaApp.server.message.data;

import java.io.Serializable;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.rsa.RSA;
import com.stamacoding.rsaApp.rsa.keyCreate.Key;
import com.stamacoding.rsaApp.server.NetworkUtils;
import com.stamacoding.rsaApp.server.exceptions.InvalidValueException;
import com.stamacoding.rsaApp.server.exceptions.NullPointerException;

/**
 *  Stores server-relevant information about the message. Should get encrypted before sending. The
 *  server should be able to encrypt the message.
 */
public class ServerData implements Serializable {
	
	/** Auto-generated for serialization */
	private static final long serialVersionUID = -7534005873602518807L;

	/** The id of the client that sent, is sending or will send the message */
	private final byte sendingId;
	
	/** The id of the client that received or will receive this message */
	private final byte receivingId;
	
	
	/**
	 * Creates an instance of the {@link ServerData} class.
	 * @param sendingId the id of the client that sent, is sending or will send the message
	 * @param receivingId the id of the client that received or will receive this message
	 */
	public ServerData(byte sendingId, byte receivingId) {
		if(sendingId < 0) Logger.error(this.getClass().getSimpleName(), new InvalidValueException(byte.class, "sendingId", sendingId, "greater than -1"));
		if(receivingId < 0) Logger.error(this.getClass().getSimpleName(), new InvalidValueException(byte.class, "receivingId", receivingId, "greater than -1"));
		
		this.sendingId = sendingId;
		this.receivingId = receivingId;
	}
	

	/**
	 * Gets the id of the client that sent, is sending or will send this message.
	 * @return the id of the client that sent, is sending or will send this message
	 */
	public byte getSendingId() {
		return sendingId;
	}

	/**
	 * Gets the id of the client that received or will receive this message.
	 * @return the id of the client that received or will receive this message
	 */
	public byte getReceivingId() {
		return receivingId;
	}
	
	/**
	 * Indicates if another object is equal to this one.
	 * @return whether another object is equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o instanceof ServerData) {
			ServerData d2 = (ServerData) o;
			if(d2.getReceivingId() != this.getReceivingId()) return false;
			if(d2.getSendingId() != this.getSendingId()) return false;
			return true;
		}
		return false;
	}
	
	/**
	 * Encrypts a {@link ServerData}-object.
	 * <b>Will be replaced by {@link RSA#encrypt(Object, Key)} very soon.</b>
	 * @param serverData the object to encrypt
	 * @return the object as encrypted byte array
	 */
	public static byte[] encrypt(ServerData serverData) {
		if(serverData == null) Logger.error(ServerData.class.getSimpleName(), new NullPointerException(ServerData.class, "serverData"));
		
		byte[] decryptedServerData = NetworkUtils.serialize(serverData);
		// TODO encode decoded data
		byte[] encryptedServerData = decryptedServerData;
		return encryptedServerData;
	}
	
	/**
	 * Decrypts a {@link ServerData}-objects.
	 * <b>Will be replaced by {@link RSA#decrypt(Object, Key)} very soon.</b>
	 * @param encryptedServerData the object as encrypted byte array
	 * @return the decrypted object
	 */
	public static ServerData decrypt(byte[] encryptedServerData) {
		if(encryptedServerData == null) Logger.error(ServerData.class.getSimpleName(), new NullPointerException(byte[].class, "encryptedServerData"));
		
		// TODO decode encoded data
		byte[] decryptedServerData = encryptedServerData;
		return (ServerData) NetworkUtils.deserialize(decryptedServerData);
	}
}
