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
	private final byte sendingId;
	
	/** The id of the client that received or will receive this message */
	private final byte receivingId;
	
	
	/**
	 * Creates an instance of the {@link ServerData} class.
	 * @param sendingId the id of the client that sent, is sending or will send the message
	 * @param receivingId the id of the client that received or will receive this message
	 */
	public ServerData(byte sendingId, byte receivingId) {
		if(sendingId < 0) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("byte sendingId (" + sendingId +  ") should be greater than -1 !"));
		if(receivingId < 0) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("byte receivingId (" + receivingId +  ") should be greater than -1 !"));
		
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
}
