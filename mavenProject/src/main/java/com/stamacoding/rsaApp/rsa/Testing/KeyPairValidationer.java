package com.stamacoding.rsaApp.rsa.Testing;

import com.stamacoding.rsaApp.rsa.keyCreate.KeyPair;

public class KeyPairValidationer {
	public static boolean validate(KeyPair kp) {
		int messageMaximumLength = 50;
		int messageMinimumLength = 25;
		int randomSize = (int) (Math.random()*(messageMaximumLength-messageMinimumLength) + messageMinimumLength);
		
		long[] testArray = new long[randomSize];
		for(int j = 0; j<randomSize; j++) testArray[j] = (long) (Math.random()*1000L);
		
		long[] newArray = TestEncryption.decrypt(TestEncryption.encrypt(testArray, kp.getPublicKey()), kp.getPrivateKey());
		for(int j = 0; j<randomSize; j++) {
			if(newArray[j] != testArray[j]) return false;
		}
		return true;
	}
	
	public static void validateRandomKeyPairs(int testKeyPairCount) {
		int errors = 0;
		for(int i=0; i<testKeyPairCount; i++) {
			KeyPair kp = new KeyPair();
			if(!validate(kp)) errors++;
		}
		System.out.println("-------------------------------------------------------------");
		System.out.println("Key pair vailidation test");
		System.out.println("-------------------------------------------------------------");
		System.out.printf("%-30s %s\n", "Key pairs tested:", testKeyPairCount);
		System.out.printf("%-30s %s\n", "Valid key pairs:", testKeyPairCount-errors);
		System.out.printf("%-30s %s\n", "Invalid key pairs:", errors);
		System.out.println("-------------------------------------------------------------");
	}
}
