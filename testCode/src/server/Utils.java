package server;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Utils {
	public static class Meta{
		/**
		 * Gets the id of the sending device.
		 * @param messageIncludingMeta data package holding meta information and the plain message
		 * @return the id of the sending device
		 */
		public static byte getSending(byte[] messageIncludingMeta) {
			return messageIncludingMeta[0];
		}
		
		/**
		 * Gets the id of the receiving device.
		 * @param messageIncludingMeta message holding meta information and the plain message
		 * @return the id of the receiving device
		 */
		public static byte getReceiving(byte[] messageIncludingMeta) {
			return messageIncludingMeta[1];
		}
		
		/**
		 * Adds the meta information to the plain message.
		 * @param sendingClientId id of the sending device
		 * @param receivingClientId id of the receiving device
		 * @param message the plain message
		 * @return byte array holding meta information and the plain message
		 */
		public static byte[] addMetaToMessage(byte sendingClientId, byte receivingClientId, byte[] message) {
			byte[] messageIncludingMeta = new byte[message.length + 2];
			messageIncludingMeta[0] = sendingClientId;
			messageIncludingMeta[1] = receivingClientId;
			for(int i = 2; i<messageIncludingMeta.length; i++) {
				messageIncludingMeta[i] = message[i-2];
			}
			return messageIncludingMeta;
		}
		
		/**
		 * Prints a byte array (just for debug purposes).
		 * @param array
		 */
		public static void printByteArray(byte[] array) {
			System.out.print("[");
			for(int i=0; i<array.length; i++) {
				if(i+1 != array.length) System.out.print(array[i] + ", ");
				else System.out.print(array[i]);
			}
			System.out.println("]");
		}
	}
	
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
