package com.stamacoding.rsaApp.server.message.data;

import java.io.Serializable;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.rsa.RSA;
import com.stamacoding.rsaApp.rsa.keyCreate.Key;
import com.stamacoding.rsaApp.server.NetworkUtils;
import com.stamacoding.rsaApp.server.exceptions.InvalidValueException;
import com.stamacoding.rsaApp.server.exceptions.NullPointerException;

/**
 *  Stores essential information about a message. Should get encrypted before sending. The server should not be
 *  able to encrypt this data.
 */
public class ProtectedData implements Serializable{
	
	/** Auto-generated for serialization */
	private static final long serialVersionUID = -4421436516268769528L;

	/** The message as decoded string */
	private String textMessage;
	
	/** The time the message was originally created */
	private final long date;
	
	
	/**
	 * Creates an instance of the {@link ProtectedData} class.
	 *
	 * @param textMessage the message as decoded string
	 * @param date the time the message was originally created
	 */
	public ProtectedData(String textMessage, long date) {
		if(date < 0) Logger.error(this.getClass().getSimpleName(), new InvalidValueException(long.class, "date", date));
		
		this.date = date;
		setTextMessage(textMessage);
	}
	
	
	/**
	 * Gets the time the message was originally created.
	 * @return the time the message was originally created
	 */
	public long getDate() {
		return date;
	}
	
	/**
	 * Gets the message as decoded string.
	 * @return the message as decoded string
	 */
	public String getTextMessage() {
		return textMessage;
	}

	/**
	 * Sets the message.
	 * @param textMessage the message
	 */
	public void setTextMessage(String textMessage) {
		if(textMessage == null) Logger.error(this.getClass().getSimpleName(), new NullPointerException(String.class, "textMessage"));
		
		this.textMessage = textMessage;
	}
	
	/**
	 * Indicates if another object is equal to this one.
	 * @return whether another object is equal
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ProtectedData) {
			ProtectedData pd = (ProtectedData) obj;
			if(pd.getDate() != this.getDate()) return false;
			if(!pd.getTextMessage().equals(this.getTextMessage())) return false;
			return true;
		}
		return false;
	}


	/**
	 * Encrypts a {@link ProtectedData}-object.
	 * <b>Will be replaced by {@link RSA#encrypt(Object, Key)} very soon.</b>
	 * @param protectedData the object to encrypt
	 * @return the object as encrypted byte array
	 */
	public static byte[] encrypt(ProtectedData protectedData) {
		if(protectedData == null) Logger.error(ProtectedData.class.getSimpleName(), new NullPointerException(ProtectedData.class, "protectedData"));
		
		byte[] decodedProtectedData = NetworkUtils.serialize(protectedData);
		// TODO encrypt decoded data
		byte[] encryptedProtectedData = decodedProtectedData;
		return encryptedProtectedData;
	}
	
	/**
	 * Decrypts a {@link ProtectedData}-objects.
	 * <b>Will be replaced by {@link RSA#decrypt(Object, Key)} very soon.</b>
	 * @param encryptedProtectedData the object as encrypted byte array
	 * @return the decrypted object
	 */
	public static ProtectedData decrypt(byte[] encryptedProtectedData) {
		if(encryptedProtectedData == null) Logger.error(ProtectedData.class.getSimpleName(), new NullPointerException(byte[].class, "encryptedProtectedData"));
		
		// TODO decrypt encrypted data
		byte[] decryptedProtectedData = encryptedProtectedData;
		return (ProtectedData) NetworkUtils.deserialize(decryptedProtectedData);
	}
}
