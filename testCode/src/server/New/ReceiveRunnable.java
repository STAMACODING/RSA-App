package server.New;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.Server;

/**
 * {@link Runnable} to receive new messages from other clients. Works using a socket server that listens on the specified {@link #port}.
 *
 */
public class ReceiveRunnable implements Runnable{
	/** The used server socket to listen to new messages from other clients. **/
	private ServerSocket serverSocket;
	/** The used port to listen to new messages from other clients. **/
	private int port;
	/** Describes if the used device acts as server or as client. If this attribute is set to <i>true</i> incoming messages get forwarded. **/
	private final boolean server;
	
	/**
	 * Instantiates an object of the {@link ReceiveRunnable} class.
	 * @param port the used port to listen to new messages from other clients
	 * @param server describes if the used device acts as server or as client. If this attribute is set to <i>true</i> incoming messages get forwarded
	 */
	public ReceiveRunnable(int port, boolean server) {
		setPort(port);
		this.server = server;
	}
	
	/**
	 * Runs the {@link ReceiveRunnable}.
	 * @see ReceiveRunnable
	 */
	public void run() {
		Logger.debug(ReceiveRunnable.class.getSimpleName(), "ReceiveThread is running");
		// Creates server socket listening on specified port (client should send message to this port)
		try {
			setServerSocket(new ServerSocket(getPort()));
			Logger.debug(ReceiveRunnable.class.getSimpleName(), "Receiving server socket started successfully");
		} catch (IOException e) {
			Logger.error(ReceiveRunnable.class.getSimpleName(), "Receiving server socket failed to start");
		}
		while(true) {
			// Accept message from client and if this is a server forward it to another client
			try {
				Socket receiver = getServerSocket().accept();
				Logger.debug(Server.class.getSimpleName(), "Receiving new message from a client");
				DataInputStream inputStream = new DataInputStream(receiver.getInputStream());
				
				// Check if message is not empty
				int length = inputStream.readInt();
				
				if(length>0) {
					byte[] messageIncludingMeta = new byte[length];
					
				    // Write message into byte[] messageIncludingMeta
				    inputStream.readFully(messageIncludingMeta, 0, messageIncludingMeta.length);
				    Logger.debug(Server.class.getSimpleName(), "Received new message from a client");
					
					if(isServer()) {
						// Forward message
						Logger.debug(Server.class.getSimpleName(), "Forwarding message");
						SendQueue.add(messageIncludingMeta);
					}
				}
			} catch (IOException e) {
				Logger.error(ReceiveRunnable.class.getSimpleName(), "Failed to receive message from a client");
			}
		}
		// server.close();
	}

	/**
	 * Gets the used server socket.
	 * @return the used server socket
	 * @see #serverSocket
	 */
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	/**
	 * Sets the used server socket
	 * @param serverSocket the used server socket
	 * @see #serverSocket
	 */
	private void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * Gets the used port to listen to new messages from other clients.
	 * @return the used port to listen to new messages from other clients.
	 * @see #port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the used port to listen to new messages from other clients.
	 * @param port the used port to listen to new messages from other clients
	 * @see #port
	 */
	private void setPort(int port) {
		this.port = port;
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
