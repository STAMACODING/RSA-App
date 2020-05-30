package com.stamacoding.rsaApp.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 
 * This class contains static functions providing the ability to exclude specific the meta information from a transferred byte array.
 *
 */
public class MetaUtils {
	/**
	 * Gets the IPv4 address of the sending device.
	 * @param dataPackage data package holding meta information and the crypted message
	 * @return the IPv4 address of the sending device
	 */
	public static String getSending(byte[] dataPackage) {
		byte[] sending = new byte[15];
		
		// Excludes bytes holding the IPv4 address of the sending device
		for(int i=0; i<sending.length; i++) {
			sending[i] = dataPackage[i];
		}
		// Converts bytes into string
		String rawResult = new String(sending);
		
		// Removes '-' from meta information (StringBuilder is faster in concatenating strings)
		StringBuilder result = new StringBuilder();
		for(int i=0; i<rawResult.length(); i++) {
			if(rawResult.charAt(i) != '-') {
				result.append(rawResult.charAt(i));
			}
		}
		return result.toString();
	}
	
	/**
	 * Gets the IPv4 adress of the receiving device.
	 * @param dataPackage data package holding meta information and the crypted message
	 * @return the IPv4 adress of the receiving device
	 */
	public static String getReceiving(byte[] dataPackage) {
		byte[] receiving = new byte[15];
		
		// Excludes bytes holding the IPv4 address of the receiving device
		for(int i=15; i<15+receiving.length; i++) {
			receiving[i-15] = dataPackage[i];
		}
		// Converts bytes into string
		String rawResult = new String(receiving);
		
		// Removes '-' from meta information (StringBuilder is faster in concatenating strings)
		StringBuilder result = new StringBuilder();
		for(int i=0; i<rawResult.length(); i++) {
			if(rawResult.charAt(i) != '-') {
				result.append(rawResult.charAt(i));
			}
		}
		return result.toString();
	}
	
	/**
	 * Adds the meta information to the crypted message.
	 * @param sendingIpAdress ip address of the sending device
	 * @param receivingIpAdress ip address of the receiving device
	 * @param cryptedMessage crypted message
	 * @return array holding meta information and the crypted message
	 */
	public static byte[] addMetaToMessage(String sendingIpAdress, String receivingIpAdress, byte[] cryptedMessage) {
		// Converts sendingIpAdress into a byte array (length: 15)
		byte[] sending = new byte[15];
		byte[] tempArray1 = sendingIpAdress.getBytes();
		for(int i=0; i<sending.length; i++) {
			if(i >= tempArray1.length) {
				sending[i] = "-".getBytes()[0];
			}else {
				sending[i] = tempArray1[i];
			}
		}
		
		// Converts receivingIpAdress into a byte array (length: 15)
		byte[] receiving = new byte[15];
		byte[] tempArray2 = receivingIpAdress.getBytes();
		for(int i=0; i<receiving.length; i++) {
			if(i >= tempArray2.length) {
				receiving[i] = "-".getBytes()[0];
			}else {
				receiving[i] = tempArray2[i];
			}
		}

		// Merges all arrays together
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(sending);
			outputStream.write(receiving);
			outputStream.write(cryptedMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputStream.toByteArray();
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
		
		String ipSending = "192.123.123";
		String ipReceiving = "121.23.1123";
		byte[] cryptedMessage = {22, 11, 23, 11, 0, 3, 3, 23, 12, 12, 60, 90, 11, 10};
		
		// Adds meta information to the crypted message
		byte[] dataPackage = addMetaToMessage(ipSending, ipReceiving, cryptedMessage);
		
		System.out.println("Crypted message: \t");
		printByteArray(cryptedMessage);
		System.out.println("Crypted message + meta information: \t");
		printByteArray(dataPackage);
		
		// Excludes meta information
		System.out.println("Sending: \t" + getSending(dataPackage));
		System.out.println("Receiving: \t" + getReceiving(dataPackage));
	}
}
