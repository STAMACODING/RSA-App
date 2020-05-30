package server;

import java.io.*;
import java.net.*;

public class TestServer {

	public void go() {
		try {
			ServerSocket serverSock = new ServerSocket(9000); // here insert any free port number

			while (true) {
				Socket sock = serverSock.accept();

				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				String test = getTestO();
				writer.println(test);
				writer.close();
				System.out.println(test);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}// close go

	private String getTestO() {
		String x = " Hello Client";
		System.out.println("new server up!!"); //////somehwo this part of the function doesn't get called on a second try,
		//but the client still receives the message from first server who shouldn't be running anymore
		
		return x;
	
	}
	
	public static void main(String[] args) {
		TestServer server = new TestServer();
		server.go();
	}
}

// simple Client Server interaktion
// 1. run server class
// 2. run client class
// message test willbe sending on the selected port "forever"
// when you rerun server class with same port you will get an binding..... Exception
// to "stop" the "server" :
// 1. find application number of running application on port, in cmd: netstat -ano | find "your portnumber"
// the number at the end of the line will be the applicationNumber
// 2. to stop, in cmd: taskkill /f /PDI applicationNumber
// now you could technally rerun the server class
// --------------------------------------------------------------------------------------------------
// it somehow doesn't change when you change sth in the server class, maybe sb. can figure this out
// --------------------------------------------------------------------------------------------------
// goal :
// send and receive messages between 2 different devices, you would have to use the real ip addresse and port of our server(RasberryPie)
