package server.services.transferServices.sendService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.config.NetworkConfig;
import server.message.Message;
import server.message.MessageManager;
import server.message.MessageManager.Client;
import server.message.data.ProtectedData;
import server.message.data.SendState;
import server.message.data.ServerData;
import server.services.Service;


/**
 *  {@link Service} sending messages from client to server using the {@link MessageManager}.
 */
public class ClientSendService extends Service {
	
	/** The only instance of this class */
	private static volatile ClientSendService singleton = new ClientSendService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientSendService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private ClientSendService() {}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static ClientSendService getInstance() {
		return singleton;
	}

	/**
	 * Sends a message from a client to the server if there is any message to send.
	 *  <ol>
	 * 	<li>If {@link Client#getMessageToSend()} does not return null, there is a message to send.</li>
	 * 	<li>After that the client connect to the server using a {@link Socket}.</li>
	 * 	<li>Then the message gets encoded and sent.</li>
	 * 	<li>If the server successfully receives the message, the message's {@link SendState} will get updated to {@link SendState#SENT}.</li>
	 * </ol>
	 * @see Service#onRepeat()
	 */
	@Override
	public void onRepeat() {
		// If there is a message to be sent
		Message messageToSend = MessageManager.Client.getMessageToSend();
		if(messageToSend != null) {
			Logger.debug(this.getClass().getSimpleName(), "Got new message to send from MessageManager");
			Logger.debug(this.getClass().getSimpleName(), "Message to send: " + messageToSend.toString());
			
			// Encode message before sending to server
			byte[] messageMeta = ServerData.encrypt(messageToSend.getServerData());
			byte[] messageData = ProtectedData.encrypt(messageToSend.getProtectedData());
			
			Logger.debug(this.getClass().getSimpleName(), "Encoded message");
				
			Socket connectionToServer = null;
			try {
				connectionToServer = new Socket(NetworkConfig.Server.IP, NetworkConfig.Server.RECEIVE_PORT);
				connectionToServer.setSoTimeout(5000);
				Logger.debug(this.getClass().getSimpleName(), "Successfully connected to the receive server");
				try {
					DataOutputStream outputStream = new DataOutputStream(connectionToServer.getOutputStream());
					
					// Send message meta
					outputStream.writeInt(messageMeta.length);
					outputStream.write(messageMeta);
					
					// Send message data
					outputStream.writeInt(messageData.length);
					outputStream.write(messageData);
					
					outputStream.flush();
						
					Logger.debug(this.getClass().getSimpleName(), "Successfully sent message to the receive server");
					Logger.debug(this.getClass().getSimpleName(), "Updating message status");
					
					messageToSend.getLocalData().setSendState(SendState.SENT);
					messageToSend.getLocalData().setUpdateRequested(true);
					
					// Close connection to the receiver
					connectionToServer.close();
					Logger.debug(this.getClass().getSimpleName(), "Closed connection to the receive server");
				} catch (IOException e) {
					e.printStackTrace();
					Logger.error(this.getClass().getSimpleName(), "Failed to send message to the receive server");
				}
			} catch (IOException e) {
				e.printStackTrace();
				Logger.error(this.getClass().getSimpleName(), "Failed to connect to receive server");
			}
			
		}
	}

}
