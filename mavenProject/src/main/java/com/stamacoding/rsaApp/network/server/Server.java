package com.stamacoding.rsaApp.network.server;

import java.util.Scanner;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.logger.Level;
import com.stamacoding.rsaApp.logger.file.FileMode;
import com.stamacoding.rsaApp.network.global.TextUtils;
import com.stamacoding.rsaApp.network.server.service.MainService;

/**
 * Tests the {@link MainService} on the server-side.
 */
public class Server {
	
	public static void main(String[] args) {
		Thread.currentThread().setName("ServerStartThread");
		L.Config.Console.set(true, Level.INFO, false);
		L.Config.File.set(true, Level.INFO, true, 4 * 1024 * 1024, FileMode.ONE_PER_THREAD);
		
		System.out.print(TextUtils.heading("RSA-App Server v0.1.0"));
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
			
			System.out.print(TextUtils.inputBefore("Set server send port"));
			Config.SEND_PORT = s.nextInt();
			System.out.print(TextUtils.inputAfter());
			
			System.out.print(TextUtils.inputBefore("Set server receive port"));
			Config.RECEIVE_PORT = s.nextInt();
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
			
			Config.save();
		}while (true);
		
		s.close();
		System.out.print(TextUtils.heading("STARTING SERVER NOW...") + "\n");
		
		MainService.getInstance().launch();
	}
}
