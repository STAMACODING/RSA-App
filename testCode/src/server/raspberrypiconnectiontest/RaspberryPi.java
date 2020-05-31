package server.raspberrypiconnectiontest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;

public class RaspberryPi {
	public static void main(String[] args) {
		Logger.debug(RaspberryPi.class.getSimpleName(), "Starting server application");
		try {
			ServerSocket serverSock = new ServerSocket(1001);
			Logger.debug(RaspberryPi.class.getSimpleName(), "Server application running");
			while (true) {
				Socket sock = serverSock.accept();
				Logger.debug(RaspberryPi.class.getSimpleName(), "Accepted client");
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				String raspberryMessage = "Hello, I'm raspberry pi 4b!";
				System.out.println("Sending message to client");
				Logger.debug(RaspberryPi.class.getSimpleName(), raspberryMessage);
				writer.close();
				Logger.debug(RaspberryPi.class.getSimpleName(), "Sent message to client");
			}

		} catch (IOException ex) {
			Logger.error(RaspberryPi.class.getSimpleName(), "Server crashed \n" + ex.getMessage());
		}
	}
}
