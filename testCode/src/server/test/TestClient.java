package server.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Scanner;

import server.NetworkService;
import server.config.NetworkConfig;
import server.config.Type;
import server.services.transferServices.TransferMessage;
import server.services.transferServices.sendService.SendQueue;

public class TestClient {
	public static void main(String[] args) {
		System.out.println("------------------------------------------------------------");
		System.out.println("RSA-App Test Client BETA");
		System.out.println("------------------------------------------------------------");
		Scanner s = new Scanner(System.in);
		
		System.out.print("Server send port: ");
		NetworkConfig.Server.SEND_PORT = s.nextInt();
		
		System.out.print("Server receive port: ");
		NetworkConfig.Server.RECEIVE_PORT = s.nextInt();
		
		System.out.print("Server ip: ");
		NetworkConfig.Server.IP = s.next();
		
		System.out.print("Setup your id: ");
		NetworkConfig.Client.ID = s.nextByte();
		
		System.out.println("------------------------------------------------------------");
		
		System.out.print("Do you want to send a message? (y/n): ");
		String input = s.next(), message = "";
		byte idReceiving = 0;
		
		if(input.equals("y")) {
			System.out.print("Type in the message: ");
			try {
				message = new BufferedReader(new InputStreamReader(System.in)).readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.print("Type in the receiver's id: ");
			idReceiving = s.nextByte();
		}
	

		
		s.close();
		System.out.println("------------------------------------------------------------");
		
		NetworkConfig.TYPE = Type.CLIENT;
		NetworkService.getInstance().start();
		if(input.equals("y")) {
			TransferMessage t = new TransferMessage(message.getBytes(), NetworkConfig.Client.ID, idReceiving, new Date(System.currentTimeMillis()));
			SendQueue.add(t);
		}
	}
}
