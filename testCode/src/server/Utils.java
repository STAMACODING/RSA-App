package server;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import server.services.Message;

public class Utils {
	
	public static class Ip{
		/**
		 * Gets the current device's local ip address.
		 * @return the current device's local ip address
		 */
		public static String getIpAdress() {
			try {
				return Inet4Address.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static class RSA{
		
		public static byte[] encode(String textMessage) {
			return new byte[] {23, 12, 111, 2, 11};
		}

		public static String decode(byte[] byteMessage) {
			return "Hallo Welt";
		}
		
	}
}
