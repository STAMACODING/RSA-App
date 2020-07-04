package server.config;

import server.NetworkService;
import server.services.databaseServices.chatHistoryService.ChatHistoryService;
import server.services.transferServices.receiveService.ReceiveService;

/**
 * Class containing all configurations needed to start
 * the {@link NetworkService}. By changing the static variables you change the configurations.
 * It's not recommended do this while the {@link NetworkService} is already running because
 * this could lead to unexpected behavior.
 */
public class NetworkConfig {
	/**
	 * the type of the device the {@link NetworkService} is running on
	 */
	public static Type TYPE = null;
	
	/**
	 * configurations concerning the client
	 */
	public static class Client{
		/**
		 * the client's unique id
		 */
		public static byte ID = -1;
		
		/**
		 * the time the {@link ReceiveService} waits before querying new messages from the server
		 */
		public static long QUERY_MESSAGES_INTERVAL = 10000;
		
		/**
		 * the time the {@link ChatHistoryService} waits before updating the messages
		 */
		public static long UPDATE_CHAT_HISTORY_INTERVAL = 2000;
	}
	
	/**
	 * configurations concerning the server
	 */
	public static class Server{
		/**
		 * the server's unique public ip
		 */
		public static String IP = null;
		/**
		 * On this port the server sends messages. The clients receive messages using this port.
		 */
		public static int SEND_PORT = -1;
		/**
		 * On this port the server listens to receive messages from clients. The clients send messages using this port.
		 */
		public static int RECEIVE_PORT = -1;
	}
	
	/**
	 * You can use this method to change all network configurations at once.
	 * @param type the type of the device the {@link NetworkService} is running on
	 * @param clientID the client's unique id
	 * @param serverIP the server's unique public ip
	 * @param serverSendPort On this port the server sends messages. The clients receive messages using this port.
	 * @param serverReceivePort On this port the server listens to receive messages from clients. The clients send messages using this port.
	 */
	public static void setup(Type type, byte clientID, String serverIP, int serverSendPort, int serverReceivePort) {
		TYPE = type;
		Client.ID = clientID;
		Server.IP = serverIP;
		Server.SEND_PORT = serverSendPort;
		Server.RECEIVE_PORT = serverReceivePort;
	}

	/**
	 * Checks if the configurations are valid. Attention! This function cannot verify
	 * if the configurations will lead to a correctly working {@link NetworkService}.
	 * @return if the configurations are valid
	 */
	public static boolean isValid() {
		if(TYPE == null) return false;
		if(Client.ID < 0 && TYPE == Type.CLIENT) return false;
		if(Server.SEND_PORT < 0) return false;
		if(Server.RECEIVE_PORT < 0) return false;
		if(Server.RECEIVE_PORT == Server.SEND_PORT) return false;
		if(Server.IP == null) return false;
		
		return true;
	}
}
