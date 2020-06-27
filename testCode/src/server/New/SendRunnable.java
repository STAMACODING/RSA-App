package server.New;

import java.io.PrintWriter;
import java.net.Socket;

public class SendRunnable implements Runnable{

	Socket sock;
	PrintWriter writer;
	
	String SERVER_IP;
	int SERVER_PORT;
	
	public SendRunnable(String ip, int port) {
		this.SERVER_IP = ip;
		this.SERVER_PORT = port;
	}
	
	public void run() {
		
	}
}
