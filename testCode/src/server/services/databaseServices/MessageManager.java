package server.services.databaseServices;

import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.config.NetworkConfig;
import server.config.NetworkType;
import server.services.Message;
import server.services.RsaState;

public class MessageManager {
	/**
	 * array containing all messages stored in the chat database
	 */
	private static volatile ArrayList<Message> messages = new ArrayList<Message>();
	
	{
		MessageManager.manage(DBManager.getInstance().getMessagesFromDB());
	}
	
	
	/**
	 * Gets an array containing all messages stored in the chat database.
	 * @return array containing all messages stored in the chat database
	 */
	public static ArrayList<Message> getAllMessages() {
		return messages;
	}
	
	public static class Client{
		public static Message getMessageToStoreOrUpdate() {
			for(int i=0; i<getAllMessages().size(); i++) {
				if(getAllMessages().get(i).isToStore() || getAllMessages().get(i).isToUpdate()) {
					return getAllMessages().get(i);
				}
			}
			return null;
		}
		
		public static Message getMessageToSend() {
			for(int i=0; i<getAllMessages().size(); i++) {
				if(getAllMessages().get(i).isToSend()) {
					return getAllMessages().get(i);
				}
			}
			return null;
		}
	}
	
	public static class Server{
		public static ArrayList<Message> poll(byte clientId) {
			ArrayList<Message> messagesToSend = new ArrayList<Message>();
			for(int i=0; i<getAllMessages().size(); i++) {
				if(getAllMessages().get(i).getReceivingId() == clientId) {
					messagesToSend.add(getAllMessages().get(i));
				}
			}
			for(Message m : messagesToSend) {
				getAllMessages().remove(m);
			}
			return messagesToSend;
		}
	}
	
	public static void manage(Message...messages) {
		for(Message m : messages) {
			if(getAllMessages().indexOf(m) == -1) {
				getAllMessages().add(m);
				
				// Decode messages when running on client
				if(NetworkConfig.TYPE == NetworkType.CLIENT) m.setRsaState(RsaState.DECODED);
				
				Logger.debug(MessageManager.class.getSimpleName(), "Added new message to MessageManager");
			}
		}
	}
}
