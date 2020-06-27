package server.New;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Queue for sending messages to other clients. Use {@link #add(byte[])} to send a new message to
 * a client.
 *
 */
public class SendQueue {
	private static Queue<byte[]> queue = new LinkedList<byte[]>();
	
	/**
	 * Adds a message to the queue.
	 * @param messageIncludingMeta the message to be added (including meta information!)
	 */
	public static void add(byte[] messageIncludingMeta) {
		if(messageIncludingMeta == null) return;
		
		queue.add(messageIncludingMeta);
	}
	
	/**
	 * Retrieves and removes the oldest item from the queue. Returns null if the queue is empty.
	 * @return the oldest item from the queue
	 */
	static byte[] poll() {
		return queue.poll();
	}
	
	/**
	 * Checks if the queue is empty.
	 * @return if the queue is empty
	 */
	public static boolean isEmpty() {
		return queue.size() == 0;
	}
}
