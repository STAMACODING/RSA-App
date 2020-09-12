package com.stamacoding.rsaApp.network.client.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.network.client.Client;
import com.stamacoding.rsaApp.network.client.managers.ClientMessageManager;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.global.message.data.LocalData;
import com.stamacoding.rsaApp.network.global.message.data.ProtectedData;
import com.stamacoding.rsaApp.network.global.message.data.ServerData;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.global.service.database.DatabaseConfiguration;
import com.stamacoding.rsaApp.network.global.service.database.DatabaseService;

/**
 * {@link Service} storing and updating messages in the chat database using the {@link ClientMessageManager}.
 */
public class ChatDatabaseService extends DatabaseService{

	/** The only instance of this class */
	private volatile static ChatDatabaseService singleton = new ChatDatabaseService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ChatDatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	public ChatDatabaseService() {
		super("ChatDatabaseService", 
				new DatabaseConfiguration(
						"jdbc:sqlite:ClientDatabase.db",
						"root",
						"root"));
	}

	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ChatDatabaseService getInstance() {
		return singleton;
	}

	/**
	 * If {@link Client#pollToStoreOrUpdate()} returns a message, this message will get updated/stored.
	 * @see Service#onRepeat()
	 */
	@Override
	public void onRepeat() {
		// Check if there is any message to store or update
		Message m = ClientMessageManager.getInstance().pollToStoreOrUpdate();
		
		if(m != null) {
			Logger.debug(this.getClass().getSimpleName(), "Got message to store/update: " + m.toString());
			
			// Update message if is is already stored in the chat database
			if(m.getLocalData().isToUpdate()) {
				if(!updateMessage(m) && m.getLocalData().isToUpdate()) {
					Logger.warning(this.getClass().getSimpleName(), "Readding message to the message manager to get updated again");
					m.getLocalData().setUpdateRequested(true);
					ClientMessageManager.getInstance().manage(m);
				}
			}
			
			// Store new message
			else{
				if(!storeMessage(m) && !m.isStored()) {
					Logger.warning(this.getClass().getSimpleName(), "Readding message to the message manager to get stored again");
					ClientMessageManager.getInstance().manage(m);
				}
			}
			
		}
	}
	
	/**
	 * Updates the message in the chat database
	 * @param m the message to update
	 */
	private boolean updateMessage(Message m) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot update message! You aren't connected to the chat database");
			return false;
		}
		
		if(!m.isStored()) {
			Logger.error(this.getServiceName(), "Cannot update unstored message!");
			return false;
		}else if(m.isEncrypted()) {
			Logger.error(this.getServiceName(), "Cannot update encrypted message!");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("UPDATE Messages SET "
					+ "textMessage = ?, "
					+ "date = ?, "
					+ "sendState = ?, "
					+ "sending = ?, "
					+ "receiving = ? "
					+ "WHERE id = ?;");
			pst.setString(1, m.getProtectedData().getTextMessage());
			pst.setLong(2, m.getProtectedData().getDate());
			pst.setInt(3, LocalData.getSendStateAsInt(m.getLocalData().getSendState()));
			pst.setString(4, m.getServerData().getSending());
			pst.setString(5, m.getServerData().getReceiving());
			pst.setLong(6, m.getLocalData().getId());
			
			pst.executeUpdate();
			
			Logger.debug(this.getClass().getSimpleName(), "Updated message: " + m.toString());
			m.getLocalData().setUpdateRequested(false);
			pst.close();
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to update message (SQL exception)");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Stores the message in the chat database
	 * @param m the message to store
	 */
	private boolean storeMessage(Message m) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot store message! You aren't connected to the chat database");
			return false;
		}
		
		if(m.isStored()) {
			Logger.error(this.getClass().getSimpleName(), "Cannot store already stored message!");
			return false;
		}else if(m.isEncrypted()) {
			Logger.error(this.getClass().getSimpleName(), "Cannot store encrypted message!");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("INSERT INTO Messages "
					+ "(textMessage, date, sendState, sending, receiving) VALUES( "
					+ "?, ?, ?, ?, ?);");
			pst.setString(1, m.getProtectedData().getTextMessage());
			pst.setLong(2, m.getProtectedData().getDate());
			pst.setInt(3, LocalData.getSendStateAsInt(m.getLocalData().getSendState()));
			pst.setString(4, m.getServerData().getSending());
			pst.setString(5, m.getServerData().getReceiving());
			
			pst.executeUpdate();
			
			Statement statement = getConnection().createStatement();
			ResultSet res = statement.executeQuery("SELECT MAX(id) AS LAST FROM Messages");
			long id = Long.parseLong(res.getString("LAST"));
			m.getLocalData().setId(id);
			
			Logger.debug(this.getClass().getSimpleName(), "Stored message: " + m.toString());
			m.getLocalData().setUpdateRequested(false);
			pst.close();
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to store message (SQL exception)");
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean deleteMessage(Message m) {
		if(!m.isStored()) {
			Logger.error(this.getClass().getSimpleName(), "Cannot delete an unstored message!");
			return false;
		}
		return deleteMessage(m.getLocalData().getId());
	}
	
	private boolean deleteMessage(long id){
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot delete message! You aren't connected to the chat database");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("DELETE FROM Messages WHERE id = ?;");
			pst.setLong(1, id);
			
			pst.executeUpdate();
			pst.close();
			Logger.debug(this.getClass().getSimpleName(), "Deleted message using id(" + id + ")");
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to delete message (SQL exception)");
			e.printStackTrace();
		}
		return false;
	}
	
	
	private Message getMessage(long id) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot get message! You aren't connected to the chat database");
			return null;
		}
		
		try {
			Statement stm = getConnection().createStatement();
			ResultSet res = stm.executeQuery("SELECT "
					+ "id, textMessage, "
					+ "date, sendState, "
					+ "sending, receiving "
					+ "FROM Messages WHERE id = " + id + ";");
			if(res != null && res.next()) {
				Message m = new Message(
						new LocalData(res.getInt(1), LocalData.getIntAsSendState(res.getInt(4))), 
						new ProtectedData(res.getString(2), res.getLong(3)), 
						new ServerData(res.getString(5), res.getString(6)));
				
				Logger.debug(this.getClass().getSimpleName(), "Got message: " + m.toString());
				return m;
			}
			Logger.warning(this.getClass().getSimpleName(), "Didn't find any message with this id!");
			return null;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to get message (SQL exception)");
			e.printStackTrace();
		}
		return null;
	}
	
	private ArrayList<Message> getPendingMessages() {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot get pending messages! You aren't connected to the chat database");
			return null;
		}
		
		try {
			Statement stm = getConnection().createStatement();
			ResultSet res = stm.executeQuery("SELECT "
					+ "id, textMessage, "
					+ "date, sendState, "
					+ "sending, receiving "
					+ "FROM Messages WHERE sendState = 0;");
			
			if(res != null) {
				ArrayList<Message> messages = new ArrayList<Message>();
				
				while(res.next()) {
					Message m = new Message(
							new LocalData(res.getInt(1), LocalData.getIntAsSendState(res.getInt(4))), 
							new ProtectedData(res.getString(2), res.getLong(3)), 
							new ServerData(res.getString(5), res.getString(6)));
					messages.add(m);
				}
				Logger.debug(this.getClass().getSimpleName(), "Got (" + messages.size() + ") pending messages");
				return messages;
			}
			Logger.debug(this.getClass().getSimpleName(), "Got (0) pending messages");
			return null;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to get pending messages (SQL exception)");
			e.printStackTrace();
		}
		return null;
	}

	private ArrayList<Message> getMessages() {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot get messages! You aren't connected to the chat database");
			return null;
		}
		
		try {
			Statement stm = getConnection().createStatement();
			ResultSet res = stm.executeQuery("SELECT "
					+ "id, textMessage, "
					+ "date, sendState, "
					+ "sending, receiving "
					+ "FROM Messages;");
			
			if(res != null) {
				ArrayList<Message> messages = new ArrayList<Message>();
				
				while(res.next()) {
					Message m = new Message(
							new LocalData(res.getInt(1), LocalData.getIntAsSendState(res.getInt(4))), 
							new ProtectedData(res.getString(2), res.getLong(3)), 
							new ServerData(res.getString(5), res.getString(6)));
					messages.add(m);
				}
				Logger.debug(this.getClass().getSimpleName(), "Got (" + messages.size() + ") messages");
				return messages;
			}
			Logger.debug(this.getClass().getSimpleName(), "Got (0) messages");
			return null;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to get messages (SQL exception)");
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean deleteMessages() {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot delete messages! You aren't connected to the chat database");
			return false;
		}
		
		try {
			Statement stm = getConnection().createStatement();
			stm.executeUpdate("DELETE FROM Messages;");
			stm.close();
			Logger.debug(this.getClass().getSimpleName(), "Deleted messages!");
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to delete messages (SQL exception)");
			e.printStackTrace();
		}
		return false;
	}
	
	public String toString() {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot print database! You aren't connected to the chat database");
			return "[ NOT CONNECTED TO DATBASE ]";
		}
		ArrayList<Message> messages = getMessages();
		
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<127; i++) sb.append("#");
		sb.append('\n');
		sb.append(String.format("| %-18s | %-18s | %-18s | %-18s | %-18s | %-18s |\n", "id", "textMessage", "date", "sendState", "sending", "receiving"));

		for(int i=0; i<127; i++) sb.append("#");
		sb.append('\n');
		
		if(messages.size() == 0) {
			sb.append("\t Database is empty!\n");
		}
		
		for(int i=0; i<messages.size(); i++) {
			Message m = messages.get(i);
			sb.append(String.format("| %-18s | %-18s | %-18s | %-18s | %-18s | %-18s |\n", 
					m.getLocalData().getId(),
					m.getProtectedData().getTextMessage(),
					new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(m.getProtectedData().getDate()),
					m.getLocalData().getSendState(),
					m.getServerData().getSending(),
					m.getServerData().getReceiving()));
			if(i+1<messages.size()) {
				for(int j=0; j<127; j++) sb.append("-");
				sb.append('\n');
			}
		}
		for(int i=0; i<127; i++) sb.append("#");
		return sb.toString();
	}


	@Override
	protected void initialize() {
		try {
			Statement statement = getConnection().createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS Messages ("
					+ "id INTEGER PRIMARY KEY CHECK ((id > 0)), "
					+ "textMessage TEXT NOT NULL CHECK (LENGTH(textMessage) > 0), "
					+ "date INTEGER NOT NULL CHECK ((date > 0)), "
					+ "sendState INT NOT NULL CHECK ((sendState = 0) OR (sendState = 1)), "
					+ "sending VARCHAR (15) NOT NULL CHECK (LENGTH(sending) > 0), "
					+ "receiving VARCHAR (15) NOT NULL CHECK (LENGTH(receiving) > 0));");
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to initialize database! (SQL Exception)");
			e.printStackTrace();
		}
		Logger.debug(this.getServiceName(), "Logging chat database...\n" + this.toString());

		ClientMessageManager.getInstance().manage(getPendingMessages());
	}
}
