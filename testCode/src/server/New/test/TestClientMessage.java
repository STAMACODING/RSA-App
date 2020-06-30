package server.New.test;

import java.util.Scanner;

import com.stamacoding.rsaApp.server.MetaUtils;

import server.New.Client;
import server.New.SendQueue;
import server.New.Server;

public class TestClientMessage {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Client receive port: ");
		Client.RECEIVE_PORT = s.nextInt();
		System.out.println("Server receive port: ");
		Server.RECEIVE_PORT = s.nextInt();
		System.out.println("Server ip: ");
		Server.SERVER_IP = s.next();
	
		byte[] message = new byte[] {12, 23, 23, 34, 1};
		String ipSending = "", ipReceiving = "";
		
		System.out.println("Sending ip (your ip!): ");
		ipSending = s.next();
		System.out.println("Receiving ip (your friend's ip!): ");
		ipReceiving = s.next();
		
		SendQueue.add(MetaUtils.addMetaToMessage(ipSending, ipReceiving, message));
		
		s.close();
		
		Client.run();
	}
}
