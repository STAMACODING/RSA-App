package server.services.databaseServices;

import java.sql.Connection;
import java.sql.DriverManager;

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
			Connection c = DriverManager.getConnection(url, userName, password);
			Logger.debug(this.getClass().getSimpleName(), "Connectin to ChatHistory DB succesful");
			
			
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
	
	public void updateMessage(DatabaseMessage updatedMessage) {
		
	}
	
	public DatabaseMessage[] getMessagesFromDB() {
		return null;
	}
	
}
