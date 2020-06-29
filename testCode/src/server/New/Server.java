package server.New;

import java.io.PrintWriter;

import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;

public class Server {
	
	Socket sock;
	PrintWriter writer;
	
	public static String serverName;
	public final static String SERVER_IP = "127.0.0.1";
	public final static int SERVER_PORT = 455;
	
	public Server(String name) {
		this.serverName = name;
	}

	public static void run() {
		
		Runnable receive = new server.New.ReceiveRunnable(SERVER_PORT, true);
		Logger.debug(Server.class.getSimpleName(), "ReceiveRunnable of Server:(" + serverName + ") initiated as: receive");
		Runnable send = new server.New.SendRunnable(SERVER_PORT);
		Logger.debug(Server.class.getSimpleName(), "SendRunnable of Server:(" + serverName + ") initiated as: send");
		
		Thread receiveThread = new Thread(receive);
		Thread sendThread = new Thread(send);
		
		receiveThread.start();
		receiveThread.setName("receiveThread");
		Logger.debug(Server.class.getSimpleName(), "receiveThrad of Server:(" + serverName + ") started") ;
		receiveThread.setName("receiveThread");
		
		sendThread.start();	
		sendThread.setName("sendThread");
		Logger.debug(Server.class.getSimpleName(), "sendThrad of Server:(" + serverName + ") started");
		sendThread.setName("sendThread");
	}
	
}

//this is just a test about the annotations
