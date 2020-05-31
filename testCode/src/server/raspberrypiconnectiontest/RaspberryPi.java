package server.raspberrypiconnectiontest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RaspberryPi {
	public static void main(String[] args) {
		System.out.println("Starting server application");
		try {
			ServerSocket serverSock = new ServerSocket(10000);
			System.out.println("Server application running");
			while (true) {
				Socket sock = serverSock.accept();
				System.out.println("Accepted client");
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				String raspberryMessage = "Hello, I'm raspberry pi 4b!";
				System.out.println("Sending message to client");
				writer.println(raspberryMessage);
				writer.close();
				System.out.println("Sent message to client");
			}

		} catch (IOException ex) {
			System.err.println("Server crashed");
			ex.printStackTrace();
		}
	}
}
