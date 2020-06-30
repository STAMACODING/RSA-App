package server.New;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.MetaUtils;


/**
 * {@link Runnable} to send messages to other clients. Works using the {@link SendQueue} and sockets.
 *
 */
public class SendRunnable implements Runnable{
	/** The used port to send messages. **/
	private int port;
	/** The current receiver's ip. The receiver is the one messages are sent to. This variable changes with every new message listed in the {@link SendQueue}.**/
	private String ip;
	/** The current connection to the receiver. Gets recreated after every message.
	 * @see #run()
	 */
	private Socket socket;
	/** Describes if the used device acts as server or as client. If this attribute is set to <i>true</i> incoming messages get forwarded. **/
	private final boolean server;
	
	
	/**
	 * Instantiates an object of the {@link SendRunnable} class.
	 * @param port the used port to send messages
	 */
	public SendRunnable(int port, boolean server) {
		setPort(port);
		this.server = server;
	}

	/**
	 * Runs the {@link SendRunnable}.
	 * @see SendRunnable
	 */
	public void run() {
		Logger.debug(SendRunnable.class.getSimpleName(), "SendThread is running");
		while(true) {
			// If there is a message to be sent
			if(!SendQueue.isEmpty()) {
				Logger.debug(SendRunnable.class.getSimpleName(), "SendQueue is not empty");
				byte[] messageIncludingMeta = SendQueue.poll();
				
				if(isServer()) {
					// Extract the receiver's ip from the message
					setIp(MetaUtils.getReceiving(messageIncludingMeta));
				}else {
					setIp(Server.SERVER_IP);
				}
				
				Logger.debug(SendRunnable.class.getSimpleName(), "Sending message to " + getIp());
					
				try {
					// Create connection to the receiver via socket
					setSocket(new Socket(getIp(), getPort()));
					DataOutputStream outputStream = new DataOutputStream(getSocket().getOutputStream());
					
					// Send message to the receiver
					outputStream.writeInt(messageIncludingMeta.length);
					outputStream.write(messageIncludingMeta);
						
					Logger.debug(SendRunnable.class.getSimpleName(), "Sent message successfully to " + getIp());
					
					// Close connection to the receiver
					getSocket().close();
				} catch (IOException e) {
					e.printStackTrace();
					Logger.error(SendRunnable.class.getSimpleName(), "Failed to send message to " + getIp());
				}

			}
		}
	}
	
	/**
	 * Sets the current receiver's ip. The receiver is the one messages are sent to. This variable changes with every new message listed in the {@link SendQueue}.
	 * @param ip
	 * @see #ip
	 */
	private void setIp(String ip){
		this.ip = ip;
	}
	
	/**
	 * Gets the current receiver's ip. The receiver is the one messages are sent to. This variable changes with every new message listed in the {@link SendQueue}.
	 * @return the current receiver's ip
	 * @see #ip
	 */
	public String getIp() {
		return this.ip;
	}
	
	/**
	 * Sets the used port to send messages.
	 * @param port the used port to send messages
	 * @see #port
	 */
	private void setPort(int port){
		this.port = port;
	}
	
	/**
	 * Gets the used port to send messages.
	 * @return the used port to send messages
	 * @see #port
	 */
	public int getPort() {
		return this.port;
	}
	
	/**
	 * Gets the current connection to the receiver. Gets recreated after every message.
	 * @return the current connection to the receiver
	 * @see #socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Sets the current connection to the receiver. Gets recreated after every message.
	 * @param socket the current connection to the receiver
	 * @see #socket
	 */
	private void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * Describes if the used device acts as server or as client. If this attribute is set to <i>true</i> incoming messages get forwarded.
	 * @return if the used device acts as server or as client
	 * @see #serer
	 */
	public boolean isServer() {
		return server;
	}
}
