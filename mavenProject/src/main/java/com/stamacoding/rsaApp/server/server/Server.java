package com.stamacoding.rsaApp.server.server;

import java.util.Scanner;

import com.stamacoding.rsaApp.server.TextUtils;
import com.stamacoding.rsaApp.server.client.services.ClientMainService;
import com.stamacoding.rsaApp.server.server.services.ServerMainService;

/**
 * Tests the {@link ClientMainService} on the server-side.
 */
public class Server {
	
	public static void main(String[] args) {
		
		System.out.print(TextUtils.heading("RSA-App Server 1.0.0-alpha"));
		System.out.print(TextUtils.box(""));
		Scanner s = new Scanner(System.in);
		
		System.out.print(TextUtils.inputBefore("Set server send port"));
		ServerConfig.SEND_PORT = s.nextInt();
		System.out.print(TextUtils.inputAfter());
		
		System.out.print(TextUtils.inputBefore("Set server receive port"));
		ServerConfig.RECEIVE_PORT = s.nextInt();
		System.out.print(TextUtils.inputAfter());
		
		s.close();
		System.out.print(TextUtils.heading("STARTING SERVER NOW...") + "\n");
		
		ServerMainService.getInstance().launch();
	}
}
