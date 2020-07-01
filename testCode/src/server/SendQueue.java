package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.Utils.Serialization;

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
	
	public static ArrayList<byte[]> pollMessages(byte clientId){
		ArrayList<byte[]> messages = new ArrayList<byte[]>();
		for(int i = 0; i<queue.size(); i++) {
			byte receivingIdOfMessage = Utils.Meta.getReceiving(queue.get(i));
			if(receivingIdOfMessage == clientId) {
				messages.add(queue.get(i));
			}
		}
		queue.removeAll(messages);
		return messages;
	}
	
	public static ArrayList<byte[]> byteArrayToMessageList(byte[] messages){
		return (ArrayList<byte[]>) Serialization.deserialize(messages);
	}
	
	public static byte[] messageListToByteArray(ArrayList<byte[]> messages){
		return Serialization.serialize(messages);
	}
	
	public static void main(String[] args) {
		System.out.println(SendQueue.isEmpty());
		SendQueue.add(new byte[]{3, 2, 4});
		System.out.println(SendQueue.isEmpty());
	}
}
