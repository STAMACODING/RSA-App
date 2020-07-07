package server.message;

import java.io.Serializable;

import server.services.databaseService.DatabaseService;
import server.services.transferServices.sendService.SendService;

/**
 * An instance of this class represents a message with all its different attributes.
 *
 */
public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7382792471329161848L;

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
	
	/**
	 * This data cannot be decoded by the server.
	 */
	private MessageData messageData;
	private byte[] encodedMessageData;
	
	/**
	 * This data can be decoded by the server.
	 */
	private MessageMeta messageMeta;
	private byte[] encodedMessageMeta;
	
	
	public Message(int id, SendState state, MessageData messageData, MessageMeta messageMeta) {
		setId(id);
		setMessageData(messageData);
		setMessageMeta(messageMeta);
		setSendState(state);
	}
	
	public Message(int id, SendState state, byte[] encodedMessageData, byte[] encodedMessageMeta) {
		setId(id);
		setEncodedMessageData(encodedMessageData);
		setEncodedMessageMeta(encodedMessageMeta);
		setSendState(state);
	}
	
	public Message(int id, SendState state, byte[] encodedMessageData, MessageMeta messageMeta) {
		setId(id);
		setEncodedMessageData(encodedMessageData);
		setMessageMeta(messageMeta);
		setSendState(state);
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

	public MessageData getMessageData() {
		return messageData;
	}

	public MessageMeta getMessageMeta() {
		return messageMeta;
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

	public byte[] getEncodedMessageData() {
		return encodedMessageData;
	}

	private void setEncodedMessageData(byte[] encodedMessageData) {
		this.encodedMessageData = encodedMessageData;
	}

	public byte[] getEncodedMessageMeta() {
		return encodedMessageMeta;
	}

	private void setEncodedMessageMeta(byte[] encodedMessageMeta) {
		this.encodedMessageMeta = encodedMessageMeta;
	}

	private void setMessageData(MessageData messageData) {
		this.messageData = messageData;
	}

	private void setMessageMeta(MessageMeta messageMeta) {
		this.messageMeta = messageMeta;
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
	
	public void encodeMessageMeta() {
		setEncodedMessageMeta(MessageMeta.encode(getMessageMeta()));
		setMessageMeta(null);
	}
	
	public void decodeMessageMeta() {
		setMessageMeta(MessageMeta.decode(getEncodedMessageMeta()));
		setEncodedMessageMeta(null);
	}
	
	public void encodeMessageData() {
		setEncodedMessageData(MessageData.encode(getMessageData()));
		setMessageData(null);
	}
	
	public void decodeMessageData() {
		setMessageData(MessageData.decode(getEncodedMessageData()));
		setEncodedMessageData(null);
	}
}
