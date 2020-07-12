package server.message.data;

import server.services.databaseService.DatabaseService;
import server.services.transferServices.sendService.SendService;

public class LocalData {
	/**
	 * the message's unique id in the chat database
	 */
	private int id;
	
	/**
	 * whether the message needs to be updated in the chat database
	 */
	private boolean updateRequested = false;
	
	/**
	 * the message's send state
	 * @see SendState
	 */
	private SendState sendState = SendState.PENDING;
	
	
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
	 * Checks whether this message should get stored in the chat database by the {@link DatabaseService}.
	 * @return whether this message should get stored in the chat database by the {@link DatabaseService}
	 */
	public boolean isToStore() {
		return getId() == -1;
	}

	/**
	 * Checks whether this message should get updated by the {@link DatabaseService}.
	 * @return whether this message should get updated by the {@link DatabaseService}
	 */
	public boolean isToUpdate() {
		return this.isUpdateRequested() && !isToStore();
	}

	/**
	 * Checks whether this message should be send using the {@link SendService}.
	 * @return whether this message should be send using the {@link SendService}
	 */
	public boolean isToSend() {
		return getSendState().equals(SendState.PENDING);
	}
}
