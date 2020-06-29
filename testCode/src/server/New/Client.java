package server.New;

import java.io.PrintWriter;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;

public class Client {

	Socket sock;
	PrintWriter writer;
	
	public static String clientName;
	public static String CLIENT_IP;
	public static int CLIENT_PORT;
	
	// the server ip does not change
	public final static String SERVER_IP = "127.0.0.1";
	public final static int SERVER_PORT = 455;
	
	public Client(String name, String ip, int port) {
		this.clientName = name;
		this.CLIENT_IP = ip;
		this.CLIENT_PORT = port;
	}

	public static void run() {
		
		Runnable receive = new server.New.ReceiveRunnable(SERVER_PORT, false);
		Logger.debug(Server.class.getSimpleName(), "ReceiveRunnable of Client:(" + clientName + ") initiated as: receive");
		Runnable send = new server.New.SendRunnable(CLIENT_PORT);
		Logger.debug(Server.class.getSimpleName(), "SendRunnable of Client:(" + clientName + ") initiated as: send");
		
		Thread receiveThread = new Thread(receive);
		Thread sendThread = new Thread(send);
		
		receiveThread.start();
		receiveThread.setName("receiveThread");
		Logger.debug(Server.class.getSimpleName(), "receiveThrad of Client:(" + clientName + ") started") ;
		
		sendThread.start();	
		sendThread.setName("sendThread");
		Logger.debug(Server.class.getSimpleName(), "sendThrad of Client:(" + clientName + ") started") ;
	}
}





