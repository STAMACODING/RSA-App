package server.New;

import com.stamacoding.rsaApp.log.logger.Logger;

public class Server {
	
	public static String serverName = "raspberrypi";
	public static String SERVER_IP = null;
	public static int RECEIVE_PORT = -1;

	public static void run() {
		Runnable receive = new server.New.ReceiveRunnable(RECEIVE_PORT, true);
		Logger.debug(Server.class.getSimpleName(), "ReceiveRunnable of Server:(" + serverName + ") initiated as: receive");
		Runnable send = new server.New.SendRunnable(Client.RECEIVE_PORT, true);
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
