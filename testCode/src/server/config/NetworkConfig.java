package server.config;

public class NetworkConfig {
	public static Type TYPE = null;
	
	public static class Client{
		public static byte ID = -1;
	}
	
	public static class Server{
		public static String IP = null;
		public static int SEND_PORT = -1;
		public static int RECEIVE_PORT = -1;
	}
	
	public static void setup(Type type, byte clientID, String serverIP, int serverSendPort, int serverReceivePort) {
		TYPE = type;
		Client.ID = clientID;
		Server.IP = serverIP;
		Server.SEND_PORT = serverSendPort;
		Server.RECEIVE_PORT = serverReceivePort;
	}

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
