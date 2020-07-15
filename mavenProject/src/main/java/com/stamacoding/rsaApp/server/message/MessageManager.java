package com.stamacoding.rsaApp.server.message;

import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.config.NetworkConfig;
import com.stamacoding.rsaApp.server.config.NetworkType;
import com.stamacoding.rsaApp.server.services.databaseService.DBManager;
import com.stamacoding.rsaApp.server.services.mainService.MessageService;

/**
 * Static class containing all stored, received, sending and sent messages. Provides functions to filter messages by their attributes.
 */
public class MessageManager {
	
	/** {@link ArrayList} containing all messages stored in the chat database */
	private static volatile ArrayList<Message> messages = new ArrayList<Message>();
	
	/*
	 * Queries all messages from the chat database after starting the application.
	 */
	static{
		if(NetworkConfig.TYPE == NetworkType.CLIENT) {
			Logger.debug(MessageManager.class.getSimpleName(), "Querying stored messages from chat database");
			Message[] storedMessages = DBManager.getInstance().getMessagesFromDB();
			if(storedMessages != null)MessageManager.manage(storedMessages);
		}
	}
	
	
	/**
	 * Gets an {@link ArrayList} containing all messages stored in the chat database.
	 * @return {@link ArrayList} containing all messages stored in the chat database
	 */
	public static ArrayList<Message> getAllMessages() {
		return messages;
	}
	
	/**
	 * Contains functions that are useful for the client-side application.
	 */
	public static class Client{
		
		/**
		 * Gets a message from the {@link MessageManager} that should get stored or updated in the client's chat database.
		 * Returns {@code null} if there is no message to store or update.
		 * @return the message that should get stored or updated
		 */
		public static Message getMessageToStoreOrUpdate() {
			for(int i=0; i<getAllMessages().size(); i++) {
				if(getAllMessages().get(i).getLocalData().isToStore() || getAllMessages().get(i).getLocalData().isToUpdate()) {
					return getAllMessages().get(i);
				}
			}
			return null;
		}
		
		/**
		 * Gets a message from the {@link MessageManager} that should get sent.
		 * Returns {@code null} if there is no message to store or update.
		 * @return the message that should get sent
		 */
		public static Message getMessageToSend() {
			for(int i=0; i<getAllMessages().size(); i++) {
				if(getAllMessages().get(i).getLocalData().isToSend()) {
					return getAllMessages().get(i);
				}
			}
			return null;
		}
	}
	
	/**
	 * Contains functions that are useful for the server-side application.
	 */
	public static class Server{
		
		/**
		 * Retrieves and removes an {@link ArrayList} of messages from the {@link MessageManager} that are concerned to a client. Added to that these messages get encrypted.
		 * @param clientId the client's id
		 * @return an {@link ArrayList} containing all messages that are concerned to the client with the specified id
		 */
		public static ArrayList<Message> poll(byte clientId) {
			ArrayList<Message> messagesToSend = new ArrayList<Message>();
			for(int i=0; i<getAllMessages().size(); i++) {
				if(getAllMessages().get(i).getServerData().getReceivingId() == clientId) {
					messagesToSend.add(getAllMessages().get(i));
					Logger.debug(MessageManager.class.getSimpleName(), "Polling message: " + messagesToSend.get(i).toString());
					getAllMessages().get(i).encryptServerData();
					getAllMessages().get(i).encryptProtectedData();
				}
			}
			for(Message m : messagesToSend) {
				getAllMessages().remove(m);
			}
			return messagesToSend;
		}
	}
	
	/**
	 * Adds a single or multiple {@link Message}(s) to the {@link MessageManager}. If the {@link MessageService} is running, this will lead to messages
	 * getting stored, updated and sent automatically.
	 * @param messages to get managed
	 */
	public static void manage(Message...messages) {
		for(Message m : messages) {
			if(getAllMessages().indexOf(m) == -1) {
				getAllMessages().add(m);
				
				Logger.debug(MessageManager.class.getSimpleName(), "Added new message to MessageManager");
			}
		}
	}
}
