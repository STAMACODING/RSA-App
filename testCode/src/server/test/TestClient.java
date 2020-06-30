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
		System.out.print("Client receive port: ");
		Client.RECEIVE_PORT = s.nextInt();
		System.out.print("Server receive port: ");
		Server.RECEIVE_PORT = s.nextInt();
		System.out.print("Server ip: ");
		Server.SERVER_IP = s.next();
		System.out.println("------------------------------------------------------------");
		
		System.out.print("Do you want to send a message? (y/n): ");
		String input = s.next(), message = "", ipReceiving = "";
		
		if(input.equals("y")) {
			//byte[] message = new byte[] {12, 23, 23, 34, 1};
			
			
			System.out.print("Type in the message: ");
			try {
				message = new BufferedReader(new InputStreamReader(System.in)).readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.print("Type in the receiver's local ip address: ");
			ipReceiving = s.next();
		}
	

		
		s.close();
		System.out.println("------------------------------------------------------------");
		
		Client.run();
		if(input.equals("y")) SendQueue.add(Utils.Meta.addMetaToMessage(Utils.Ip.getIpAdress(), ipReceiving, message.getBytes()));
	}
}
