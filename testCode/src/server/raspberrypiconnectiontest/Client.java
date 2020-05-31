package server.raspberrypiconnectiontest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		System.out.println("Enter raspberry's IP: ");
		String raspberryIP = s.nextLine();
		System.out.println("Enter raspberry's port: ");
		int raspberryPort = s.nextInt();
		s.close();
		try {
			System.out.println("Trying to connect to raspberry pi (" + raspberryIP + ":" + raspberryPort + ")");
			Socket socket = new Socket(raspberryIP, raspberryPort);
			
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			
			System.out.println("Connected to raspberry pi (" + raspberryIP + ":" + raspberryPort + ")");
			
			String messageFromRaspberry = reader.readLine();
			System.out.println("Raspberry said: " + messageFromRaspberry);
			reader.close();
			socket.close();
			System.out.println("Test was successfull!");
		} catch (IOException e) {
			System.err.println("Connection failed :(");
			e.printStackTrace();
		}
	}
}
