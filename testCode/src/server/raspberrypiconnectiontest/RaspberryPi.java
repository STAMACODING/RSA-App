package server.raspberrypiconnectiontest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;

public class RaspberryPi {
	public static void main(String[] args) {
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		System.out.println("Raspberry Pi Connection Test (Server Application)");
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		
		Logger.debug(RaspberryPi.class.getSimpleName(), "Starting server application");
		try {
			ServerSocket serverSock = new ServerSocket(1001);
			Logger.debug(RaspberryPi.class.getSimpleName(), "Server application running");
			System.out.println("--------------------------------------------------------------------------------------------------------------------");
			while (true) {
				Socket sock = serverSock.accept();
				Logger.debug(RaspberryPi.class.getSimpleName(), "Accepted client");
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				String raspberryMessage = "Hello, I'm raspberry pi 4b!";
				System.out.println("Sending signal to client");
				Logger.debug(RaspberryPi.class.getSimpleName(), raspberryMessage);
				writer.close();
				Logger.debug(RaspberryPi.class.getSimpleName(), "Sent message to client");
				System.out.println("--------------------------------------------------------------------------------------------------------------------");
			}

		} catch (IOException ex) {
			Logger.error(RaspberryPi.class.getSimpleName(), "Server crashed \n" + ex.getMessage());
			System.out.println("--------------------------------------------------------------------------------------------------------------------");
		}
	}
}
