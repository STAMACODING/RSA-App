package com.stamacoding.rsaApp.network.global.message.data;

import java.io.Serializable;

import com.stamacoding.rsaApp.logger.L;

/**
 * Stores information about a message that is relevant to the client only. This information is not sent to the server and is also not encrypted.
 */
public class LocalData implements Serializable{
	
	/** Auto-generated for serialization */
	private static final long serialVersionUID = -2576893616665222300L;

	/** The message's unique id in the chat database. Is set to {@code -1} if the message has not been saved yet. */
	private long id;
	
	/** Stores whether the message needs to be updated in the chat database. */
	private boolean updateRequested = false;
	
	/**
	 * The message's send state
	 *
	 * @see SendState
	 */
	private SendState sendState = SendState.PENDING;
	
	
	/**
	 * Creates an instance of the {@link LocalData} class. The attribute {@link #updateRequested} is automatically set to {@code false}.
	 *
	 * @param id the message's unique id in the chat database (use {@code -1} if the message has not been saved yet)
	 * @param sendState the message's send state
	 */
	public LocalData(long id, SendState sendState) {
		setId(id);
		setSendState(sendState);
	}

	/**
	 * Gets the message's unique id in the chat database.
	 * @return the message's unique id in the chat database.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the message's unique id in the chat database. Is not allowed to be smaller than {@code -1}.
	 * @param id the message's unique id in the chat database
	 */
	public void setId(long id) {
		if(id < -1) L.f(this.getClass(), new IllegalArgumentException("int id (" + id +  ") should be greater than -2 !"));
		this.id = id;
	}
	
	/**
	 * Gets the message's send state.
	 * @return the message's send state
	 */
	public SendState getSendState() {
		return sendState;
	}

	/**
	 * Sets the message's send state.
	 * @param sendState the message's send state
	 */
	public void setSendState(SendState sendState) {
		if(sendState == null) L.f(this.getClass(), new IllegalArgumentException("SendState sendState is not allowed to be null!"));
		
		this.sendState = sendState;
	}
	
	@Override
	public LocalData clone() {
		return new LocalData(getId(), getSendState());
	}
}
