package server.config;

import server.services.mainService.MessageService;

/**
 * Class containing all configurations needed to start
 * the {@link MessageService}. By changing the static variables you change the configurations.
 * It's not recommended do this while the {@link MessageService} is already running because
 * this could lead to unexpected behavior.
 */
public class NetworkConfig {
	
	/** The type of the device the {@link MessageService} is running on */
	public static NetworkType TYPE = null;
	
	/**
	 * Configurations concerning the client
	 */
	public static class Client{
		
		/** The client's unique id */
		public static byte ID = -1;
		
		/**
		 * The time the {@link ReceiveService} waits before querying new messages from
		 * the server
		 */
		public static long QUERY_MESSAGES_INTERVAL = 10000;
	}
	
	/**
	 * Configurations concerning the server
	 */
	public static class Server{
		
		/** The server's unique public ip-address */
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
	 * @param type the type of the device the {@link MessageService} is running on
	 * @param clientID the client's unique id
	 * @param serverIP the server's unique public ip-address
	 * @param serverSendPort On this port the server sends messages. The clients receive messages using this port.
	 * @param serverReceivePort On this port the server listens to receive messages from clients. The clients send messages using this port.
	 */
	public static void setup(NetworkType type, byte clientID, String serverIP, int serverSendPort, int serverReceivePort) {
		TYPE = type;
		Client.ID = clientID;
		Server.IP = serverIP;
		Server.SEND_PORT = serverSendPort;
		Server.RECEIVE_PORT = serverReceivePort;
	}

	/**
	 * Checks if the configurations are valid. Attention! This function cannot verify
	 * if the configurations will lead to a correctly working {@link MessageService}.
	 * @return if the configurations are valid
	 */
	public static boolean isValid() {
		if(TYPE == null) return false;
		
		if(Client.ID < 0 && TYPE == NetworkType.CLIENT) return false;
		if(Client.QUERY_MESSAGES_INTERVAL < 0 && TYPE == NetworkType.CLIENT) return false;
		
		if(Server.SEND_PORT < 0) return false;
		if(Server.RECEIVE_PORT < 0) return false;
		if(Server.RECEIVE_PORT == Server.SEND_PORT) return false;
		if(Server.IP == null) return false;
		
		return true;
	}
}
