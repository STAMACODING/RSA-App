package server;

import java.sql.*;

public class Message {

	private final int messageId;
	private byte status;
	private final byte[] message;
	private final byte sendingId;
	private final java.sql.Date date;
	
	public Message(int messageId, byte status, byte [] message, byte sendingId, java.sql.Date d) {
		this.messageId = messageId;
		this.status = status;
		this.message = message;
		this.sendingId = sendingId;
		this.date = d;
	}
	
	public int getmessageId() {
		return this.messageId;
	}
	
	public byte getstatus() {
		return this.status;
	}
	
	public byte[] getmessage() {
		return this.message;
	}
	
	public byte getsendingId() {
		return this.sendingId;
	}
	
	public java.sql.Date getDate(){
		return this.date;
	}
	
	public void setstatus(byte s) {
		this.status = s;
	}
	
}
