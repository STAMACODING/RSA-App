package server.New;

import java.io.PrintWriter;
import java.net.Socket;

public class Server {
	
	Socket sock;
	PrintWriter writer;
	public final static String SERVER_IP = "127.0.0.1";
	public final static int SERVER_PORT = 455;

	public static void run() {
		
		Runnable receive = new server.New.ReceiveRunnable(SERVER_PORT, true);
		Runnable send = new server.New.SendRunnable(SERVER_PORT);
		
		Thread receiveThread = new Thread(receive);
		Thread sendThread = new Thread(send);
		
		receiveThread.start();
		receiveThread.setName("receiveThread");
		
		sendThread.start();	
		sendThread.setName("sendThread");
	}
	
}

//this is just a test about the annotations
