package com.stamacoding.rsaApp.server.client.managers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.message.Message;

/**
 *  Interface to the client's chat database. In this chat database all messages the client receives and sends get stored.
 *  <ul>
 *  	<li> Use <code>DBManager.getInstance()</code> to get an instance of this object.</li>
 * 	</ul>
 */
public class DatabaseManager{
	
	/** The only instance of this class */
	private volatile static DatabaseManager singleton = new DatabaseManager();
	
	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the manager's {@link #singleton}. Use {@link DatabaseManager#getInstance()} to get the
	 *  only instance of this class.
	 */
	private DatabaseManager() {}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class.
	 */
	public static DatabaseManager getInstance() {
		return singleton;
	}

	
	/**
	 * Stores a message in the chat database. By doing that the message's id is set to something not equal to {@code -1}.
	 * If the message's id stays {@code -1}, the message couldn't get stored. 
	 * <p>
	 * Added to that the method will return {@code false} if
	 * the storing process fails and {@code true} if everything goes fine.
	 * </p>
	 * @param m the message to store
	 * @return whether the storing process succeeded
	 */
	public boolean addMessageToDB(Message m) {
		m.getLocalData().setId(23); // to tell message that it has been stored in the DB
		
		return true;
	}
	
	/**
	 * Updates a message in the chat database. The message has
	 * to be already stored in the database to get updated successfully.
	 * @param updatedMessage
	 */
	public boolean updateMessage(Message updatedMessage) {
		
		return true;
	}
	
	/**
	 * Gets all messages stored in the chat database as array. Returns
	 * {@code null} if the process fails.
	 * @param id // the sendingId for which the messages should be retrieved
	 * @return all messages stored in the chat database 
	 */
	public Message[] getMessagesFromDB(int sendingId){
		
		int sID = sendingId;
		int messageLimit = 10; // how many messges can be retreived and stored in array
		Message[] messages = new Message [messageLimit];
		
		MessageDAO messagesDAO = new MessageDAO();
		messages = messagesDAO.getMessages(sID, messageLimit);
		
		// printMessageFromDB(rs);
		
		return messages;		
			
	}
	
	/**
	 * Prints a message contained in a {@link ResultSet}.
	 * @param rs ResultSet containing the message
	 */
	public void printMessageFromDB(ResultSet rs) {
		int messageId;
		Date date;
		String message;
		int sendingId;
		int status;
		
		System.out.print("messageId");
		System.out.print("|");
		System.out.print("date");
		System.out.print("|");
		System.out.print("message");
		System.out.print("|");
		System.out.print("sendingId");
		System.out.print("|");
		System.out.print("status");
		System.out.println("");
		
		try {
			
			while(rs.next()) {
				
				messageId = rs.getInt("messageId");
			
				date = new Date(rs.getLong("timestamp_sec"));
				
				message = rs.getString("message");
				sendingId = rs.getInt("sendingID");
				status = rs.getInt("status");
				
			
				System.out.print(messageId);
				System.out.print("|");
				System.out.print(date);
				System.out.print("|");
				System.out.print(message);
				System.out.print("|");
				System.out.print(sendingId);
				System.out.print("|");
				System.out.print(status);
				System.out.println("");
			}
		}catch(Exception e) {
				e.printStackTrace();
		}
		
	}
	
	
	public static void main(String [] args) throws Exception {
		//Message[] messages = DatabaseManager.getInstance().getMessagesFromDB();
	}
	
}
