package com.stamacoding.rsaApp.network.global.message.data;

import java.io.Serializable;

import com.stamacoding.rsaApp.logger.L;

/**
 *  Stores server-relevant information about the message. At the moment this covers the <b>user names of the sender and receiver</b>. 
 *  Should get encrypted before sending. The
 *  <b>server should be able to encrypt</b> the message.
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
		if(sendingUser == null || sendingUser.length() == 0 || sendingUser.length() > 15) L.f(this.getClass(), new IllegalArgumentException("Invalid username (sending)!"));
		if(receivingUser == null || receivingUser.length() == 0 || receivingUser.length() > 15) L.f(this.getClass(), new IllegalArgumentException("Invalid username (receiving)!"));
		
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((receivingUser == null) ? 0 : receivingUser.hashCode());
		result = prime * result + ((sendingUser == null) ? 0 : sendingUser.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		ServerData other = (ServerData) obj;
		if (receivingUser == null) {
			if (other.receivingUser != null)
				return false;
		} else if (!receivingUser.equals(other.receivingUser))
			return false;
		if (sendingUser == null) {
			if (other.sendingUser != null)
				return false;
		} else if (!sendingUser.equals(other.sendingUser))
			return false;
		return true;
	}
	
	public ServerData clone() {
		return new ServerData(getSending(), getReceiving());
	}
}
