package server.New;

import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	Socket sock;
	PrintWriter writer;
	
	
	public static String CLIENT_IP;
	public static int CLIENT_PORT;
	
	public Client(String ip, int port) {
		this.CLIENT_IP = ip;
		this.CLIENT_PORT = port;
	}

	public static void run() {
		
		Runnable receive = new server.New.ReceiveRunnable(CLIENT_IP, CLIENT_PORT);
		Runnable runNetwork = new server.New.SendRunnable(CLIENT_IP, CLIENT_PORT);
		
		Thread alphaThread = new Thread(receive);
		
		
		alphaThread.start();
		
	}
}

//this is just a test about the annotations
