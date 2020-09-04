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
	public void addNewMessage( Message newM) throws Exception {
		 Message newMessage = newM;
		 
		 LocalData messageLocalData = newMessage.getLocalData();
		 ProtectedData messageProtectedData = newMessage.getProtectedData();
		 ServerData messageServerData = newMessage.getServerData();
		 
		 if (userPassword == null) { // user needs to provide password !!!
			 System.out.println("user: " + u.getUserID() + " did not provide a password!!" );
			 System.out.println("failed to insert User into DB");
			 return;
		 }
		 
		 Connection con = getDBConnection(url, userName, password);
		 
		 String query = (String) "Insert into users(lastName,name,birthDate,eMail,password) "
			 		+ "values(\"" + userLastName + "\", \"" + uName + "\", " + userBirthDate + ", \"" + userEMail + "\", \"" + userPassword + "\")";
		 
		 PreparedStatement pst;
		 try {
			pst = con.prepareStatement(query);
			pst.executeUpdate();
			System.out.println("Added new user to the userDB.db database");
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to execute insert query");
			System.out.println("failed to insert User into DB");
			e.printStackTrace();
			con.close();
		} 
	}
	
	/**
	 * updates a certain user in DB
	 * @param userID to identify the user which has to be updated
	 * @param u , this is the updatedUserObject
	 * stops when userID would change
	 * stops when password, eMail or lastName is empty
	 * gives error when update failed
	 * throws exception when error occures
	 * @throws Exception 
	 */
	public void updateUser(int userID, User u ) throws Exception {
		int uID = userID;
		User updatedUser = u;
		User oldUser = getUser(uID);
		System.out.println(" from updatedUser");
		String updateStatement;
		
		// get data from updated User
		String lastName = updatedUser.getLastName();
		String name = updatedUser.getName();
		int birthDate = updatedUser.getBirthDate();
		String eMail = updatedUser.geteMail();
		String password = updatedUser.getPassword();
		
		if(lastName == null || eMail == null || password == null) { // stops when password, eMail or lastName is empty
			System.out.println("lastName, eMail or password was set to 'null, which is not allowed");
			return;
		}
		
		if (oldUser.getUserID() != updatedUser.getUserID()) { // stops when userID would change
			System.out.println("You tried to change the the userID of oldUser " + oldUser.getUserID() + ". This is not allowed !!");
			return;
		}
		
		Connection con = getDBConnection(url, userName, password);
		
		//insert new user Data into prepared Statement
		updateStatement = " UPDATE users "
				+ "Set  lastName    = \"" + lastName + "\""
					+ ",name        = \"" + name     + "\""
					+ ",birthDate   =   " + birthDate
					+ ",eMail       = \"" + eMail    + "\""
					+ ",password    = \"" + password + "\""
					+ "WHERe userID =  " + uID + " ";
				
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
			con.close();
		}
				
	}
	
	/**
	 * deletes a user from Database
	 * @param userID the userID of the user that will be delted
	 * throws error when user is not existing
	 * throw exception when process fails
	 * @throws Exception 
	 */
	public void deleteUserObject(int userID) throws Exception {
		
		int uID = userID;
		 
		 Connection con = getDBConnection(url, userName, password);
		 
		 String query = (String) "Delete from users where userID = " + uID;
		 PreparedStatement pst;
		 try {
			pst = con.prepareStatement(query);
			pst.executeUpdate();
			System.out.println("Deleted user with userID: " + uID + " from usersDB.db");
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to execute delete query");
			e.printStackTrace();
			// add exception when the user which is supposed to be deleted does not exist
			con.close();
		} 
	}
	
	
	 /* connects to users database
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
