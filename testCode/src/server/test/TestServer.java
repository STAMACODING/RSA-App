package server.test;

import java.util.Scanner;

import server.Server;
import server.Utils;

public class TestServer {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("------------------------------------------------------------");
		System.out.println("RSA-App Test Server BETA");
		System.out.println("------------------------------------------------------------");
		
		System.out.print("Server send port: ");
		Server.SEND_PORT = s.nextInt();
		
		System.out.print("Server receive port: ");
		Server.RECEIVE_PORT = s.nextInt();
		
		s.close();
		System.out.println("------------------------------------------------------------");
		
		Server.IP = Utils.Ip.getIpAdress();
		Server.run();
	}
}
