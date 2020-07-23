package com.stamacoding.rsaApp.rsa.endecrypt;

import com.stamacoding.rsaApp.rsa.keyCreate.Key;

public class EnDeCrypt {
	
	public static long[] encode(long[] decodedData, Key publicKey) {
		for (int i=0; i<=decodedData.length; i++) {
			decodedData[i] = quickPotentiate(decodedData[i], publicKey);
			/**
			 * the main RSA-calculation
			 */
		}
		return decodedData;
	}
	
	public static long[] decode(long[] encodedData, Key privateKey) {
		for (int i=0; i<=encodedData.length; i++) {
			encodedData[i] = quickPotentiate(encodedData[i], privateKey);
			/**
			 * the main RSA-calculation
			 */
		}
		return encodedData;
	}
	
	public static long quickPotentiate (long data, Key someKey) {
		long result = data;
		for (int counter = 1; counter < someKey.getExp(); counter++) {
			result = (result * data) % someKey.getMod();
			/**
			 * uses the modulo operator directly to speed up the RSA-calculation
			 */
		}
		return result;
	}
	
	public static void main(String[] agrs) {
		Key test = new Key(7,10);
		System.out.println(quickPotentiate(6, test));
	}
	
}
