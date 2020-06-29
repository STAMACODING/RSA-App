package server.New;

import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	Socket sock;
	PrintWriter writer;
	
	
	public static String CLIENT_IP;
	public static int CLIENT_PORT;
	
	// the server ip does not change
	public final static String SERVER_IP = "127.0.0.1";
	public final static int SERVER_PORT = 455;
	
	public Client(String ip, int port) {
		this.CLIENT_IP = ip;
		this.CLIENT_PORT = port;
	}

	public static void run() {
		
		Runnable receive = new server.New.ReceiveRunnable(SERVER_PORT, false);
		Runnable send = new server.New.SendRunnable(CLIENT_PORT);
		
		Thread receiveThread = new Thread(receive);
		Thread sendThread = new Thread(send);
		
		receiveThread.start();
		receiveThread.setName("receiveThread");
		
		sendThread.start();	
		sendThread.setName("sendThread");
	}
}





