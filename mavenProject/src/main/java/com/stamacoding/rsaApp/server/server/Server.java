package com.stamacoding.rsaApp.server.server;

import java.util.Scanner;

import com.stamacoding.rsaApp.server.client.services.ClientMainService;
import com.stamacoding.rsaApp.server.server.services.ServerMainService;

/**
 * Tests the {@link ClientMainService} on the server-side.
 */
public class Server {
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("------------------------------------------------------------");
		System.out.println("RSA-App Server 0.0.0-SNAPSHOT");
		System.out.println("------------------------------------------------------------");
		
		System.out.print("Server send port: ");
		ServerConfig.SEND_PORT = s.nextInt();
		
		System.out.print("Server receive port: ");
		ServerConfig.RECEIVE_PORT = s.nextInt();
		
		s.close();
		System.out.println("------------------------------------------------------------");
		
		ServerMainService.getInstance().launch();
	}
}
