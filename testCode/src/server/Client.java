package server;

import com.stamacoding.rsaApp.log.logger.Logger;

public class Client {
	public static String clientName = "client";
	public static int RECEIVE_PORT = -2;

	public static void run() {
		Runnable receive = new server.ReceiveRunnable(RECEIVE_PORT, false);
		Logger.debug(Server.class.getSimpleName(), "ReceiveRunnable of Client:(" + clientName + ") initiated as: receive");
		Runnable send = new server.SendRunnable(Server.RECEIVE_PORT, false);
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





