package server.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import server.Client;
import server.SendQueue;
import server.Server;
import server.Utils;

public class TestClient {
	public static void main(String[] args) {
		System.out.println("------------------------------------------------------------");
		System.out.println("RSA-App Test Client BETA");
		System.out.println("------------------------------------------------------------");
		Scanner s = new Scanner(System.in);
		System.out.print("Server send port: ");
		Server.SEND_PORT = s.nextInt();
		System.out.print("Server receive port: ");
		Server.RECEIVE_PORT = s.nextInt();
		System.out.print("Server ip: ");
		Server.IP = s.next();
		System.out.print("Setup your id: ");
		Client.ID = s.nextByte();
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
		
		Client.run();
		if(input.equals("y")) SendQueue.add(Utils.Meta.addMetaToMessage(Client.ID, idReceiving, message.getBytes()));
	}
}
