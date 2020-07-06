package server.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.Utils;
import server.services.databaseServices.DatabaseService;
import server.services.transferServices.sendService.SendService;

/**
 * An instance of this class represents a message with all its different attributes.
 *
 */
public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2536727039165040724L;
	private static final String encodeTextMessage = "********";
	

	/**
	 * the id of the client that sent, is sending or will send this message
	 */
	private final byte sendingId;
	
	/**
	 *  the id of the client that received or will receive this message
	 */
	private final byte receivingId;
	
	/**
	 * the time the message was originally created
	 */
	private final long date;
	
	/**
	 * the message's unique id in the chat database
	 */
	private int id;
	
	/**
	 * the message as encoded byte[]
	 */
	private byte[] byteMessage;
	
	/**
	 * the message as decoded string
	 */
	private String textMessage;
	
	/**
	 * the message's send state
	 * @see SendState
	 */
	private SendState sendState;
	
	/**
	 * whether the message needs to be updated in the chat database
	 */
	private boolean updateRequested = false;
	
	/**
	 * the messsage's rsa state
	 * @see RsaState
	 */
	private RsaState rsaState;
	
	public Message(int id, String textMessage, byte sendingId, byte receivingId, long date, SendState state, RsaState rsaState) {
		this.date = date;
		this.receivingId = receivingId;
		this.sendingId = sendingId;

		setTextMessage(textMessage);
		setId(id);
		setByteMessage(Utils.RSA.encode(textMessage));
		setRsaState(rsaState);
	}
	
	public Message(byte[] byteMessage, byte sendingId, byte receivingId, long date, SendState state, RsaState rsaState) {
		this.date = date;
		this.receivingId = receivingId;
		this.sendingId = sendingId;
		setId(-1);
		setByteMessage(byteMessage);
		setTextMessage(Utils.RSA.decode(byteMessage));
		setRsaState(rsaState);
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
		return getSendState() == SendState.PENDING;
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
	 * gets the time the message was originally created.
	 * @return the time the message was originally created
	 */
	public long getDate() {
		return date;
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
	 * Gets the message as byte[].
	 * @return the message as byte[]
	 */
	public byte[] getByteMessage() {
		return byteMessage;
	}

	/**
	 * Sets the message byte[] representation. Automatically updates the message's textMessage.
	 * @param byteMessage the message byte[] representation
	 */
	public void setByteMessage(byte[] byteMessage) {
		this.byteMessage = byteMessage;
		setRsaState(getRsaState());
	}

	/**
	 * Gets the message as decoded string. Returns "********" if {@link #getRsaState()} is set to {@link RsaState#ENCODED}.
	 * @return the message as decoded string
	 */
	public String getTextMessage() {
		return textMessage;
	}

	/**
	 * Sets the message's textual representation. Automatically updates the message's byteMessage. Encodes this string if {@link #getRsaState()} is set to {@link RsaState#ENCODED}.
	 * @param textMessage the message as decoded string
	 */
	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
		setRsaState(getRsaState());
	}

	/**
	 * Gets the messsage's rsa state.
	 * @return the messsage's rsa state
	 */
	public RsaState getRsaState() {
		return rsaState;
	}

	/**
	 * Sets the messsage's rsa state. Setting the rsa state to {@link RsaState#ENCODED} encodes the message,
	 * setting it to {@link RsaState#DECODED} decodes the message.
	 * @param rsaState the messsage's rsa state
	 */
	public void setRsaState(RsaState rsaState) {
		if(rsaState == null) throw new IllegalArgumentException("RsaState is not allowed to be null!");
		this.rsaState = rsaState;
		switch(rsaState) {
		case DECODED:
			setTextMessage(Utils.RSA.decode(getByteMessage()));
			break;
		case ENCODED:
			setTextMessage(Message.encodeTextMessage);
			break;
		}
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
	 * Removes the message's id and sets {@link Message#updateRequested} to false.
	 */
	public void removeLocalDependencies() {
		setUpdateRequested(false);
		setId(-1);
	}
	
	/**
	 * Converts a {@link Message} to a byte array.
	 * @param m the message to serialize
	 * @return the message as byte array
	 */
	public static byte[] messageToByteArray(Message m) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(out);
			os.writeObject(m);
		} catch (Exception e) {
			Logger.error(Message.class.getSimpleName(), "Failed to serialize message!");
			return null;
		}
		return out.toByteArray();
	}
	
	/**
	 * Converts an {@link Message} of {@link Message}s to a byte array.
	 * @param l the list to serialize
	 * @return the list as byte array
	 */
	public static byte[] messageListToByteArray(ArrayList<Message> l) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(out);
			os.writeObject(l);
		} catch (Exception e) {
			Logger.error(Message.class.getSimpleName(), "Failed to serialize message list!");
			return null;
		}
		return out.toByteArray();
	}
	
	/**
	 * Converts a serialized {@link Message} back to a {@link Message} object. 
	 * @param message the serialized message
	 * @return the message as object
	 */
	public static Message byteArrayToMessage(byte[] message) {
		ByteArrayInputStream in = new ByteArrayInputStream(message);
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(in);
			return (Message) is.readObject();
		} catch (Exception e) {
			Logger.error(Message.class.getSimpleName(), "Failed to deserialize message!");
			return null;
		}
	}
	
	/**
	 * Converts a serialized {@link ArrayList} of {@link Message}s back to an {@link ArrayList} object. 
	 * @param messageList the serialized list
	 * @return the list as object
	 */
	public static ArrayList<Message> byteArrayToMessageList(byte[] messageList) {
		ByteArrayInputStream in = new ByteArrayInputStream(messageList);
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(in);
			return (ArrayList<Message>) is.readObject();
		} catch (Exception e) {
			Logger.error(Message.class.getSimpleName(), "Failed to deserialize message list!");
			return null;
		}
	}
}
