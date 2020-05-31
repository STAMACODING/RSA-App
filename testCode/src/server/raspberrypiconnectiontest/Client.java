package server.raspberrypiconnectiontest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import com.stamacoding.rsaApp.log.logger.Logger;

public class Client {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		System.out.println("Enter raspberry's IP: ");
		String raspberryIP = s.nextLine();
		System.out.println("Enter raspberry's port: ");
		int raspberryPort = s.nextInt();
		s.close();
		try {
			Logger.debug(RaspberryPi.class.getSimpleName(), "Trying to connect to raspberry pi (" + raspberryIP + ":" + raspberryPort + ")");
			Socket socket = new Socket(raspberryIP, raspberryPort);
			
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			
			Logger.debug(RaspberryPi.class.getSimpleName(), "Connected to raspberry pi (" + raspberryIP + ":" + raspberryPort + ")");
			
			String messageFromRaspberry = reader.readLine();
			Logger.debug(RaspberryPi.class.getSimpleName(), "Raspberry said: " + messageFromRaspberry);
			reader.close();
			socket.close();
			Logger.debug(RaspberryPi.class.getSimpleName(), "Test was successfull!");
		} catch (IOException e) {
			Logger.error(RaspberryPi.class.getSimpleName(), "Connection failed :( \n" + e.getMessage());
		}
	}
}
