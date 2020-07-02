package server.services.transferServices;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;

public class TransferMessage implements Serializable{
	/**
	 * Necessary for serializing this object (needed to send and receive messages as byte arrays).
	 */
	private static final long serialVersionUID = -8765409025585334341L;
	
	/**
	 * the id of the client that sent or is sending this message
	 */
	private final byte sendingId;
	
	/**
	 *  the id of the client that received this message
	 */
	private final byte receivingId;
	
	/**
	 * the time the message was originally created
	 */
	private final Date date;
	
	/**
	 * the message's byte[] representation
	 */
	private final byte[] byteMessage;
	
	public TransferMessage(byte[] byteMessage, byte sendingId, byte receivingId, Date date) {
		this.byteMessage = byteMessage;
		this.date = date;
		this.receivingId = receivingId;
		this.sendingId = sendingId;
	}
	
	/**
	 * Gets the id of the client that sent or is sending this message.
	 * @return the id of the client that sent or is sending is message
	 */
	public byte getSendingId() {
		return this.sendingId;
	}
	
	/**
	 * Gets the id of the client that received this message.
	 * @return the id of the client that received this message
	 */
	public byte getReceivingId() {
		return receivingId;
	}
	
	/**
	 * Gets the time the message was originally created. 
	 * @return the time the messages was originally created
	 */
	public Date getDate(){
		return this.date;
	}
	
	/**
	 * Gets the message's byte[] representation.
	 * @return the message's byte[] representation
	 */
	public byte[] getByteMessage() {
		return byteMessage;
	}
	
	public static byte[] messageToByteArray(TransferMessage m) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(out);
			os.writeObject(m);
		} catch (Exception e) {
			Logger.error(TransferMessage.class.getSimpleName(), "Failed to serialize message!");
			return null;
		}
		return out.toByteArray();
	}
	
	public static byte[] messageListToByteArray(ArrayList<TransferMessage> l) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(out);
			os.writeObject(l);
		} catch (Exception e) {
			Logger.error(TransferMessage.class.getSimpleName(), "Failed to serialize message list!");
			return null;
		}
		return out.toByteArray();
	}
	
	public static TransferMessage byteArrayToMessage(byte[] message) {
		ByteArrayInputStream in = new ByteArrayInputStream(message);
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(in);
			return (TransferMessage) is.readObject();
		} catch (Exception e) {
			Logger.error(TransferMessage.class.getSimpleName(), "Failed to deserialize message!");
			return null;
		}
	}
	
	public static ArrayList<TransferMessage> byteArrayToMessageList(byte[] messageList) {
		ByteArrayInputStream in = new ByteArrayInputStream(messageList);
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(in);
			return (ArrayList<TransferMessage>) is.readObject();
		} catch (Exception e) {
			Logger.error(TransferMessage.class.getSimpleName(), "Failed to deserialize message list!");
			return null;
		}
	}
}
