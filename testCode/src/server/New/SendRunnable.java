package server.New;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.MetaUtils;
import com.stamacoding.rsaApp.server.Server;


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
	
	
	/**
	 * Instantiates an object of the {@link SendRunnable} class.
	 * @param port the used port to send messages
	 */
	public SendRunnable(int port) {
		setPort(port);
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
				byte[] messageIncludingMeta = SendQueue.poll();
				
				// Extract the receiver's ip from the message
				setIp(MetaUtils.getReceiving(messageIncludingMeta));
				
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
}
