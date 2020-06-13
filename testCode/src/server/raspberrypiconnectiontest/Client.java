package server.raspberrypiconnectiontest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import com.stamacoding.rsaApp.log.logger.Logger;

public class Client {
	public static void main(String[] args) {
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		System.out.println("Raspberry Pi Connection Test (Client Application)");
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		
		Scanner s = new Scanner(System.in);
		
		System.out.println("Enter raspberry's IP: ");
		String raspberryIP = s.nextLine();
		System.out.println("Enter raspberry's port: ");
		int raspberryPort = s.nextInt();
		s.close();
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		try {
			Logger.debug(RaspberryPi.class.getSimpleName(), "Trying to connect to raspberry pi (" + raspberryIP + ":" + raspberryPort + ")");
			
			Socket socket = new Socket(raspberryIP, raspberryPort);
			
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			
			Logger.debug(RaspberryPi.class.getSimpleName(), "Connected to raspberry pi (" + raspberryIP + ":" + raspberryPort + ")");
			
			String messageFromRaspberry = reader.readLine();
			reader.close();
			socket.close();
			System.out.println("--------------------------------------------------------------------------------------------------------------------");
			Logger.debug(RaspberryPi.class.getSimpleName(), "Result: SUCCESS");
			System.out.println("--------------------------------------------------------------------------------------------------------------------");
			
		} catch (IOException e) {
			System.out.println("--------------------------------------------------------------------------------------------------------------------");
			Logger.debug(RaspberryPi.class.getSimpleName(), "Result: ERROR (NOT UNAVAILABLE)");
			System.out.println("--------------------------------------------------------------------------------------------------------------------");
			
		}
	}
}
