package server.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import server.config.NetworkConfig;
import server.config.NetworkType;
import server.message.Message;
import server.message.data.LocalData;
import server.message.data.ProtectedData;
import server.message.data.SendState;
import server.message.data.ServerData;
import server.services.databaseService.MessageManager;
import server.services.mainService.MessageService;

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
		
		NetworkConfig.TYPE = NetworkType.CLIENT;
		MessageService.getInstance().start();
		if(input.equals("y")) {
			Message m = new Message(new LocalData(-1, SendState.PENDING), new ProtectedData(message, System.currentTimeMillis()), new ServerData(NetworkConfig.Client.ID, idReceiving));
			MessageManager.manage(m);
		}
	}
}
