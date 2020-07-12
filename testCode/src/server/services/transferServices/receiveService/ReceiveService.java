package server.services.transferServices.receiveService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.Utils;
import server.config.NetworkConfig;
import server.config.NetworkConfig.Client;
import server.config.NetworkConfig.Server;
import server.config.NetworkType;
import server.message.Message;
import server.message.data.LocalData;
import server.message.data.SendState;
import server.services.Service;
import server.services.databaseService.MessageManager;
import server.services.transferServices.sendService.SendService;

/**
 * {@link Service} receiving messages.
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
	 * Restarts the {@link ReceiveService} safely.
	 */
	public static void restart() {
		Logger.debug(ReceiveService.class.getSimpleName(), "Restarting " + singleton.getName());
		singleton.requestShutdown();
		while(singleton.isRunning()) {}
		singleton = new ReceiveService();
		Logger.debug(ReceiveService.class.getSimpleName(), "Restarted " + singleton.getName());
		singleton.start();
	}
	
	/**
	 * Runs the {@link ReceiveService}.
	 * @see #runServer()
	 * @see #runClient()
	 */
	@Override
	public void run() {
		super.run();
		if(NetworkConfig.TYPE == NetworkType.CLIENT) {
			Logger.debug(this.getClass().getSimpleName(), "Running on client");
			runClient();
		}else if(NetworkConfig.TYPE == NetworkType.SERVER) {
			Logger.debug(this.getClass().getSimpleName(), "Running on server");
			runServer();
		}
		Logger.debug(this.getClass().getSimpleName(), "Shut down " + getName());
	}

	/**
	 * Creates a {@link ServerSocket} to receive messages from clients.
	 * <ol>
	 * 	<li>If a clients wants to connect to the server using a {@link Socket}, the server will accept the connection.</li>
	 * 	<li>After that the server reads the message from the socket's {@link DataInputStream}.</li>
	 * 	<li>Then the message gets forwarded using the {@link SendService} and the {@link MessageManager}.</li>
	 * </ol>
	 */
	private void runServer() {
		// Creates server socket listening on specified port (client should send message to this port)
		ServerSocket receiveServer = null;
		try {
			receiveServer = new ServerSocket(NetworkConfig.Server.RECEIVE_PORT);
			Logger.debug(this.getClass().getSimpleName(), "Successfully started the receive server");
			
			while(!isShutDownRequested()) {
				// Accept message from client and if this is a server forward it to another client
				try {
					Socket connectionFromClient = receiveServer.accept();
					connectionFromClient.setSoTimeout(5000);
					Logger.debug(this.getClass().getSimpleName(), "Receiving a new message from a client");
					DataInputStream inputStream = new DataInputStream(connectionFromClient.getInputStream());
					

					try {
					    // Read message from client
						int messageMetaLength = inputStream.readInt();
						byte[] messageMeta = null, messageData = null;
						if(messageMetaLength>0) {
							// Read message meta
							messageMeta = new byte[messageMetaLength];
						    inputStream.readFully(messageMeta, 0, messageMetaLength);
						    
						    
						    
						    // Read message data
						    int messageDataLength = inputStream.readInt();
						    
						    if(messageDataLength > 0) {
						    	messageData = new byte[messageDataLength];
						    	inputStream.readFully(messageData, 0, messageDataLength);
						    }else {
						    	throw new Exception("Received invalid data");
						    }
						    
						    Logger.debug(this.getClass().getSimpleName(), "Successfully received message's meta and data");
							
							Message receivedMessage = new Message(new LocalData(-1, SendState.PENDING), messageData, messageMeta);
							receivedMessage.decodeMessageMeta();
							
							Logger.debug("MessageManager." + Server.class.getSimpleName(), "Received message: " + receivedMessage.toString());
							MessageManager.manage(receivedMessage);
						}else {
							throw new Exception("Received invalid data");
						}
					}catch(Exception e) {
						Logger.error(this.getClass().getSimpleName(), "Received invalid data");
					}

					connectionFromClient.close();
					Logger.debug(this.getClass().getSimpleName(), "Closed connection to client");
				} catch (IOException e) {
					e.printStackTrace();
					Logger.error(this.getClass().getSimpleName(), "Failed to receive message from a client");
				}
			}
			receiveServer.close();
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
			while(!isShutDownRequested()) {
				try {
					Socket connectionToServer = new Socket(NetworkConfig.Server.IP, NetworkConfig.Server.SEND_PORT);
					connectionToServer.setSoTimeout(5000);
					Logger.debug(this.getClass().getSimpleName(), "Successfully connected to the send server");
					
					Logger.debug(this.getClass().getSimpleName(), "Querying messages from the send server using the client id (" + NetworkConfig.Client.ID + ")");
					// Read message from server
					DataOutputStream outputStream = new DataOutputStream(connectionToServer.getOutputStream());
					outputStream.writeByte(NetworkConfig.Client.ID);
					
					DataInputStream inputStream = new DataInputStream(connectionToServer.getInputStream());
					byte[] messages = null;
					try {
						int length = inputStream.readInt();
						if(length!=0) {
							messages = new byte[length];
						    inputStream.readFully(messages, 0, length);
						    
						    ArrayList<Message> messagesAsList = (ArrayList<Message>) Utils.Serialization.deserialize(messages);
						    Logger.debug(this.getClass().getSimpleName(), "Successfully received " + messagesAsList.size() + " new message(s) from the send server");
							for(Message m : messagesAsList) {
								m.getLocalData().setSendState(SendState.SENT);
						    	m.decodeMessageMeta();
						    	m.decodeMessageData();
						    	Logger.debug(this.getClass().getSimpleName(), "Received message: " + m.toString());
						    	MessageManager.manage(m);
						    }
						}
					}catch(Exception e) {
						Logger.debug(this.getClass().getSimpleName(), "No new messages available");
					}
					connectionToServer.close();
					Logger.debug(this.getClass().getSimpleName(), "Closed connection to the send server");
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
