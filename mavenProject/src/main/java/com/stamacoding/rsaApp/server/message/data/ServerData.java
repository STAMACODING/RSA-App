package com.stamacoding.rsaApp.server.message.data;

import java.io.Serializable;

import com.stamacoding.rsaApp.log.logger.Logger;

/**
 *  Stores server-relevant information about the message. Should get encrypted before sending. The
 *  server should be able to encrypt the message.
 */
public class ServerData implements Serializable {
	
	/** Auto-generated for serialization */
	private static final long serialVersionUID = -7534005873602518807L;

	/** The id of the client that sent, is sending or will send the message */
	private final String sendingUser;
	
	/** The id of the client that received or will receive this message */
	private final String receivingUser;
	
	
	/**
	 * Creates an instance of the {@link ServerData} class.
	 * @param sendingId the id of the client that sent, is sending or will send the message
	 * @param receivingId the id of the client that received or will receive this message
	 */
	public ServerData(String sendingUser, String receivingUser) {
		if(sendingUser == null || sendingUser.length() == 0) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("Invalid username (sending)!"));
		if(receivingUser == null || receivingUser.length() == 0) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("Invalid username (receiving)!"));
		
		this.sendingUser = sendingUser;
		this.receivingUser = receivingUser;
	}
	

	/**
	 * Gets the username of the client that sent, is sending or will send this message.
	 * @return the username of the client that sent, is sending or will send this message
	 */
	public String getSending() {
		return sendingUser;
	}

	/**
	 * Gets the username of the client that received or will receive this message.
	 * @return the username of the client that received or will receive this message
	 */
	public String getReceiving() {
		return receivingUser;
	}
	
	/**
	 * Indicates if another object is equal to this one.
	 * @return whether another object is equal
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof ServerData) {
			ServerData d2 = (ServerData) o;
			if(!d2.getReceiving().equals(this.getReceiving())) return false;
			if(!d2.getSending().equals(this.getSending())) return false;
			return true;
		}
		return false;
	}
	
	public ServerData clone() {
		return new ServerData(getSending(), getReceiving());
	}
}
