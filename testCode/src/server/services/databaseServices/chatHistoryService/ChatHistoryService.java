package server.services.databaseServices.chatHistoryService;

import java.util.Date;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.services.Service;
import server.services.databaseServices.DBManager;
import server.services.databaseServices.DatabaseMessage;

public class ChatHistoryService extends Service{
	private static volatile ChatHistoryService singleton = new ChatHistoryService();

	private ChatHistoryService() {
		super("chat-history-service");
	}
	
	public static ChatHistoryService getInstance() {
		return singleton;
	}
	
	private volatile DatabaseMessage[] messages = null;
	private volatile Date lastUpdate = null;
	
	@Override
	public void run() {
		super.run();
		while(!requestedShutDown()) {
			setMessages(DBManager.getInstance().getAllMessages());
			setLastUpdate(new Date(System.currentTimeMillis()));
			
			Logger.debug(this.getClass().getSimpleName(), "Successfully synchonized message array with database");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Logger.error(this.getClass().getSimpleName(), "chat-history-service interrupted");
			}
		}
	}
	
	public DatabaseMessage[] getMessages() {
		return messages;
	}

	private void setMessages(DatabaseMessage[] messages) {
		this.messages = messages;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	private void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
