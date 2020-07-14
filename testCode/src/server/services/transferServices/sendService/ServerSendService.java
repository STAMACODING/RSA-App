package server.services.transferServices.sendService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.NetworkUtils;
import server.config.NetworkConfig;
import server.config.NetworkConfig.Server;
import server.message.Message;
import server.message.MessageManager;
import server.services.ServerService;

/**
 * {@link ServerService} sending messages to requesting clients using a {@link ServerSocket}.
 */
public class ServerSendService extends ServerService {
	
	/** The only instance of this class */
	private static volatile ServerSendService singleton = new ServerSendService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ServerSendService#getInstance()} to get the
	 *  only instance of this class.
	 *  The server's port is set to {@link Server#SEND_PORT}.
	 */
	private ServerSendService() {
		super(NetworkConfig.Server.SEND_PORT);
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ServerSendService getInstance() {
		return singleton;
	}
	

	/**
	 * <ol>
	 * 	<li>If a client connects to the sever using a {@link Socket} and queries his messages, he tells the server his unique id.</li>
	 * 	<li>After that using this id the server searches for messages using the {@link MessageManager} that concern the client.</li>
	 * 	<li>If there are any messages the server sends them (fully encrypted) to the client.</li>
	 * </ol>
	 */
	@Override
	public void onRepeat() {
		try {
			Socket connectionFromClient = getServerSocket().accept();
			connectionFromClient.setSoTimeout(5000);
			Logger.debug(this.getClass().getSimpleName(), "Received new client request");

			if(MessageManager.getAllMessages().size() != 0) {
				DataInputStream inputStream = new DataInputStream(connectionFromClient.getInputStream());

				byte clientId = inputStream.readByte();
				Logger.debug(this.getClass().getSimpleName(), "Client logged in as (" + clientId + ")");

				Logger.debug(this.getClass().getSimpleName(), "Searching for messages that belong to (" + clientId + ")");
				ArrayList<Message> messagesToSendAsList = MessageManager.Server.poll(clientId);
				
				int messageCount = messagesToSendAsList.size();
				byte[] messagesToSend = NetworkUtils.serialize(messagesToSendAsList);
				DataOutputStream outputStream = new DataOutputStream(connectionFromClient.getOutputStream());
				
				if(messageCount > 0) {
					Logger.debug(this.getClass().getSimpleName(), "Found " + messageCount + " messages belonging to (" + clientId + ")");
					
					outputStream.writeInt(messagesToSend.length);
					outputStream.write(messagesToSend);
					
					Logger.debug(this.getClass().getSimpleName(), "Successfully sent " + messageCount + " message(s) to a client");
				}else{
					Logger.debug(this.getClass().getSimpleName(), "Found no messages belongig to (" + clientId + ")");
				}

			}else {
				Logger.debug(this.getClass().getSimpleName(), "No messages available to send");
			}
			connectionFromClient.close();
			Logger.debug(this.getClass().getSimpleName(), "Closed connection to client");
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error(this.getClass().getSimpleName(), "Failed to send message to a client");
		}
	}

}
