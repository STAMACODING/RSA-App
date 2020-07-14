package com.stamacoding.rsaApp.rsa;

import com.stamacoding.rsaApp.rsa.convert.Convert;
import com.stamacoding.rsaApp.rsa.endecrypt.EnDeCrypt;
import com.stamacoding.rsaApp.rsa.keyCreate.Key;

public class RSA {
	public static byte[] encrypt(Object o, Key publicKey) {
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
