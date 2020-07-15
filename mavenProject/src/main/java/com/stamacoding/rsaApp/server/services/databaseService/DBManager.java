package com.stamacoding.rsaApp.server.services.databaseService;

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
public class DBManager{
	
	/** The only instance of this class */
	private volatile static DBManager singleton = new DBManager();
	
	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the manager's {@link #singleton}. Use {@link DBManager#getInstance()} to get the
	 *  only instance of this class.
	 */
	private DBManager() {}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class.
	 */
	public static DBManager getInstance() {
		return singleton;
	}
	

	/** The URL to the database file. */
	private final String url = "jdbc:sqlite:DB/UserDatabase.db";
	
	/** The used user name to interact with the database. */
	private final String userName = "root";
	
	/** The used password to interact with the database. */
	private final String password = "root";

	/**
	 * Connects to the chat database using the JDBC driver. Returns {@code null} if the process fails.
	 * @return the created connection
	 */
	public Connection connect(){
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url, userName, password);
			Logger.debug(this.getClass().getSimpleName(), "Connecting to DB/UserDatabse.db succesfull");
			
			return con;
		}catch(ClassNotFoundException | SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to connect to chat database");
			return con;
		}
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
		m.getLocalData().setId(23);
		return true;
	}
	
	/**
	 * Updates a message in the chat database. The message has
	 * to be already stored in the database to get updated successfully.
	 * @param updatedMessage
	 */
	public void updateMessage(Message updatedMessage) {
		
	}
	
	/**
	 * Gets all messages stored in the chat database as array. Returns
	 * {@code null} if the process fails.
	 * @return all messages stored in the chat database
	 * @throws Exception 
	 */
	public Message[] getMessagesFromDB(){
		try {
			Connection con = DBManager.getInstance().connect();
			
			String query = "SELECT * from ChatHistory";
			
			Statement state = con.createStatement();
			Logger.debug(this.getClass().getSimpleName(), "Statement instance initiated");
			
			ResultSet rs = state.executeQuery(query);
			Logger.debug(this.getClass().getSimpleName(), "Query executed succesfully");
			
			int messageId;
			Date date;
			String message;
			int sendingId;
			int status;
			
			while(rs.next()) {
				messageId = rs.getInt("messageId");
				date = new Date(rs.getLong("time"));	
				message = rs.getString("message");
				sendingId = rs.getInt("sendingID");
				status = rs.getInt("status");
	
			}
			//printMessageFromDB(rs);
			
		}catch(Exception e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to get messages from database");
		}
		return null;
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
			
				 date = new Date(rs.getLong("time"));
				
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
		Message[] messages = DBManager.getInstance().getMessagesFromDB();
	}
	
}
