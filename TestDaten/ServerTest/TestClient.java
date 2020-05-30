package testpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TestClient {
	
	public void go() {
		try {
			Socket s = new Socket("127.0.0.1", 10000);  // constructor of socket takes ("ipAdresse", port number); 127.0.0.1 is the default ip addresse when directing to ports on same device
			
			InputStreamReader streamReader = new InputStreamReader(s.getInputStream()); //reads messages from server
			BufferedReader reader = new BufferedReader(streamReader); // translates bit stream into characters
			
			String testoutcome = reader.readLine();
			System.out.println(testoutcome);
			reader.close();
			
		} catch(IOException ex){
			System.out.println("connection failed");
			ex.printStackTrace();
			
		}
	}
	
	public static void main (String [] args) {
		TestClient client = new TestClient();
		client.go();
	}
}
