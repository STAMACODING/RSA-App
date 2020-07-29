package com.stamacoding.rsaApp.rsa;

import java.io.Serializable;

import com.stamacoding.rsaApp.log.logger.Logger;

public class RSA {
	public static byte[] encryptF(Object o) {
		return Convert.serialize(o);
	}
	public static Object decryptF(byte[] encodedByteArray) {
		return Convert.deserialize(encodedByteArray);
	}
	
	public static byte[] encrypt(Object o, Key publicKey) {
		if(o == null || publicKey == null) Logger.error(RSA.class.getSimpleName(), new IllegalArgumentException("Null arguments are not allowed."));
		if(!(o instanceof Serializable)) Logger.error(RSA.class.getSimpleName(), new IllegalArgumentException("Object is not serializable."));
		
		// Object o => byte[] (Serialisierung)
		byte[] decodedSerializedObject = Convert.serialize(o);
		
		// byte[] => long[]
		long[] decodedLongArray = Convert.byteArrayToLongArray(decodedSerializedObject);
		
		// verschlüssele long[]
		long[] encodedLongArray = EnDeCrypt.encode(decodedLongArray, publicKey);
		
		// long[] => byte[] (Serialisierung)
		byte[] encodedByteArray = Convert.serialize(encodedLongArray);
		
		return encodedByteArray;
	}
	
	public static Object decrypt(byte[] encodedByteArray, Key privateKey) {
		if(encodedByteArray == null || privateKey == null) Logger.error(RSA.class.getSimpleName(), new IllegalArgumentException("Null arguments are not allowed."));
		
		// byte[] => long[] (Deserialisierung)
		long[] encodedLongArray = (long[]) Convert.deserialize(encodedByteArray);
		
		// entschlüssele long[]
		long[] decodedLongArray = EnDeCrypt.decode(encodedLongArray, privateKey);
		
		// long[] => byte[]
		byte[] decodedByteArray = Convert.longArrayToByteArray(decodedLongArray);
		
		// byte[] => Object o (Deserialisierung)
		Object o = Convert.deserialize(decodedByteArray);
		
		return o;
	}
}
