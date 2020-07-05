package com.stamacoding.rsaApp.rsa.endecrypt;

import com.stamacoding.rsaApp.rsa.keyCreate.Key;

public class EnDecrypt {
	public static long[] encrypt(long[] message, Key publicKey) {
		long[] encryptedMessage = new long[message.length];
		for(int i = 0; i < message.length; i++) {
			encryptedMessage[i] = 1;
			for(int j=0; j<publicKey.getExp(); j++) {
				encryptedMessage[i] = (encryptedMessage[i] * message[i]) % publicKey.getMod();
			}
		}
		return encryptedMessage;
	}
	
	public static long[] decrypt(long[] encryptedMessage, Key privateKey) {
		return encrypt(encryptedMessage, privateKey);
	}
}

