package com.stamacoding.rsaApp.network.server;

import java.util.Scanner;

import com.stamacoding.rsaApp.network.client.services.ClientMainService;
import com.stamacoding.rsaApp.network.global.TextUtils;
import com.stamacoding.rsaApp.network.server.services.ServerMainService;

/**
 * Tests the {@link ClientMainService} on the server-side.
 */
public class Server {
	
	public static void main(String[] args) {
		Thread.currentThread().setName("ServerStartThread");
		
		System.out.print(TextUtils.heading("RSA-App Server 1.0.0-alpha"));
		System.out.print(TextUtils.box(""));
		Scanner s = new Scanner(System.in);
		
		ServerConfig.read();
		do {
			ServerConfig.log();
			System.out.print(TextUtils.inputBefore("Want to change your configurations? (y/n)"));
			boolean change = s.next().equals("y");
			System.out.print(TextUtils.inputAfter());
			
			if(!change) break;
			
			System.out.print(TextUtils.inputBefore("Set server send port"));
			ServerConfig.SEND_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set server receive port"));
			ServerConfig.RECEIVE_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set sign-up port"));
			ServerConfig.SIGNUP_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set log-in port"));
			ServerConfig.LOGIN_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set ping port"));
			ServerConfig.PING_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			ServerConfig.save();
		}while (true);
		
		s.close();
		System.out.print(TextUtils.heading("STARTING SERVER NOW...") + "\n");
		
		ServerMainService.getInstance().launch();
	}
}
