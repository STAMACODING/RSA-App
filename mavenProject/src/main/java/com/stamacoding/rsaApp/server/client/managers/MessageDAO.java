package com.stamacoding.rsaApp.server.client.managers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.Integer;

import com.stamacoding.rsaApp.server.message.Message;
import com.stamacoding.rsaApp.server.message.data.LocalData;
import com.stamacoding.rsaApp.server.message.data.ProtectedData;
import com.stamacoding.rsaApp.server.message.data.SendState;
import com.stamacoding.rsaApp.server.message.data.ServerData;

public class MessageDAO {

	/** The URL to the database file. */
	private final String url = "jdbc:sqlite:DB/UserDatabase.db";
	
	/** The used user name to interact with the database. */
	private final String userName = "root";
	
	/** The used password to interact with the database. */
	private final String password = "root";
	

	/**
	 * returns a certain  Message Object from ChatHistory database identified by id
	 * @param id, the userId in which context of conversation the message was sent
	 * @return the Message Object
	 * @throws Exception 
	 */
	public Message [] getMessages (int id, int messageLimit) {
		
		int Id = id;
		int mLimit = messageLimit;
		Message [] messages = new Message [messageLimit]; // for now only the first 10 messages will be retrieved
		
		String query = (String) "Select * from ChatHistory where receivingID = " + Id;
		ResultSet rs = null;
		
		int messageId;
		long date;
		String message;
		int sendingId;
		int receivingId;
		int status;
		
		
		Connection con = getDBConnection(url, userName, password);
		
		try {
			Statement st = con.createStatement();		
			rs = st.executeQuery(query);
			
			if(rs != null) {
				System.out.println("SQL statement executed succesfully and ResultSet rs filled");
			}else {
				System.out.println("Failed to execute sql statement. ResultSet rs was not filled");
			}
			rs.next();
			
			//extract values from rs and assign to messageDAO object;	

			int i = 0;
			while(rs.next() && i < messageLimit) {
				
				messageId = rs.getInt("messageId");
				date = rs.getLong("timestamp_sec");
				message = rs.getString("message");
				sendingId = rs.getInt("sendingID");
				receivingId = rs.getInt("receivingID");
				status = rs.getInt("status");
				
				// convert sendingID and receiving ID from int to byte
				byte sendigIdBytes = (byte) sendingId; 
				byte receivingIdBytes = (byte) receivingId;
				
				// set SendState according to DB
				SendState sendState; 
				if(status != 0) {
					sendState = SendState.SENT;
				}else {
					sendState = SendState.PENDING;
				}
				
				// create a Message Object
				LocalData lg = new LocalData(messageId, sendState);
				ProtectedData pd = new ProtectedData(message, date);
				ServerData sd = new ServerData(sendigIdBytes, receivingIdBytes);
				Message m = new Message(lg, pd, sd);
				
				messages[i] = m; // add last created message to messages array
				
				i ++;
			}
			
			// m.print();
			
			con.close();
			return messages;
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to extract message from database");
			
			return messages;
		}
		
	}
	
	/** 
	 * add a new user object to users Db
	 * @param newUser the user object that should be stored
	 * throws error when password of user is empty
	 * throw exception when process fails
	 * @throws Exception 
	 */
	public boolean addNewMessage( Message newM) {
		
		Message newMessage = newM;
		 
		LocalData messageLocalData = newMessage.getLocalData();
		ProtectedData messageProtectedData = newMessage.getProtectedData();
		ServerData messageServerData = newMessage.getServerData();
		 
		int messageId = messageLocalData.getId();
		long date = messageProtectedData.getDate();
		String message = messageProtectedData.getTextMessage();
		int sendingId = (int) messageServerData.getSendingId();
		int receivingId = (int) messageServerData.getReceivingId();
		
		SendState st = messageLocalData.getSendState();
		int status;
		if (st == SendState.PENDING) {
			status = 0;
		}else {
			status = 1;
		}		
		 
		if (messageId == 0 || date == 0 || message == null || sendingId == 0 || receivingId == 0 || status == 0) { // more information needed !!!
			 System.out.println("Failed to provide all neccessary data to add new message!!" );
			 System.out.println("Failed to insert Message into ChatHistory");
			 return false;
		 }
		 
		 Connection con = getDBConnection(url, userName, password);
		 
		 String query = (String) "Insert into ChatHistory(message,sendingID,receivingID,status,timestamp_sec) "
			 		+ "values(\"" + message + "\", " + sendingId + ", " + receivingId + ", " + status + ", " + date + ") ";
		 
		 PreparedStatement pst;
		 try {
			pst = con.prepareStatement(query);
			pst.executeUpdate();
			System.out.println("Added new message to the ChatHistory");
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to execute insert query");
			System.out.println("failed to insert message into DB");
			e.printStackTrace();
			return false;
		} 
		 return true;
	}
	
	/**
	 * updates a certain message's send state in DB
	 * @param messageID to identify the message which has to be updated
	 * @param st , this is the updatedMessage SendState
	 * gives error when update fails
	 * throws exception when error occures
	 * @throws Exception 
	 */
	public boolean updateStatus(int mID, SendState st) {
		int messageID = mID;
		SendState sendState = st;
		String updateStatement;
		
		// get new SendState
		SendState newSendState = st;
		int status;
		if (st == SendState.PENDING) {
			status = 0;
		}else {
			status = 1;
		}
		
		Connection con = getDBConnection(url, userName, password);
		
		//insert new message Data into prepared Statement
		updateStatement = " UPDATE ChatHistory "
				+ "Set  status    = " + status + " "
				+ "where messageId = " + messageID + " ;";
				
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(updateStatement);
			pst.executeUpdate();
			System.out.println("Updated user information");
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to update user information");
			return false;
		}
			
		return true;
	}
	
	/**
	 * deletes a message from Database
	 * @param messageID the messageID of the message that will be deleted
	 * throws error when message is not existing
	 * throw exception when process fails
	 * @throws Exception 
	 */
	public boolean deleteMessage(int messageID) {
		
		int mID = messageID;
		 
		 Connection con = getDBConnection(url, userName, password);
		 
		 String query = (String) "Delete from ChatHistory where userID = " + mID;
		 PreparedStatement pst;
		 try {
			pst = con.prepareStatement(query);
			pst.executeUpdate();
			System.out.println("Deleted message with messageID: " + mID + " from ChatHistory");
			con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to execute delete query");
			e.printStackTrace();
			// add exception when the message which is supposed to be deleted does not exist
			return false;
		} 
		 
		 return true;
	}
	
	
	 /* connects to ChatHistory database
	 * @param URL the path to the DB
	 * @param uName username of DB
	 * @param p password of DB
	 * @return , Connection object
	 */
	public Connection getDBConnection(String URL, String uName, String p){
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url, userName, password);
			
			if (con != null) {
				System.out.println("connection established !!");
			} else{
				System.out.println("connection was tried to establish but did not return any connection object");
			}
			return con;
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Failed to connect to database");
			return con;
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Failed to connect to database");
			return con;
		}
	}
	
	
}
