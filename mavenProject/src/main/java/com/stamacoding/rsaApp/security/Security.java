package com.stamacoding.rsaApp.security;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import org.mindrot.jbcrypt.BCrypt;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.security.rsa.Convert;
import com.stamacoding.rsaApp.security.rsa.Key;
import com.stamacoding.rsaApp.security.rsa.RSA;

/**
 * Class offering several security methods.
 */
public class Security {
	public static final int SALT_LENGTH = generatePasswordSalt().length();
	
	/**
	 * Fake encrypts an object by serializing it.
	 * @param o the object to encrypt
	 * @return the serialized object
	 */
	public static byte[] encryptF(Object o) {
		L.t("RSA", Security.class, "Fake encrypting object without any key");
		return Convert.serialize(o);
	}
	
	/**
	 * Fake decrypts an object by deserializing it.
	 * @param o the object to decrypt
	 * @return the deserialized object
	 */
	public static Object decryptF(byte[] encodedByteArray) {
		L.t("RSA", Security.class, "Fake decrypting object without any key");
		return Convert.deserialize(encodedByteArray);
	}
	
	/**
	 * Encrypts an object using the RSA algorithm.
	 * @param o the object to encrypt
	 * @param publicKey the key to use
	 * @return the encrypted object
	 */
	public static byte[] encrypt(Object o, Key publicKey) {
		if(o == null) L.f("RSA", Security.class, new IllegalArgumentException("Cannot serialize null object!"));
		if(publicKey == null) L.f("RSA", Security.class, new IllegalArgumentException("Cannot encrypt an object with a null key!"));
		if(!(o instanceof Serializable)) L.f("RSA", Security.class, new IllegalArgumentException("Object is not serializable!"));
		

		L.t("RSA", Security.class, "Encrypting object with public key: " + publicKey.toString());
		
		L.t("RSA", Security.class, "Serializing object (object => byte[])");
		byte[] decodedSerializedObject = Convert.serialize(o);
		
		L.t("RSA", Security.class, "Casting byte array to long array (byte[] => long[])");
		long[] decodedLongArray = Convert.byteArrayToLongArray(decodedSerializedObject);
		
		L.t("RSA", Security.class, "Encrypting long array (long[] => long[])");
		long[] encodedLongArray = RSA.encrypt(decodedLongArray, publicKey);
		
		L.t("RSA", Security.class, "Serializing encrypted long array (long[] => byte[])");
		byte[] encodedByteArray = Convert.serialize(encodedLongArray);
		
		if(encodedByteArray != null) L.t("RSA", Security.class, "Encrypted object with public key: " + publicKey.toString());
		else L.f("RSA", Security.class, "Failed to encrypt object with public key: " + publicKey.toString());
		
		return encodedByteArray;
	}
	
	/**
	 * Decrypts an object using the RSA algorithm.
	 * @param encryptedByteArray the encrypted object
	 * @param privateKey the key to use
	 * @return the decrypted object
	 */
	public static Object decrypt(byte[] encryptedByteArray, Key privateKey) {
		if(encryptedByteArray == null) L.f("RSA", Security.class, new IllegalArgumentException("Cannot decrypt a null byte array!"));
		if(privateKey == null) L.f("RSA", Security.class, new IllegalArgumentException("Cannot decrypt an object with a null key!"));
		
		L.t("RSA", Security.class, "Decrypting object with private key: " + privateKey.toString());
		
		L.t("RSA", Security.class, "Deserializing encrypted byte array (byte[] => long[])");
		long[] encryptedLongArray = (long[]) Convert.deserialize(encryptedByteArray);
		
		L.t("RSA", Security.class, "Decrypting encrypted long array (long[] => long[])");
		long[] decryptedLongArray = RSA.decrypt(encryptedLongArray, privateKey);
		
		L.t("RSA", Security.class, "Casting long array (long[] => byte[])");
		byte[] decodedByteArray = Convert.longArrayToByteArray(decryptedLongArray);
		
		L.t("RSA", Security.class, "Deserializing byte array to object (byte[] => object)");
		Object o = Convert.deserialize(decodedByteArray);
		
		if(o!=null) L.t("RSA", Security.class, "Successfully decrypted object with private key: " + privateKey.toString());
		else L.f("RSA", Security.class, "Failed to decrypt object with private key: " + privateKey.toString());
		
		return o;
	}

	/**
	 * Hashes a password using a salt.
	 * @param password the password
	 * @param salt the salt
	 * @return the hashed password
	 */
	public static byte[] hashPassword(char[] password, String salt) {
		if(password == null) L.f(Security.class, new IllegalArgumentException("Cannot hash a null password!"));
		if(salt == null) L.f(Security.class, new IllegalArgumentException("Cannot hash a password with a null salt!"));
		
		L.t("PASSWORD", Security.class, "Hashing password using salt");
		return BCrypt.hashpw(String.valueOf(password), salt).getBytes(StandardCharsets.UTF_8);
	}
	
	/**
	 * Generates a password salt.
	 * @return the generated password salt
	 */
	public static String generatePasswordSalt() {
		L.t("PASSWORD", Security.class, "Generating password salt");
		return BCrypt.gensalt();
	}
	
	/**
	 * Checks if the clear password matches the hashed one.
	 * @param clearPassword the clear password
	 * @param hashedPassword the hashed password
	 * @return whether the clear password matches the hashed one
	 */
	public static boolean checkPassword(char[] clearPassword, byte[] hashedPassword) {
		L.t("PASSWORD", Security.class, "Checking password");
		return BCrypt.checkpw(String.valueOf(clearPassword), new String(hashedPassword, StandardCharsets.UTF_8));
	}
}
