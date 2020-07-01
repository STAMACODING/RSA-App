package server;

import java.sql.*;

public class Message extends UnstoredMessage{
	private final int messageId;

	public Message(int messageId, byte status, byte[] message, byte sendingId, Date d) {
		super(status, message, sendingId, d);
		this.messageId = messageId;
	}

	public int getMessageId() {
		return messageId;
	}
	
}
