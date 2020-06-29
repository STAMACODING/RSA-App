package server.New;

public class TestClass {


	
	public static void main(String[] args) {
	
		Server testServer = new Server("testServer");
		Client testClient = new Client("testClient","127.0.0.1",5000);
		System.out.println("Server and Client have been created");
		
		
	}

}
