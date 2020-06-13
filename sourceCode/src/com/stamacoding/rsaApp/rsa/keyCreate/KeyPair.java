package com.stamacoding.rsaApp.rsa.keyCreate;

public class KeyPair {
	private Key privateKey;
	private Key publicKey;
	
	public KeyPair() {
		// Variables
		int p, q, n, phi, e, d;
		
		// 1
		p = KeyUtils.primeNumb(256);
		q = KeyUtils.primeNumb(256, p);
		
		// 2
		n = p * q;
		phi = (p-1)*(q-1);
		
		// 3
		e = KeyUtils.primeNumb(phi);
		
		// 4
		d = KeyUtils.modularInverse(phi, e);
		
		Key privateKey = new Key(d, n);
		Key publicKey = new Key(e, n);
		setPrivateKey(privateKey);
		setPublicKey(publicKey);
	}

	public Key getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(Key privateKey) {
		this.privateKey = privateKey;
	}
	public Key getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(Key publicKey) {
		this.publicKey = publicKey;
	}
}
