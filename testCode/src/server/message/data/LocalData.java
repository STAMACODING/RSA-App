package server.message.data;

import server.services.databaseService.DatabaseService;
import server.services.transferServices.sendService.ClientSendService;
import server.services.transferServices.sendService.ServerSendService;

/**
 * Stores information about a message that is relevant to the client only. This information is not sent to the server and is also not encrypted.
 */
public class LocalData {
	
	/** The message's unique id in the chat database. Is set to {@code -1} if the message has not been saved yet. */
	private int id;
	
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
	public LocalData(int id, SendState sendState) {
		setId(id);
		setSendState(sendState);
	}

	/**
	 * Gets the message's unique id in the chat database.
	 * @return the message's unique id in the chat database.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the message's unique id in the chat database.
	 * @param id the message's unique id in the chat database
	 */
	public void setId(int id) {
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
		if(sendState == null) throw new IllegalArgumentException("SendState is not allowed to be null!");
		this.sendState = sendState;
	}
	
	/**
	 * Checks whether the message should get stored in the chat database by the {@link DatabaseService}.
	 * @return whether the message should get stored in the chat database by the {@link DatabaseService}
	 */
	public boolean isToStore() {
		return getId() == -1;
	}

	/**
	 * Checks whether the message should get updated by the {@link DatabaseService}.
	 * @return whether the message should get updated by the {@link DatabaseService}
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
}
