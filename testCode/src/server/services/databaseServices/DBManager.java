package server.services.databaseServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
	

	private final String url = "jdbc:sqlite:ChatHistory.Test.db";
	private final String userName = "root";
	private final String password = "root";

	public void setUpConnection() throws Exception{
		
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection(url, userName, password);
			Logger.debug(this.getClass().getSimpleName(), "Connectin to ChatHistory DB succesful");
			
			Statement state = con.createStatement();
			Logger.debug(this.getClass().getSimpleName(), "Statement instance initiated");
			
			ResultSet rs = state.executeQuery("SELECT * from Chats");
			//getMessagesFromDb(state);
			
		}catch(Exception e) {
			e.printStackTrace();
			Logger.debug(this.getClass().getSimpleName(), "Connection to ChatHistory DB failed");
		}
		
	}
	
	/**
	 * Stores a message in the chat database.
	 * @param m the message to store
	 */
	public void addMessageToDB(DatabaseMessage m) {
		
	}
	
	public void updateMessage(DatabaseMessage updatedMessage, Statement st) {
		Statement state = st;
		try {
			ResultSet rs = state.executeQuery("SELECT * from Test");
			
			
		}catch(Exception e) {
			e.printStackTrace();
			Logger.debug(this.getClass().getSimpleName(),"Failed to get Messages from DB due to failed execution of Query");
		}
	}
	
	public DatabaseMessage[] getMessagesFromDB() {
		return null;
	}
	
	public static void main(String [] args) {
		try {
			DBManager.getInstance().setUpConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
