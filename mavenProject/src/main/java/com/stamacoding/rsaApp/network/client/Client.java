package com.stamacoding.rsaApp.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.stamacoding.rsaApp.network.client.managers.ClientMessageManager;
import com.stamacoding.rsaApp.network.client.services.ClientMainService;
import com.stamacoding.rsaApp.network.global.TextUtils;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.global.message.data.LocalData;
import com.stamacoding.rsaApp.network.global.message.data.ProtectedData;
import com.stamacoding.rsaApp.network.global.message.data.SendState;
import com.stamacoding.rsaApp.network.global.message.data.ServerData;

/**
 * Tests the {@link ClientMainService} on the client-side.
 */
public class Client {
	
	public static void main(String[] args) {
		
		System.out.print(TextUtils.heading("RSA-App Client 1.0.0-alpha"));
		System.out.print(TextUtils.box(""));
		Scanner s = new Scanner(System.in);
		
		ClientConfig.read();
		do {
			ClientConfig.log();
			System.out.print(TextUtils.inputBefore("Want to change your configurations? (y/n)"));
			boolean change = s.next().equals("y");
			System.out.print(TextUtils.inputAfter());
			
			if(!change) break;
			
			System.out.print(TextUtils.inputBefore("Set client receive port"));
			ClientConfig.RECEIVE_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set client send port"));
			ClientConfig.SEND_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set sign-up port"));
			ClientConfig.SIGNUP_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set log-in port"));
			ClientConfig.LOGIN_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set ping port"));
			ClientConfig.PING_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set server's ip"));
			ClientConfig.SERVER_IP = s.next();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Your username"));
			ClientConfig.USER_NAME = s.next();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Your password"));
			ClientConfig.USER_PASSWORD = s.next().toCharArray();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Are you a registered user? (y/n)"));
			ClientConfig.REGISTERED = s.next().equals("y");
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set query-interval (in milliseconds)"));
			ClientConfig.QUERY_MESSAGES_INTERVAL = s.nextLong();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set retry-signup-interval (in milliseconds)"));
			ClientConfig.RETRY_SIGNUP_INTERVAL = s.nextLong();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set retry-login-interval (in milliseconds)"));
			ClientConfig.RETRY_LOGIN_INTERVAL = s.nextLong();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set ping-interval (in milliseconds)"));
			ClientConfig.PING_INTERVAL = s.nextLong();
			System.out.print(TextUtils.inputAfter());
			
			ClientConfig.save();
		} while(true);
		
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
