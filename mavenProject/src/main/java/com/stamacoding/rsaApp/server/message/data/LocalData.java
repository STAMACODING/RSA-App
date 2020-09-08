package com.stamacoding.rsaApp.server.message.data;

import java.io.Serializable;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.client.services.ChatDatabaseService;
import com.stamacoding.rsaApp.server.client.services.ClientSendService;
import com.stamacoding.rsaApp.server.server.services.ServerSendService;

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
		if(id < -1) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("int id (" + id +  ") should be greater than -2 !"));
		this.id = id;
	}
	
	/**
	 * Gets whether the message needs to be updated in the chat database.
	 * @return whether the message needs to be updated in the chat database
	 */
	private boolean isUpdateRequested() {
		return updateRequested;
	}

	/**
	 * Sets whether the message needs to be updated in the chat database.
	 * @param updateRequested whether the message needs to be updated in the chat database
	 */
	public void setUpdateRequested(boolean updateRequested) {
		this.updateRequested = updateRequested;
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
		if(sendState == null) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("SendState sendState is not allowed to be null!"));
		
		this.sendState = sendState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((sendState == null) ? 0 : sendState.hashCode());
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
		LocalData other = (LocalData) obj;
		if (id != other.id)
			return false;
		if (sendState != other.sendState)
			return false;
		return true;
	}
	
	/**
	 * Checks whether the message should get stored in the chat database by the {@link ChatDatabaseService}.
	 * @return whether the message should get stored in the chat database by the {@link ChatDatabaseService}
	 */
	public boolean isToStore() {
		return getId() == -1;
	}

	/**
	 * Checks whether the message should get updated by the {@link ChatDatabaseService}.
	 * @return whether the message should get updated by the {@link ChatDatabaseService}
	 */
	public boolean isToUpdate() {
		return this.isUpdateRequested() && !isToStore();
	}

	/**
	 * Checks whether the message should be send using the {@link ClientSendService}/{@link ServerSendService}.
	 * @return whether the message should be send using the {@link ClientSendService}/{@link ServerSendService}
	 */
	public boolean isToSend() {
		return getSendState().equals(SendState.PENDING);
	}
	
	@Override
	public LocalData clone() {
		return new LocalData(getId(), getSendState());
	}
	
	public static int getSendStateAsInt(SendState sendState) {
		switch(sendState) {
		case PENDING:
			return 0;
		case SENT:
			return 1;
		default:
			return -1;
		}
	}
	
	public static SendState getIntAsSendState(int sendState) {
		switch(sendState) {
		case 0:
			return SendState.PENDING;
		case 1:
			return SendState.SENT;
		default:
			return null;
		}
	}
}
