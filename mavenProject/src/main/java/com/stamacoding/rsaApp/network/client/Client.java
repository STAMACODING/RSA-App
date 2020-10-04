package com.stamacoding.rsaApp.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.Callable;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.logger.Level;
import com.stamacoding.rsaApp.logger.file.FileMode;
import com.stamacoding.rsaApp.network.client.service.MainService;
import com.stamacoding.rsaApp.network.client.service.message.SendService;
import com.stamacoding.rsaApp.network.global.TextUtils;
import com.stamacoding.rsaApp.network.global.message.Message;
import com.stamacoding.rsaApp.network.global.message.data.LocalData;
import com.stamacoding.rsaApp.network.global.message.data.ProtectedData;
import com.stamacoding.rsaApp.network.global.message.data.SendState;
import com.stamacoding.rsaApp.network.global.message.data.ServerData;

/**
 * Tests the {@link MainService} on the client-side.
 */
public class Client {
	
	public static void main(String[] args) {		
		Thread.currentThread().setName("ClientStartThread");
		L.Config.Console.set(true, Level.INFO, false);
		L.Config.File.set(true, Level.INFO, true, 4 * 1024 * 1024, FileMode.ONE_PER_THREAD);
		
		System.out.print(TextUtils.heading("RSA-App Client v0.1.0"));
		System.out.print(TextUtils.box(""));
		Scanner s = new Scanner(System.in);
		
		System.out.print(TextUtils.inputBefore("Set your desired console log level [T:0|D:1|I:2|W:3|E:4|F:5]"));
		L.Config.Console.LEVEL = Level.parseInt(s.nextInt());
		System.out.print(TextUtils.inputAfter());
		
		System.out.print(TextUtils.inputBefore("Set your desired file log level [T:0|D:1|I:2|W:3|E:4|F:5]"));
		L.Config.File.LEVEL = Level.parseInt(s.nextInt());
		System.out.print(TextUtils.inputAfter());
		
		Config.read();
		do {
			Config.log();
			System.out.print(TextUtils.inputBefore("Want to change your configurations? (y/n)"));
			boolean change = s.next().equals("y");
			System.out.print(TextUtils.inputAfter());
			
			if(!change) break;
			
			System.out.print(TextUtils.inputBefore("Set client receive port"));
			Config.RECEIVE_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set client send port"));
			Config.SEND_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set sign-up port"));
			Config.SIGNUP_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set log-in port"));
			Config.LOGIN_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set ping port"));
			Config.PING_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set server's ip"));
			Config.SERVER_IP = s.next();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Your username"));
			Config.USER_NAME = s.next();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Your password"));
			Config.USER_PASSWORD = s.next().toCharArray();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Are you a registered user? (y/n)"));
			Config.REGISTERED = s.next().equals("y");
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set query-interval (in milliseconds)"));
			Config.QUERY_MESSAGES_INTERVAL = s.nextLong();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set retry-signup-interval (in milliseconds)"));
			Config.RETRY_SIGNUP_INTERVAL = s.nextLong();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set retry-login-interval (in milliseconds)"));
			Config.RETRY_LOGIN_INTERVAL = s.nextLong();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set ping-interval (in milliseconds)"));
			Config.PING_INTERVAL = s.nextLong();
			System.out.print(TextUtils.inputAfter());
			
			Config.save();
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
		
		MainService.getInstance().launch();
		if(input.equals("y")) {
			Message m = new Message(new LocalData(-1, SendState.PENDING), new ProtectedData(message, System.currentTimeMillis()), new ServerData(Config.USER_NAME, userReceiving));
			
			SendService.getInstance().execute(new Callable<Object>() {
				
				@Override
				public Object call() throws Exception {
					return SendService.getInstance().send(m);
				}
			});
		}
	}
}
