package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Utils {
	public static class Meta{
		/**
		 * Gets the IPv4 address of the sending device.
		 * @param dataPackage data package holding meta information and the encrypted message
		 * @return the IPv4 address of the sending device
		 */
		public static byte getSending(byte[] messageIncludingMeta) {
			return messageIncludingMeta[0];
		}
		
		/**
		 * Gets the IPv4 address of the receiving device.
		 * @param dataPackage data package holding meta information and the encrypted message
		 * @return the IPv4 address of the receiving device
		 */
		public static byte getReceiving(byte[] messageIncludingMeta) {
			return messageIncludingMeta[1];
		}
		
		/**
		 * Adds the meta information to the encrypted message.
		 * @param sendingIpAdress IP address of the sending device
		 * @param receivingIpAdress IP address of the receiving device
		 * @param encryptedMessage encrypted message
		 * @return array holding meta information and the encrypted message
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
		
		public static void main(String[] args) {
			// Example
			
			byte idSending = 23;
			byte idReceiving = 11;
			byte[] message = {22, 11, 23, 11, 0, 3, 3, 23, 12, 12, 60, 90, 11, 10};
			
			// Adds meta information to the encrypted message
			byte[] messageIncludingMeta = addMetaToMessage(idSending, idReceiving, message);
			
			System.out.println("encrypted message: \t");
			printByteArray(message);
			System.out.println("encrypted message + meta information: \t");
			printByteArray(messageIncludingMeta);
			
			// Excludes meta information
			System.out.println("Sending: \t" + getSending(messageIncludingMeta));
			System.out.println("Receiving: \t" + getReceiving(messageIncludingMeta));
		}
	}
	
	public static class Ip{
		public static String getIpAdress() {
			try {
				return Inet4Address.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
