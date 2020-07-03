package server.services.transferServices.receiveService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.config.NetworkConfig;
import server.config.NetworkConfig.Client;
import server.config.NetworkConfig.Server;
import server.config.Type;
import server.services.Service;
import server.services.databaseServices.DatabaseMessage;
import server.services.databaseServices.storeService.StoreQueue;
import server.services.transferServices.TransferMessage;
import server.services.transferServices.sendService.SendQueue;
import server.services.transferServices.sendService.SendService;

/**
 * {@link Service} receiving messages from the server or the clients.
 */
public class ReceiveService extends Service{
	/**
	 * the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 */
	private static volatile ReceiveService singleton = new ReceiveService();

	/**
	 * the object's private constructor (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>)
	 */
	private ReceiveService() {
		super("receive");
	}
	
	/**
	 * Gets the object's only instance (<b>see</b><a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton"> singleton pattern</a>).
	 * @return the object's only instance
	 */
	public static ReceiveService getInstance() {
		return singleton;
	}
	
	/**
	 * Runs the {@link ReceiveService}.
	 * @see #runServer()
	 * @see #runClient()
	 */
	@Override
	public void run() {
		super.run();
		if(NetworkConfig.TYPE == Type.CLIENT) {
			runClient();
		}else if(NetworkConfig.TYPE == Type.SERVER) {
			runServer();
		}
	}

	/**
	 * Creates a {@link ServerSocket} to receive messages from clients.
	 * <ol>
	 * 	<li>If a clients wants to connect to the server using a {@link Socket}, the server will accept the connection.</li>
	 * 	<li>After that the server reads the message from the socket's {@link DataInputStream}.</li>
	 * 	<li>Then the message gets forwarded using the {@link SendService} and the {@link SendQueue}.</li>
	 * </ol>
	 */
	private void runServer() {
		// Creates server socket listening on specified port (client should send message to this port)
		ServerSocket receiveServer = null;
		try {
			receiveServer = new ServerSocket(NetworkConfig.Server.RECEIVE_PORT);
			Logger.debug(this.getClass().getSimpleName(), "Successfully started the receive server");
			
			while(!requestedShutDown()) {
				// Accept message from client and if this is a server forward it to another client
				try {
					Socket connectionFromClient = receiveServer.accept();
					Logger.debug(this.getClass().getSimpleName(), "Receiving new message from a client");
					DataInputStream inputStream = new DataInputStream(connectionFromClient.getInputStream());
					
					// Check if message is not empty
					int length = inputStream.readInt();
					
				    // Read message from client
					byte[] messageAsByteArray = null;
					if(length>0) {
						messageAsByteArray = new byte[length];
						
					    inputStream.readFully(messageAsByteArray, 0, messageAsByteArray.length);
					    Logger.debug(this.getClass().getSimpleName(), "Successfully received new message from a client");
					}
						
					// Forward message
					Logger.debug(this.getClass().getSimpleName(), "Forwarding message");
					SendQueue.add(TransferMessage.byteArrayToMessage(messageAsByteArray));
				} catch (IOException e) {
					e.printStackTrace();
					Logger.error(this.getClass().getSimpleName(), "Failed to receive message from a client");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error(this.getClass().getSimpleName(), "Failed to start receive server");
		}
	}

	/**
	 * Creates a {@link Socket} connection to query new messages from the server in an interval of {@link Client#QUERY_MESSAGES_INTERVAL} milliseconds.
	 * <ol>
	 * 	<li>Connects to the service using a {@link Socket} at port {@link Server#SEND_PORT}.</li>
	 * 	<li>Queries new messages by sending its {@link Client#ID}.</li>
	 * 	<li>Receives new messages if there are any.</li>
	 * 	<li>Waits {@link Client#QUERY_MESSAGES_INTERVAL} milliseconds and goes back to step 1.</li>
	 * </ol>
	 */
	private void runClient() {
		try {
			while(!requestedShutDown()) {
				try {
					Socket connectionToServer = new Socket(NetworkConfig.Server.IP, NetworkConfig.Server.SEND_PORT);
					Logger.debug(this.getClass().getSimpleName(), "Successfully connected to the send server");
					
					Logger.debug(this.getClass().getSimpleName(), "Querying messages from the send server");
					// Read message from server
					DataOutputStream outputStream = new DataOutputStream(connectionToServer.getOutputStream());
					outputStream.writeByte(NetworkConfig.Client.ID);
					
					DataInputStream inputStream = new DataInputStream(connectionToServer.getInputStream());
					byte[] messagesIncludingMeta = null;
					try {
						int length = inputStream.readInt();
						if(length!=0) {
							messagesIncludingMeta = new byte[length];
						    inputStream.readFully(messagesIncludingMeta, 0, messagesIncludingMeta.length);
						    
						    ArrayList<TransferMessage> messages = TransferMessage.byteArrayToMessageList(messagesIncludingMeta);
						    Logger.debug(this.getClass().getSimpleName(), "Successfully received " + messages.size() + " new message(s) from the send server");
							for(TransferMessage m : messages) {
						    	StoreQueue.add(new DatabaseMessage(m));
						    }
						}
					}catch(Exception e) {
						Logger.debug(this.getClass().getSimpleName(), "No new messages available");
					}
					connectionToServer.close();
				} catch (IOException e) {
					e.printStackTrace();
					Logger.error(this.getClass().getSimpleName(), "Failed to connect to the send server");
				}
				Thread.sleep(NetworkConfig.Client.QUERY_MESSAGES_INTERVAL);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Logger.error(this.getClass().getSimpleName(), "Failed to receive message from the send server");
		}
	}
}
