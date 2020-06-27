package server.New;

import java.io.PrintWriter;
import java.net.Socket;

public class Server {
	
	Socket sock;
	PrintWriter writer;
	static String SERVER_IP = "127.0.0.1";
	static int SERVER_PORT = 455;

	public static void main(String [] args) {
		
		Runnable startServer = new server.New.ServerRunnable(SERVER_IP, SERVER_PORT);
		Runnable runNetwork = new server.New.NetworkingRunnable(SERVER_IP, SERVER_PORT);
		
		Thread alphaThread = new Thread(startServer);
		
		alphaThread.start();
		
		
	}
}
