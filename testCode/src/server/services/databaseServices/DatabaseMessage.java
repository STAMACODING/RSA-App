package server.services.databaseServices;

import java.io.Serializable;
import java.sql.Date;

import server.Utils;
import server.services.transferServices.TransferMessage;

/**
 * An instance of this class represents a decoded message, that is already stored or gets stored in the chat database.
 */
public class DatabaseMessage implements Serializable{
	/**
	 * Necessary for serializing this object (needed to send and receive messages as byte arrays).
	 */
	private static final long serialVersionUID = -1165829653712536630L;
	
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
	
	public DatabaseMessage(TransferMessage m) {
		this.textMessage = Utils.Convert.byteArrayToString(m.getByteMessage());
		this.date = m.getDate();
		this.receivingId = m.getReceivingId();
		this.sendingId = m.getSendingId();
		setId(id);
	}
	
	public DatabaseMessage(int id, String textMessage, byte sendingId, byte receivingId, Date date) {
		this.textMessage = textMessage;
		this.date = date;
		this.receivingId = receivingId;
		this.sendingId = sendingId;
		setId(id);
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
}
