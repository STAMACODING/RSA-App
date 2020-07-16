package com.stamacoding.rsaApp.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.stamacoding.rsaApp.server.client.managers.ClientMessageManager;
import com.stamacoding.rsaApp.server.client.services.ClientMainService;
import com.stamacoding.rsaApp.server.message.Message;
import com.stamacoding.rsaApp.server.message.data.LocalData;
import com.stamacoding.rsaApp.server.message.data.ProtectedData;
import com.stamacoding.rsaApp.server.message.data.SendState;
import com.stamacoding.rsaApp.server.message.data.ServerData;

/**
 * Tests the {@link ClientMainService} on the client-side.
 */
public class Client {
	
	public static void main(String[] args) {
		System.out.println("------------------------------------------------------------");
		System.out.println("RSA-App Server 0.0.0-SNAPSHOT");
		System.out.println("------------------------------------------------------------");
		Scanner s = new Scanner(System.in);
		
		System.out.print("Client send port: ");
		ClientConfig.SEND_PORT = s.nextInt();
		
		System.out.print("Client receive port: ");
		ClientConfig.RECEIVE_PORT = s.nextInt();
		
		System.out.print("Server ip: ");
		ClientConfig.SERVER_IP = s.next();
		
		System.out.print("Setup your id: ");
		ClientConfig.ID = s.nextByte();
		
		System.out.print("Setup query-interval (in milliseconds): ");
		ClientConfig.QUERY_MESSAGES_INTERVAL = s.nextLong();
		
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
		
		ClientMainService.getInstance().launch();
		if(input.equals("y")) {
			Message m = new Message(new LocalData(-1, SendState.PENDING), new ProtectedData(message, System.currentTimeMillis()), new ServerData(ClientConfig.ID, idReceiving));
			ClientMessageManager.getInstance().manage(m);
		}
	}
}
