package server.message.data;

import java.io.Serializable;

import server.Utils;

public class ProtectedData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4421436516268769528L;

	/**
	 * the message as decoded string
	 */
	private String textMessage;
	
	/**
	 * the time the message was originally created
	 */
	private final long date;
	
	
	public ProtectedData(String textMessage, long date) {
		this.date = date;
		setTextMessage(textMessage);
	}
	
	
	/**
	 * gets the time the message was originally created.
	 * @return the time the message was originally created
	 */
	public long getDate() {
		return date;
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
	}
	
	public static byte[] encode(ProtectedData messageData) {
		byte[] decodedData = Utils.Serialization.serialize(messageData);
		// TODO encode decoded data
		byte[] encodedData = decodedData;
		return encodedData;
	}
	
	public static ProtectedData decode(byte[] encodedData) {
		// TODO decode encoded data
		byte[] decodedData = encodedData;
		return (ProtectedData) Utils.Serialization.deserialize(decodedData);
	}
}
