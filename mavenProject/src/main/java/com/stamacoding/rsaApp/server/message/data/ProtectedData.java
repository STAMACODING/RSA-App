package com.stamacoding.rsaApp.server.message.data;

import java.io.Serializable;

import com.stamacoding.rsaApp.log.logger.Logger;

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
		if(date < 0) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("long date (" + date +  ") should be greater than 0 !"));
		
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
		if(textMessage == null) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("String textMessage is not allowed to be null!"));
		
		this.textMessage = textMessage;
	}
	
	/**
	 * Indicates if another object is equal to this one.
	 * @return whether another object is equal
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj instanceof ProtectedData) {
			ProtectedData pd = (ProtectedData) obj;
			if(pd.getDate() != this.getDate()) return false;
			if(!pd.getTextMessage().equals(this.getTextMessage())) return false;
			return true;
		}
		return false;
	}
	
	@Override
	public ProtectedData clone() {
		return new ProtectedData(getTextMessage(), getDate());
	}
}
