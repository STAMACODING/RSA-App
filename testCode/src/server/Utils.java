package server;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Utils {
	
	public static class Ip{
		/**
		 * Gets the current device's local ip address.
		 * @return
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
	
	public static class Convert{
		
		public static String byteArrayToString(byte[] byteMessage) {
			return "Placeholder-Text";
		}
		
	}
}
