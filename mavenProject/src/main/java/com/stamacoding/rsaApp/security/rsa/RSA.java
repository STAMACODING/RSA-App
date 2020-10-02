package com.stamacoding.rsaApp.security.rsa;

import com.stamacoding.rsaApp.logger.L;

/**
 * Class containing methods to encrypt/decrypt long arrays using the RSA algorithm.
 */
public class RSA {
	
	/**
	 * Encrypts a long array.
	 * @param data the array to encrypt
	 * @param publicKey the key to use
	 * @return the encrypted array
	 */
	public static long[] encrypt(long[] data, Key publicKey) {
		if(data == null || publicKey == null) L.f("RSA", RSA.class, new IllegalArgumentException("Null arguments are not allowed!"));
		
		for (int i=0; i<=data.length; i++) {
			data[i] = quickPotentiate(data[i], publicKey);
		}
		return data;
	}
	
	/**
	 * Decrypts an encrypted long array.
	 * @param encrypted the encrypted array
	 * @param privateKey the key to use
	 * @return the decrypted array
	 */
	public static long[] decrypt(long[] encrypted, Key privateKey) {
		if(encrypted == null || privateKey == null) L.f("RSA", RSA.class, new IllegalArgumentException("Null arguments are not allowed!"));
		
		for (int i=0; i<=encrypted.length; i++) {
			encrypted[i] = quickPotentiate(encrypted[i], privateKey);
		}
		return encrypted;
	}
	
	private static long quickPotentiate(long data, Key someKey) {
		long result = data;
		for (int counter = 1; counter < someKey.getExp(); counter++) {
			result = (result * data) % someKey.getMod();
		}
		return result;
	}
	
}
