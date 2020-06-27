package server.New;

public class TestRunnable implements Runnable {

	public void run() {
		
		test();
	}
	
	public void test() {
		String testString = "test worked!!";
		System.out.println(testString);
	}
}
