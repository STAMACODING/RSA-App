package server;

import java.util.ArrayList;
import java.util.LinkedList;

import com.stamacoding.rsaApp.log.logger.Logger;

/**
 * Queue for sending messages to other clients. Use {@link #add(byte[])} to send a new message to
 * a client.
 *
 */
public class SendQueue {
	private static volatile LinkedList<byte[]> queue = new LinkedList<byte[]>();
	
	/**
	 * Adds a message to the queue.
	 * @param messageIncludingMeta the message to be added (including meta information!)
	 */
	public static void add(byte[] messageIncludingMeta) {
		if(messageIncludingMeta == null) return;
		Logger.debug(SendQueue.class.getSimpleName(), "Added message to queue");
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
	
	public static ArrayList<byte[]> getMessages(String receivingIp){
		ArrayList<byte[]> messages = new ArrayList<byte[]>();
		for(int i = 0; i<queue.size(); i++) {
			String receivingIpOfMessage = Utils.Meta.getReceiving(queue.get(i));
			if(receivingIpOfMessage.equals(receivingIp)) messages.add(queue.get(i));
		}
		return messages;
	}
	
	public static void main(String[] args) {
		System.out.println(SendQueue.isEmpty());
		SendQueue.add(new byte[]{3, 2, 4});
		System.out.println(SendQueue.isEmpty());
	}
}
