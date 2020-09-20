package com.stamacoding.rsaApp.network.global.message.data;

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
	private final String textMessage;
	
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
		if(textMessage == null) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("String textMessage is not allowed to be null!"));
		
		this.date = date;
		this.textMessage = textMessage;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (date ^ (date >>> 32));
		result = prime * result + ((textMessage == null) ? 0 : textMessage.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProtectedData other = (ProtectedData) obj;
		if (date != other.date)
			return false;
		if (textMessage == null) {
			if (other.textMessage != null)
				return false;
		} else if (!textMessage.equals(other.textMessage))
			return false;
		return true;
	}
	
	@Override
	public ProtectedData clone() {
		return new ProtectedData(getTextMessage(), getDate());
	}
}
