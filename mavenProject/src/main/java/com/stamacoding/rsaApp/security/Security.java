package com.stamacoding.rsaApp.security;

import java.io.Serializable;

import org.mindrot.jbcrypt.BCrypt;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.security.rsa.Convert;
import com.stamacoding.rsaApp.security.rsa.Key;
import com.stamacoding.rsaApp.security.rsa.RSA;

public class Security {
	public static final int SALT_LENGTH = generatePasswordSalt().length();
	public static final int HASHED_PW_LENGTH = hashPassword("justATest", generatePasswordSalt()).length();
	
	public static byte[] encryptF(Object o) {
		return Convert.serialize(o);
	}
	public static Object decryptF(byte[] encodedByteArray) {
		return Convert.deserialize(encodedByteArray);
	}
	
	public static byte[] encrypt(Object o, Key publicKey) {
		if(o == null || publicKey == null) Logger.error(Security.class.getSimpleName(), new IllegalArgumentException("Null arguments are not allowed."));
		if(!(o instanceof Serializable)) Logger.error(Security.class.getSimpleName(), new IllegalArgumentException("Object is not serializable."));
		
		// Object o => byte[] (Serialisierung)
		byte[] decodedSerializedObject = Convert.serialize(o);
		
		// byte[] => long[]
		long[] decodedLongArray = Convert.byteArrayToLongArray(decodedSerializedObject);
		
		// verschlüssele long[]
		long[] encodedLongArray = RSA.encrypt(decodedLongArray, publicKey);
		
		// long[] => byte[] (Serialisierung)
		byte[] encodedByteArray = Convert.serialize(encodedLongArray);
		
		return encodedByteArray;
	}
	
	public static Object decrypt(byte[] encodedByteArray, Key privateKey) {
		if(encodedByteArray == null || privateKey == null) Logger.error(Security.class.getSimpleName(), new IllegalArgumentException("Null arguments are not allowed."));
		
		// byte[] => long[] (Deserialisierung)
		long[] encodedLongArray = (long[]) Convert.deserialize(encodedByteArray);
		
		// entschlüssele long[]
		long[] decodedLongArray = RSA.decrypt(encodedLongArray, privateKey);
		
		// long[] => byte[]
		byte[] decodedByteArray = Convert.longArrayToByteArray(decodedLongArray);
		
		// byte[] => Object o (Deserialisierung)
		Object o = Convert.deserialize(decodedByteArray);
		
		return o;
	}

	public static String hashPassword(String password, String salt) {
		return BCrypt.hashpw(password, salt);
	}
	
	public static String generatePasswordSalt() {
		return BCrypt.gensalt();
	}
	
	public static boolean checkPassword(String clearPassword, String hashedPassword) {
		return BCrypt.checkpw(clearPassword, hashedPassword);
	}

}
