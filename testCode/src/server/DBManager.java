package server;

import com.stamacoding.rsaApp.log.logger.Logger;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager implements MessageDBInterface{

	private final String url = "jdbc:sqlite:ChatHistory.Test.db";
	private final String userName = "root";
	private final String password = "root";

	public  void setUpConnection() throws Exception{
		
		try {
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection(url, userName, password);
			Logger.debug(Server.class.getSimpleName(), "Connectin to ChatHistory DB succesful");
			
			
		}catch(Exception e) {
			e.printStackTrace();
			Logger.debug(Server.class.getSimpleName(), "Connection to ChatHistory DB failed");
		}
		
	}
	
	public void addMessageToDb(Message m) {
		
	}
	
	public void updateMessage(Message mUpdatedMessage) {
		
	}
	
	public Message[] getMessagesFromDb() {
		
	}
	
}
