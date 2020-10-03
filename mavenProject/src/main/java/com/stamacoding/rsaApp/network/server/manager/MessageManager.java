package com.stamacoding.rsaApp.network.server.manager;

import java.util.ArrayList;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.service.message.ReceiveService;
import com.stamacoding.rsaApp.network.global.message.AbstractMessageManager;
import com.stamacoding.rsaApp.network.global.message.Message;

public class MessageManager extends AbstractMessageManager{
	
	/** The only instance of this class */
	private volatile static MessageManager singleton = new MessageManager();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private MessageManager() {
		
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static MessageManager getInstance() {
		return singleton;
	}
	
	/**
	 * Retrieves and removes an {@link ArrayList} of messages from the {@link MessageManager} that are concerned to a client. Added to that these messages get encrypted.
	 * @param clientId the client's id
	 * @return an {@link ArrayList} containing all messages that are concerned to the client with the specified id
	 */
	public ArrayList<Message> poll(String username) {
		ArrayList<Message> messagesToSend = new ArrayList<Message>();
		for(int i=0; i<getCurrentlyManagedMessages().size(); i++) {
			
			if(getCurrentlyManagedMessages().get(i).getServerData().getReceiving().equals(username)) {
				
				messagesToSend.add(getCurrentlyManagedMessages().get(i));
				L.d(this.getClass(), "Found message to send: " + messagesToSend.get(messagesToSend.size()-1));
				
				getCurrentlyManagedMessages().get(i).encrypt();
				L.t(this.getClass(), "Encrypted message:" + messagesToSend.get(messagesToSend.size()-1));
			}
		}
		for(int i=0; i<messagesToSend.size(); i++) {
			getCurrentlyManagedMessages().remove(messagesToSend.get(i));
			L.t(this.getClass(), "Removed message:" + messagesToSend.get(i));
		}
		return messagesToSend;
	}
}
