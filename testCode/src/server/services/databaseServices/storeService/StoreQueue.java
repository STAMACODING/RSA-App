package server.services.databaseServices.storeService;

import java.util.LinkedList;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.services.databaseServices.DatabaseMessage;

public class StoreQueue {
	private static volatile LinkedList<DatabaseMessage> queue = new LinkedList<DatabaseMessage>();
	
	/**
	 * Adds a message to the queue.
	 * @param message the message to send
	 */
	public static void add(DatabaseMessage message) {
		getQueue().add(message);
		Logger.debug(StoreQueue.class.getSimpleName(), "Added message to queue");
	}
	
	/**
	 * Retrieves and removes the oldest message from the queue. Returns null if the queue is empty.
	 * @return the oldest message from the queue
	 */
	public static DatabaseMessage poll() {
		return getQueue().poll();
	}
	
	/**
	 * Checks if the queue is empty.
	 * @return if the queue is empty
	 */
	public static boolean isEmpty() {
		return getQueue().size() == 0;
	}

	private static LinkedList<DatabaseMessage> getQueue() {
		return queue;
	}
}
