package server.New;

import java.net.*;
import java.io.*;

public class ThreadHandlerRunnable implements Runnable{

	int i;
	
	public void run() {
		createThreads(3);
	}
	
	public void createThreads(int n) {
		for  (i= 0; i>n; i++) {
			Thread thread = new Thread();
			thread.setName("thread0" + i);
		}
	}
}
