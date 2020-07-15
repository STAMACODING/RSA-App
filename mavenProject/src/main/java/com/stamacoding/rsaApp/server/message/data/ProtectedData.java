package com.stamacoding.rsaApp.server.message.data;

import java.io.Serializable;

import com.stamacoding.rsaApp.rsa.RSA;
import com.stamacoding.rsaApp.rsa.keyCreate.Key;
import com.stamacoding.rsaApp.server.NetworkUtils;

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
		this.textMessage = textMessage;
	}
	
	/**
	 * Encrypts a {@link ProtectedData}-object.
	 * <b>Will be replaced by {@link RSA#encrypt(Object, Key)} very soon.</b>
	 * @param protectedData the object to encrypt
	 * @return the object as encrypted byte array
	 */
	public static byte[] encrypt(ProtectedData protectedData) {
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
		// TODO decrypt encrypted data
		byte[] decryptedProtectedData = encryptedProtectedData;
		return (ProtectedData) NetworkUtils.deserialize(decryptedProtectedData);
	}
}
