package server.services.transferServices.receiveService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.sun.security.ntlm.Server;

import server.config.NetworkConfig;
import server.config.Type;
import server.services.Service;
import server.services.databaseServices.DatabaseMessage;
import server.services.databaseServices.storeService.StoreQueue;
import server.services.transferServices.TransferMessage;
import server.services.transferServices.sendService.SendQueue;

public class ReceiveService extends Service{
	private static volatile ReceiveService singleton = new ReceiveService();

	private ReceiveService() {
		super("receive-service");
	}
	
	public static ReceiveService getInstance() {
		return singleton;
	}
	
	@Override
	public void run() {
		super.run();
		if(NetworkConfig.TYPE == Type.CLIENT) {
			runClient();
		}else if(NetworkConfig.TYPE == Type.SERVER) {
			runServer();
		}
	}

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
						    Logger.debug(Server.class.getSimpleName(), "Successfully received " + messages.size() + " new message(s) from the send server");
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
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Logger.error(this.getClass().getSimpleName(), "Failed to receive message from the send server");
		}
	}
}
