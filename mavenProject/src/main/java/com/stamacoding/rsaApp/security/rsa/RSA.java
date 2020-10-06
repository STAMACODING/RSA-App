package com.stamacoding.rsaApp.security.rsa;

import java.math.BigInteger;

import com.stamacoding.rsaApp.logger.L;

/**
 * Class containing methods to encrypt/decrypt long arrays using the RSA algorithm.
 */
public class RSA {
	public static final int DEFAULT_BIT_COUNT = 1024;

	/**
	 * Fake encrypts an object by serializing it.
	 * @param o the object to encrypt
	 * @return the serialized object
	 */
	public static byte[] encryptF(Object o) {
		L.t(RSA.class, "Fake encrypting object without any key");
		return Serialization.serialize(o);
	}
	
	/**
	 * Fake decrypts an object by deserializing it.
	 * @param o the object to decrypt
	 * @return the deserialized object
	 */
	public static Object decryptF(byte[] encodedByteArray) {
		L.t(RSA.class, "Fake decrypting object without any key");
		return Serialization.deserialize(encodedByteArray);
	}
	
	public static BigInteger encryptBigInt(BigInteger i, Key publicKey) {
		return i.modPow(publicKey.getExp(), publicKey.getMod());
	}
	public static BigInteger decryptBigInt(BigInteger i, Key privateKey) {
		return i.modPow(privateKey.getExp(), privateKey.getMod());
	}
	
	public static BigInteger encryptLong(long l, Key publicKey) {
		return new BigInteger(Long.toUnsignedString(l)).modPow(publicKey.getExp(), publicKey.getMod());
	}
	public static long decryptLong(BigInteger i, Key privateKey) {
		return i.modPow(privateKey.getExp(), privateKey.getMod()).longValue();
	}
	
	public static BigInteger encryptInt(int i, Key publicKey) {
		return BigInteger.valueOf(Integer.toUnsignedLong(i)).modPow(publicKey.getExp(), publicKey.getMod());
	}
	public static int decryptInt(BigInteger i, Key privateKey) {
		return i.modPow(privateKey.getExp(), privateKey.getMod()).intValue();
	}
	
	public static BigInteger encryptByte(byte b, Key publicKey) {
		return BigInteger.valueOf(Byte.toUnsignedInt(b)).modPow(publicKey.getExp(), publicKey.getMod());
	}
	public static byte decryptByte(BigInteger i, Key privateKey) {
		return i.modPow(privateKey.getExp(), privateKey.getMod()).byteValue();
	}
	
	public static byte[] encryptBytes(byte[] bytes, Key publicKey) {
		BigInteger[] encrypted = new BigInteger[bytes.length];
		for(int i=0; i<bytes.length; i++) {
			encrypted[i] = encryptByte(bytes[i], publicKey);
		}
		return Serialization.serialize(encrypted);
	}
	
	public static byte[] decryptBytes(byte[] bytes, Key privateKey) {
		BigInteger[] encrypted = (BigInteger[]) Serialization.deserialize(bytes);
		byte[] decrypted = new byte[encrypted.length];
		for(int i=0; i<encrypted.length; i++) {
			decrypted[i] = decryptByte(encrypted[i], privateKey);
		}
		return decrypted;
	}
	
	public static byte[] encrypt(Object o, Key publicKey) {
		byte[] bytes = Serialization.serialize(o);
		return encryptBytes(bytes, publicKey);
	}
	
	public static Object decrypt(byte[] bytes, Key privateKey) {
		byte[] decryptedBytes = decryptBytes(bytes, privateKey);
		return Serialization.deserialize(decryptedBytes);
	}
}
