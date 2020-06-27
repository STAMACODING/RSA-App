package server.New;

import java.io.PrintWriter;
import java.net.Socket;

public class Server {
	
	Socket sock;
	PrintWriter writer;
	public final static String SERVER_IP = "127.0.0.1";
	public final static int SERVER_PORT = 455;

	public static void run() {
		
		Runnable receive = new server.New.ReceiveRunnable(SERVER_IP, SERVER_PORT);
		Runnable runNetwork = new server.New.SendRunnable(SERVER_IP, SERVER_PORT);
		
		Thread alphaThread = new Thread(receive);
		
		
		alphaThread.start();
			
	}
}
