package server.New;

import java.io.PrintWriter;
import java.net.Socket;

public class ReceiveRunnable implements Runnable{

	Socket sock;
	PrintWriter writer;
	
	String SERVER_IP;
	int SERVER_PORT;
	
	public ReceiveRunnable(String ip, int port) {
		this.SERVER_IP = ip;
		this.SERVER_PORT = port;
	}
	
	public void run() {
		
	}
}
