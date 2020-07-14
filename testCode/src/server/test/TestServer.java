package server.test;

import java.util.Scanner;

import server.NetworkUtils;
import server.config.NetworkConfig;
import server.config.NetworkType;
import server.services.mainService.MessageService;

/**
 * Tests the {@link MessageService} on the server-side.
 */
public class TestServer {
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("------------------------------------------------------------");
		System.out.println("RSA-App Test Server BETA");
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
