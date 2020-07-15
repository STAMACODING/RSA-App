package com.stamacoding.rsaApp.server.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.stamacoding.rsaApp.server.config.NetworkConfig;
import com.stamacoding.rsaApp.server.config.NetworkType;
import com.stamacoding.rsaApp.server.message.Message;
import com.stamacoding.rsaApp.server.message.MessageManager;
import com.stamacoding.rsaApp.server.message.data.LocalData;
import com.stamacoding.rsaApp.server.message.data.ProtectedData;
import com.stamacoding.rsaApp.server.message.data.SendState;
import com.stamacoding.rsaApp.server.message.data.ServerData;
import com.stamacoding.rsaApp.server.services.mainService.MessageService;

/**
 * Tests the {@link MessageService} on the client-side.
 */
public class Client {
	
	public static void main(String[] args) {
		System.out.println("------------------------------------------------------------");
		System.out.println("RSA-App Server 0.0.0-SNAPSHOT");
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
		MessageService.getInstance().launch();
		if(input.equals("y")) {
			Message m = new Message(new LocalData(-1, SendState.PENDING), new ProtectedData(message, System.currentTimeMillis()), new ServerData(NetworkConfig.Client.ID, idReceiving));
			MessageManager.manage(m);
		}
	}
}
