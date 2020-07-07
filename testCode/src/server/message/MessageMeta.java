package server.message;

import java.io.Serializable;

import server.Utils;

public class MessageMeta implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7534005873602518807L;

	/**
	 * the id of the client that sent, is sending or will send this message
	 */
	private final byte sendingId;
	
	/**
	 *  the id of the client that received or will receive this message
	 */
	private final byte receivingId;
	
	
	public MessageMeta(byte sendingId, byte receivingId) {
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
	
	public static byte[] encode(MessageMeta messageMeta) {
		byte[] decodedData = Utils.Serialization.serialize(messageMeta);
		// TODO encode decoded data
		byte[] encodedData = decodedData;
		return encodedData;
	}
	
	public static MessageMeta decode(byte[] encodedData) {
		// TODO decode encoded data
		byte[] decodedData = encodedData;
		return (MessageMeta) Utils.Serialization.deserialize(decodedData);
	}
}
