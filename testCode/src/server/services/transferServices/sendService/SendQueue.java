package server.services.transferServices.sendService;


import java.util.ArrayList;
import java.util.LinkedList;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.config.NetworkConfig;
import server.config.Type;
import server.services.transferServices.TransferMessage;

/**
 * Queue for sending messages. Only works if the {@link SendService} is running.
 * <ul>
 * 	<li>Use {@link #add(TransferMessage)} to add messages that should be sent. </li>
 * </ul>
 */
public class SendQueue{
	private static volatile LinkedList<TransferMessage> queue = new LinkedList<TransferMessage>();
	
	/**
	 * Adds a message to the queue.
	 * @param message the message to send
	 */
	public static void add(TransferMessage message) {
		getQueue().add(message);
		Logger.debug(SendQueue.class.getSimpleName(), "Added message to queue");
	}
	
	/**
	 * Retrieves and removes the oldest message from the queue. Returns null if the queue is empty.
	 * @return the oldest message from the queue
	 */
	public static TransferMessage poll() {
		return getQueue().poll();
	}
	
	/**
	 * Checks if the queue is empty.
	 * @return if the queue is empty
	 */
	public static boolean isEmpty() {
		return getQueue().size() == 0;
	}
	
	/**
	 * Retrieves and removes all messages concerning the client. Only works correctly if you are on the server.
	 * @param clientId the client's id
	 * @return all messages concerning the client
	 */
	public static ArrayList<TransferMessage> pollMessages(byte clientId){
		if(NetworkConfig.TYPE == Type.CLIENT) throw new RuntimeException("The method pollMessages() only works on server.");
		ArrayList<TransferMessage> messages = new ArrayList<TransferMessage>();
		for(int i = 0; i<getQueue().size(); i++) {
			if(getQueue().get(i).getReceivingId() == clientId) {
				messages.add(getQueue().get(i));
			}
		}
		getQueue().removeAll(messages);
		return messages;
	}
	
	private static LinkedList<TransferMessage> getQueue() {
		return queue;
	}
}
