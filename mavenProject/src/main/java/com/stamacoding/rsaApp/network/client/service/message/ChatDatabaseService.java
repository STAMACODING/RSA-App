package com.stamacoding.rsaApp.network.client.service.message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.TextUtils;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.global.message.data.LocalData;
import com.stamacoding.rsaApp.network.global.message.data.ProtectedData;
import com.stamacoding.rsaApp.network.global.message.data.SendState;
import com.stamacoding.rsaApp.network.global.message.data.ServerData;
import com.stamacoding.rsaApp.network.global.service.Service;
import com.stamacoding.rsaApp.network.global.service.executor.database.DatabaseConfiguration;
import com.stamacoding.rsaApp.network.global.service.executor.database.DatabaseService;
import com.stamacoding.rsaApp.network.server.manager.MessageManager;

/**
 * {@link Service} storing and updating messages in the chat database using the {@link MessageManager}.
 */
public class ChatDatabaseService extends DatabaseService{

	/** The only instance of this class */
	private volatile static ChatDatabaseService singleton = new ChatDatabaseService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ChatDatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	public ChatDatabaseService() {
		super(new DatabaseConfiguration(
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
	 * Updates the message in the chat database
	 * @param m the message to update
	 */
	public boolean updateMessage(Message m) {
		validateThread();
		validateConnection();
		
		if(!m.isStored()) {
			L.e(this.getClass(), "Cannot update unstored message!");
			return false;
		}else if(m.isEncrypted()) {
			L.e(this.getClass(), "Cannot update encrypted message!");
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
			pst.setInt(3, SendState.asInt(m.getLocalData().getSendState()));
			pst.setString(4, m.getServerData().getSending());
			pst.setString(5, m.getServerData().getReceiving());
			pst.setLong(6, m.getLocalData().getId());
			
			pst.executeUpdate();
			
			L.i(this.getClass(), "Updated message: " + m.toString());
			pst.close();
			L.i(this.getClass(), "Logging database content \n" + toString());
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to update message", e);
		}
		return false;
	}
	
	/**
	 * Stores the message in the chat database
	 * @param m the message to store
	 */
	public boolean storeMessage(Message m) {
		validateThread();
		validateConnection();
		
		if(m.isStored()) {
			L.e(this.getClass(), "Cannot store already stored message!");
			return false;
		}else if(m.isEncrypted()) {
			L.e(this.getClass(), "Cannot store encrypted message!");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("INSERT INTO Messages "
					+ "(textMessage, date, sendState, sending, receiving) VALUES( "
					+ "?, ?, ?, ?, ?);");
			pst.setString(1, m.getProtectedData().getTextMessage());
			pst.setLong(2, m.getProtectedData().getDate());
			pst.setInt(3, SendState.asInt(m.getLocalData().getSendState()));
			pst.setString(4, m.getServerData().getSending());
			pst.setString(5, m.getServerData().getReceiving());
			
			pst.executeUpdate();
			
			Statement statement = getConnection().createStatement();
			ResultSet res = statement.executeQuery("SELECT MAX(id) AS LAST FROM Messages");
			long id = Long.parseLong(res.getString("LAST"));
			m.getLocalData().setId(id);
			
			L.i(this.getClass(), "Stored message: " + m.toString());
			pst.close();

			L.i(this.getClass(), "Logging database content \n" + toString());
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to store message", e);
		}
		return false;
	}
	
	public boolean deleteMessage(Message m) {
		return deleteMessage(m.getLocalData().getId());
	}
	
	public boolean deleteMessage(long id){
		validateThread();
		validateConnection();
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("DELETE FROM Messages WHERE id = ?;");
			pst.setLong(1, id);
			
			pst.executeUpdate();
			pst.close();
			L.i(this.getClass(), "Deleted message using id(" + id + ")");
			L.i(this.getClass(), "Logging database content \n" + toString());
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to delete message" , e);
		}
		return false;
	}
	
	
	public Message getMessage(long id) {
		validateThread();
		validateConnection();
		
		try {
			Statement stm = getConnection().createStatement();
			ResultSet res = stm.executeQuery("SELECT "
					+ "id, textMessage, "
					+ "date, sendState, "
					+ "sending, receiving "
					+ "FROM Messages WHERE id = " + id + ";");
			if(res != null && res.next()) {
				Message m = new Message(
						new LocalData(res.getInt(1), SendState.parseInt(res.getInt(4))), 
						new ProtectedData(res.getString(2), res.getLong(3)), 
						new ServerData(res.getString(5), res.getString(6)));
				
				L.d(this.getClass(), "Got message: " + m.toString());
				return m;
			}
			L.w(this.getClass(), "Didn't find any message with this id!");
			return null;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to get message", e);
		}
		return null;
	}
	
	public ArrayList<Message> getMessagesToSend() {
		validateThread();
		validateConnection();
		
		try {
			Statement stm = getConnection().createStatement();
			ResultSet res = stm.executeQuery("SELECT "
					+ "id, textMessage, "
					+ "date, sendState, "
					+ "sending, receiving "
					+ "FROM Messages WHERE sendState = " + SendState.asInt(SendState.PENDING) + " OR sendState = " + SendState.asInt(SendState.FAILED) + ";");
			
			if(res != null) {
				ArrayList<Message> messages = new ArrayList<Message>();
				
				while(res.next()) {
					SendState databaseSendState = SendState.parseInt(res.getInt(4));
					Message m = new Message(
							new LocalData(res.getInt(1), SendState.parseInt(res.getInt(4))), 
							new ProtectedData(res.getString(2), res.getLong(3)), 
							new ServerData(res.getString(5), res.getString(6)));
					messages.add(m);
				}
				L.d(this.getClass(), "Got (" + messages.size() + ") to send");
				return messages;
			}
			L.d(this.getClass(), "Got (0) to send");
			return null;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to get pending/failed messages", e);
		}
		return null;
	}

	public ArrayList<Message> getMessages() {
		validateThread();
		validateConnection();
		
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
							new LocalData(res.getInt(1), SendState.parseInt(res.getInt(4))), 
							new ProtectedData(res.getString(2), res.getLong(3)), 
							new ServerData(res.getString(5), res.getString(6)));
					messages.add(m);
				}
				L.d(this.getClass(), "Got (" + messages.size() + ") messages");
				return messages;
			}
			L.d(this.getClass(), "Got (0) messages");
			return null;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to get messages", e);
		}
		return null;
	}
	
	public boolean deleteMessages() {
		validateThread();
		validateConnection();
		
		try {
			Statement stm = getConnection().createStatement();
			stm.executeUpdate("DELETE FROM Messages;");
			stm.close();
			L.i(this.getClass(), "Deleted messages!");
			L.i(this.getClass(), "Logging database content \n" + toString());
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to delete messages", e);
		}
		return false;
	}
	
	public String toString() {
		validateThread();
		validateConnection();
		
		ArrayList<Message> messages = getMessages();
		
		StringBuilder sb = new StringBuilder();
		
		int length = 137;
		for(int i=0; i<length; i++) sb.append("#");
		sb.append('\n');
		sb.append(String.format("| %-18s | %-28s | %-18s | %-18s | %-18s | %-18s |\n", "id", "textMessage", "date", "sendState", "sending", "receiving"));

		for(int i=0; i<length; i++) sb.append("#");
		sb.append('\n');
		
		if(messages.size() == 0) {
			sb.append("\t Database is empty!\n");
		}
		
		for(int i=0; i<messages.size(); i++) {
			Message m = messages.get(i);
			sb.append(String.format("| %-18s | %-28s | %-18s | %-18s | %-18s | %-18s |\n", 
					m.getLocalData().getId(),
					TextUtils.cut(m.getProtectedData().getTextMessage(), 28),
					new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(m.getProtectedData().getDate()),
					m.getLocalData().getSendState(),
					TextUtils.cut(m.getServerData().getSending(), 18),
					TextUtils.cut(m.getServerData().getReceiving(), 18)));
			if(i+1<messages.size()) {
				for(int j=0; j<length; j++) sb.append("-");
				sb.append('\n');
			}
		}
		for(int i=0; i<length; i++) sb.append("#");
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
					+ "sendState INT NOT NULL CHECK ((sendState = 0) OR (sendState = 1) OR (sendState = -1)), "
					+ "sending VARCHAR (15) NOT NULL CHECK (LENGTH(sending) > 0), "
					+ "receiving VARCHAR (15) NOT NULL CHECK (LENGTH(receiving) > 0));");
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to initialize database!", e);
		}
		L.d(this.getClass(), "Logging chat database...\n" + this.toString());

		ArrayList<Message> oldPendingMessages = getMessagesToSend();
		for(int i=0; i<oldPendingMessages.size(); i++) {
			Message m = oldPendingMessages.get(i);
			SendService.getInstance().execute(new Callable<Object>() {
				
				@Override
				public Object call() throws Exception {
					return SendService.getInstance().send(m);
				}
			});
		}
	}
}
