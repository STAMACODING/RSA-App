package server.services.databaseServices;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.stamacoding.rsaApp.log.logger.Logger;

/**
 * Interface to the client's chat database.
 *  <ul>
 *  <li> Use <code>DBManager.getInstance()</code> to get an instance of this object.</li>
 * </ul>
 *
 */
public class DBManager{
	/**
	 * the only instance of this class
	 */
	private volatile static DBManager singleton = new DBManager();
	
	private DBManager() {}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class.
	 */
	public static DBManager getInstance() {
		return singleton;
	}
	

	private final String url = "jdbc:sqlite:DB/UserDatabase.db";
	private final String userName = "root";
	private final String password = "root";

	public Connection connect() throws Exception{
		
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url, userName, password);
			Logger.debug(this.getClass().getSimpleName(), "Connectin to Connection to DB/UserDatabse.db succesful");
			
			return con;
			
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			Logger.debug(this.getClass().getSimpleName(), "Connection to DB/UserDatabse.db failed");
			return con;
		}
		
	}
	
	/**
	 * Stores a message in the chat database.
	 * @param m the message to store
	 */
	public void addMessageToDB(DatabaseMessage m) {
		
	}
	
	public void updateMessage(DatabaseMessage updatedMessage) {
		
	}
	
	public DatabaseMessage[] getMessagesFromDB() {
		
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
			e.printStackTrace();
			Logger.debug(this.getClass().getSimpleName(),"Failed to get Messages from DB due to failed execution of Query");
		}
		
		return null;
	}
	
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
	
	
	public static void main(String [] args) {
		try {
			DatabaseMessage[] messges = DBManager.getInstance().getMessagesFromDB();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}