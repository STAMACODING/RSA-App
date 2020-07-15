package com.stamacoding.rsaApp.server.run;

import java.util.Scanner;

import com.stamacoding.rsaApp.server.NetworkUtils;
import com.stamacoding.rsaApp.server.config.NetworkConfig;
import com.stamacoding.rsaApp.server.config.NetworkType;
import com.stamacoding.rsaApp.server.services.mainService.MessageService;

/**
 * Tests the {@link MessageService} on the server-side.
 */
public class Server {
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("------------------------------------------------------------");
		System.out.println("RSA-App Server 0.0.0-SNAPSHOT");
		System.out.println("------------------------------------------------------------");
		
		System.out.print("Server send port: ");
		NetworkConfig.Server.SEND_PORT = s.nextInt();
		
		System.out.print("Server receive port: ");
		NetworkConfig.Server.RECEIVE_PORT = s.nextInt();
		
		s.close();
		System.out.println("------------------------------------------------------------");
		
		NetworkConfig.Server.IP = NetworkUtils.getIpAdress();
		NetworkConfig.TYPE = NetworkType.SERVER;
		
		MessageService.getInstance().launch();
	}
}
