package com.stamacoding.rsaApp.rsa.Testing;

import com.stamacoding.rsaApp.rsa.keyCreate.KeyPair;

public class KeyPairSpeedTest {
	public static void test(int testKeyPairCount) {
		long timeBefore = System.currentTimeMillis();
		for(int i=1; i<=testKeyPairCount; i++) {
			KeyPair kp = new KeyPair();
		}
		long timeAfter = System.currentTimeMillis();
		double diffMilliSeconds = timeAfter - timeBefore;
		double diffSeconds = diffMilliSeconds/1000.0;
		double speed = testKeyPairCount/diffSeconds;
		System.out.println("-------------------------------------------------------------");
		System.out.println("Key speed test");
		System.out.println("-------------------------------------------------------------");
		System.out.printf("%-30s %s\n", "Total key count:", testKeyPairCount*2);
		System.out.printf("%-30s %ss\n", "Elapsed time:", diffSeconds);
		System.out.printf("%-30s %s/s\n", "Key generation speed:", speed*2);
		System.out.println("-------------------------------------------------------------");
		System.out.printf("%-30s %s\n", "Total key pair count:", testKeyPairCount);
		System.out.printf("%-30s %ss\n", "Elapsed time:", diffSeconds);
		System.out.printf("%-30s %s/s\n", "Key pair generation speed:", speed);
		System.out.println("-------------------------------------------------------------");
	}
}
