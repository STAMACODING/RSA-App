package server.services.databaseServices;

import java.sql.Date;

import server.Utils;
import server.services.transferServices.TransferMessage;

/**
 * An instance of this class represents a decoded message, that is already stored or gets stored in the chat database.
 */
public class DatabaseMessage{
	
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
	 * the message's textual representation
	 */
	private final String textMessage;
	
	/**
	 * the message's id
	 */
	private int id; 
	
	/**
	 * the message's status
	 */
	private byte status;
	
	/**
	 * Creates an new instance of the {@link DatabaseMessage} using a {@link TransferMessage}. The message's status
	 * is set to 0.
	 * @param m the TransferMessage
	 */
	public DatabaseMessage(TransferMessage m) {
		this.textMessage = Utils.Convert.byteArrayToString(m.getByteMessage());
		this.date = m.getDate();
		this.receivingId = m.getReceivingId();
		this.sendingId = m.getSendingId();
		setStatus((byte) 0);
	}

	/**
	 * Creates an new instance of the {@link DatabaseMessage}.
	 * @param id the message's id
	 * @param textMessage the message's textual representation
	 * @param sendingId the id of the client that sent or is sending this message
	 * @param receivingId the id of the client that received this message
	 * @param date the time the message was originally created
	 * @param status the message's status
	 */
	public DatabaseMessage(int id, String textMessage, byte sendingId, byte receivingId, Date date, byte status) {
		this.textMessage = textMessage;
		this.date = date;
		this.receivingId = receivingId;
		this.sendingId = sendingId;
		setId(id);
		setStatus(status);
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
	 * Gets the message's textual representation.
	 * @return the message's textual representation
	 */
	public String getTextMessage() {
		return textMessage;
	}
	
	/**
	 * Gets the message's id.
	 * @return the message's id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the message's id.
	 * @param id the message's id
	 */
	protected void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the message's status.
	 * @return the message's status
	 */
	public byte getStatus() {
		return status;
	}
	
	/**
	 * Sets the message's status.
	 * @param status the message's status
	 */
	public void setStatus(byte status) {
		this.status = status;
	}
}
