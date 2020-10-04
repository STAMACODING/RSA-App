package com.stamacoding.rsaApp.security.passwordHashing;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.stamacoding.rsaApp.logger.L;

public class PasswordHasher {
	public final static int SALT_LENGTH = 16;
	public final static int HASHING_STRENGTH = 65535;
	
	public static byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH];
		random.nextBytes(salt);
		return salt;
	}
	
	public static byte[] hashPassword(char[] password, byte[] salt) {
		KeySpec spec = new PBEKeySpec(password, salt, HASHING_STRENGTH, 128);
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return factory.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			L.f("Hashing", PasswordHasher.class, "Failed to hash password!", e);
		}
		return null;
	}
	
	public static boolean checkPassword(char[] password, byte[] hashedPassword, byte[] salt) {
		byte[] hashedPasswordToCheck = hashPassword(password, salt);
		return Arrays.equals(hashedPassword, hashedPasswordToCheck);
	}
}
