package com.stamacoding.rsaApp.server.server.managers;

import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.client.services.ClientReceiveService;
import com.stamacoding.rsaApp.server.message.AbstractMessageManager;
import com.stamacoding.rsaApp.server.message.Message;

public class ServerMessageManager extends AbstractMessageManager{
	
	/** The only instance of this class */
	private volatile static ServerMessageManager singleton = new ServerMessageManager();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ServerMessageManager() {
		
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ServerMessageManager getInstance() {
		return singleton;
	}
	
	/**
	 * Retrieves and removes an {@link ArrayList} of messages from the {@link MessageManager} that are concerned to a client. Added to that these messages get encrypted.
	 * @param clientId the client's id
	 * @return an {@link ArrayList} containing all messages that are concerned to the client with the specified id
	 */
	public ArrayList<Message> poll(String username) {
		ArrayList<Message> messagesToSend = new ArrayList<Message>();
		for(int i=0; i<getAllMessages().size(); i++) {
			if(getAllMessages().get(i).getServerData().getReceiving().equals(username)) {
				messagesToSend.add(getAllMessages().get(i));
				Logger.debug(this.getClass().getSimpleName(), "Polling message: " + messagesToSend.get(i).toString());
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
