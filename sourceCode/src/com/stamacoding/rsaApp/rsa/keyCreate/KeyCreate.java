package com.stamacoding.rsaApp.rsa.keyCreate;

public class KeyCreate {
	public static void main(String[] args) {
		// Variables
		int p = 0, q = 0;
		int n = 0;
		int phi = 0;
		int e = 0;
		int modInv = 0;
		
		// 1
		p = primeNumb(256);
		q = primeNumb(256, p);
		
		// 2
		n = p * q;
		phi = (p-1)*(q-1);
		
		// 3
		e = primeNumb(phi);
		
		// 4
		modInv = modularInverse(phi, e);
		
		// 5
		KeyPair keys = new KeyPair();
	}

	private static int primeNumb(int i, int p) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int modularInverse(int phi, int e) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int primeNumb(int i) {
		// TODO Auto-generated method stub
		return 0;
	}
}

/*
 * 1 - create 2 random prime numbers 
 * 2 - calculate n=p*q and phi(n)=(p-1)*(q-1)
 * 3 - creating prime number e
 * 4 - calculate modular inverse of phi(n) and e
 * 5 - set keys to: private(d,n) and public(e,n)
*/
