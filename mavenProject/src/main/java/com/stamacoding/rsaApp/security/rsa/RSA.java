package com.stamacoding.rsaApp.security.rsa;

import java.io.Serializable;

import com.stamacoding.rsaApp.logger.L;

/**
 * Class containing methods to encrypt/decrypt long arrays using the RSA algorithm.
 */
public class RSA {
	
	/**
	 * Fake encrypts an object by serializing it.
	 * @param o the object to encrypt
	 * @return the serialized object
	 */
	public static byte[] encryptF(Object o) {
		L.t(RSA.class, "Fake encrypting object without any key");
		return Convert.serialize(o);
	}
	
	/**
	 * Fake decrypts an object by deserializing it.
	 * @param o the object to decrypt
	 * @return the deserialized object
	 */
	public static Object decryptF(byte[] encodedByteArray) {
		L.t(RSA.class, "Fake decrypting object without any key");
		return Convert.deserialize(encodedByteArray);
	}
	
	/**
	 * Encrypts an object using the RSA algorithm.
	 * @param o the object to encrypt
	 * @param publicKey the key to use
	 * @return the encrypted object
	 */
	public static byte[] encrypt(Object o, Key publicKey) {
		if(o == null) L.f(RSA.class, new IllegalArgumentException("Cannot serialize null object!"));
		if(publicKey == null) L.f(RSA.class, new IllegalArgumentException("Cannot encrypt an object with a null key!"));
		if(!(o instanceof Serializable)) L.f(RSA.class, new IllegalArgumentException("Object is not serializable!"));
		

		L.t(RSA.class, "Encrypting object with public key: " + publicKey.toString());
		
		L.t(RSA.class, "Serializing object (object => byte[])");
		byte[] decodedSerializedObject = Convert.serialize(o);
		
		L.t(RSA.class, "Casting byte array to long array (byte[] => long[])");
		long[] decodedLongArray = Convert.byteArrayToLongArray(decodedSerializedObject);
		
		L.t(RSA.class, "Encrypting long array (long[] => long[])");
		long[] encodedLongArray = RSA.encryptLongArray(decodedLongArray, publicKey);
		
		L.t(RSA.class, "Serializing encrypted long array (long[] => byte[])");
		byte[] encodedByteArray = Convert.serialize(encodedLongArray);
		
		if(encodedByteArray != null) L.t(RSA.class, "Encrypted object with public key: " + publicKey.toString());
		else L.f(RSA.class, "Failed to encrypt object with public key: " + publicKey.toString());
		
		return encodedByteArray;
	}
	
	/**
	 * Decrypts an object using the RSA algorithm.
	 * @param encryptedByteArray the encrypted object
	 * @param privateKey the key to use
	 * @return the decrypted object
	 */
	public static Object decrypt(byte[] encryptedByteArray, Key privateKey) {
		if(encryptedByteArray == null) L.f(RSA.class, new IllegalArgumentException("Cannot decrypt a null byte array!"));
		if(privateKey == null) L.f(RSA.class, new IllegalArgumentException("Cannot decrypt an object with a null key!"));
		
		L.t(RSA.class, "Decrypting object with private key: " + privateKey.toString());
		
		L.t(RSA.class, "Deserializing encrypted byte array (byte[] => long[])");
		long[] encryptedLongArray = (long[]) Convert.deserialize(encryptedByteArray);
		
		L.t(RSA.class, "Decrypting encrypted long array (long[] => long[])");
		long[] decryptedLongArray = RSA.decryptLongArray(encryptedLongArray, privateKey);
		
		L.t(RSA.class, "Casting long array (long[] => byte[])");
		byte[] decodedByteArray = Convert.longArrayToByteArray(decryptedLongArray);
		
		L.t(RSA.class, "Deserializing byte array to object (byte[] => object)");
		Object o = Convert.deserialize(decodedByteArray);
		
		if(o!=null) L.t(RSA.class, "Successfully decrypted object with private key: " + privateKey.toString());
		else L.f(RSA.class, "Failed to decrypt object with private key: " + privateKey.toString());
		
		return o;
	}
	
	/**
	 * Encrypts a long array.
	 * @param data the array to encrypt
	 * @param publicKey the key to use
	 * @return the encrypted array
	 */
	private static long[] encryptLongArray(long[] data, Key publicKey) {
		if(data == null || publicKey == null) L.f(RSA.class, new IllegalArgumentException("Null arguments are not allowed!"));
		
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
	private static long[] decryptLongArray(long[] encrypted, Key privateKey) {
		if(encrypted == null || privateKey == null) L.f(RSA.class, new IllegalArgumentException("Null arguments are not allowed!"));
		
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
