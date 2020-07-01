package server;

import java.sql.Date;

public class UnstoredMessage {
	private byte status;
	private final byte[] message;
	private final byte sendingId;
	private final Date date;
	
	public UnstoredMessage(byte status, byte [] message, byte sendingId, Date d) {
		this.status = status;
		this.message = message;
		this.sendingId = sendingId;
		this.date = d;
	}
	
	public byte getStatus() {
		return this.status;
	}
	
	public byte[] getMessage() {
		return this.message;
	}
	
	public byte getSendingId() {
		return this.sendingId;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public void setStatus(byte s) {
		this.status = s;
	}
}
