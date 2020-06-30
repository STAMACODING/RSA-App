package server.New.test;

import java.util.Scanner;

import server.New.Client;
import server.New.Server;

public class TestClient {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Client receive port: ");
		Client.RECEIVE_PORT = s.nextInt();
		System.out.println("Server receive port: ");
		Server.RECEIVE_PORT = s.nextInt();
		System.out.println("Server ip: ");
		Server.SERVER_IP = s.next();
		s.close();
		
		Client.run();
	}
}
