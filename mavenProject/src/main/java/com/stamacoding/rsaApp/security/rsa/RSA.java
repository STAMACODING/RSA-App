package com.stamacoding.rsaApp.security.rsa;

import com.stamacoding.rsaApp.log.logger.Logger;

public class RSA {
	
	public static long[] encrypt(long[] decodedData, Key publicKey) {
		if(decodedData == null || publicKey == null) Logger.error(RSA.class.getSimpleName(), new IllegalArgumentException("Null arguments are not allowed!"));
		
		for (int i=0; i<=decodedData.length; i++) {
			decodedData[i] = quickPotentiate(decodedData[i], publicKey);
			/**
			 * the main RSA-calculation
			 */
		}
		return decodedData;
	}
	
	public static long[] decrypt(long[] encodedData, Key privateKey) {
		if(encodedData == null || privateKey == null) Logger.error(RSA.class.getSimpleName(), new IllegalArgumentException("Null arguments are not allowed!"));
		
		for (int i=0; i<=encodedData.length; i++) {
			encodedData[i] = quickPotentiate(encodedData[i], privateKey);
			/**
			 * the main RSA-calculation
			 */
		}
		return encodedData;
	}
	
	private static long quickPotentiate(long data, Key someKey) {
		long result = data;
		for (int counter = 1; counter < someKey.getExp(); counter++) {
			result = (result * data) % someKey.getMod();
			/**
			 * uses the modulo operator directly to speed up the RSA-calculation
			 */
		}
		return result;
	}
	
}
