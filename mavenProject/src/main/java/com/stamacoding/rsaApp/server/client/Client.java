package com.stamacoding.rsaApp.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.stamacoding.rsaApp.server.TextUtils;
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
		
		System.out.print(TextUtils.heading("RSA-App Client 1.0.0-alpha"));
		System.out.print(TextUtils.box(""));
		Scanner s = new Scanner(System.in);
		
		System.out.print(TextUtils.inputBefore("Set client receive port"));
		ClientConfig.RECEIVE_PORT = s.nextInt();
		System.out.print(TextUtils.inputAfter());
		
		System.out.print(TextUtils.inputBefore("Set client send port"));
		ClientConfig.SEND_PORT = s.nextInt();
		System.out.print(TextUtils.inputAfter());
		
		System.out.print(TextUtils.inputBefore("Set server's ip"));
		ClientConfig.SERVER_IP = s.next();
		System.out.print(TextUtils.inputAfter());
		
		System.out.print(TextUtils.inputBefore("Your username"));
		ClientConfig.USER_NAME = s.next();
		System.out.print(TextUtils.inputAfter());
		
		System.out.print(TextUtils.inputBefore("Your password"));
		ClientConfig.USER_PASSWORD = s.next();
		System.out.print(TextUtils.inputAfter());
		
		System.out.print(TextUtils.inputBefore("Set query-interval (in milliseconds)"));
		ClientConfig.QUERY_MESSAGES_INTERVAL = s.nextLong();
		System.out.print(TextUtils.inputAfter());
		
		System.out.print(TextUtils.inputBefore("Do you want to send a message? (y/n)"));
		String input = s.next(), message = "";
		System.out.print(TextUtils.inputAfter());
		
		String userReceiving = null;
		
		if(input.equals("y")) {
			System.out.print(TextUtils.inputBefore("Set message"));
			try {
				message = new BufferedReader(new InputStreamReader(System.in)).readLine();
				System.out.print(TextUtils.inputAfter());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.print(TextUtils.inputBefore("Set receiver's username"));
			userReceiving = s.next();
			System.out.print(TextUtils.inputAfter());
		}
	

		
		s.close();
		System.out.print(TextUtils.heading("STARTING CLIENT NOW...") + "\n");
		
		ClientMainService.getInstance().launch();
		if(input.equals("y")) {
			Message m = new Message(new LocalData(-1, SendState.PENDING), new ProtectedData(message, System.currentTimeMillis()), new ServerData(ClientConfig.USER_NAME, userReceiving));
			ClientMessageManager.getInstance().manage(m);
		}
	}
}
