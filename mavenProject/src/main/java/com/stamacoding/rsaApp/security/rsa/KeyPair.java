package com.stamacoding.rsaApp.security.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.security.rsa.Key.Type;

/**
 * Represents a RSA key pair consisting of a public and a fitting private key. Invoking the constructor {@link #KeyPair()} automatically generates the both keys.
 */
public class KeyPair {
	private static SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * The private key (d, n).
	 */
	private Key privateKey;
	/**
	 * The public key (e, n).
	 */
	private Key publicKey;
	
	/**
	 * Generates a RSA key pair.
	 * @see KeyPair
	 */
	public KeyPair() {
		generate();
	}
	
	public KeyPair(Key privateKey, Key publicKey) {
		setPrivateKey(privateKey);
		setPublicKey(publicKey);
	}

	public Key getPrivateKey() {
		return privateKey;
	}
	
	private void setPrivateKey(Key privateKey) {
		if(privateKey == null) L.f("RSA", this.getClass(), new IllegalArgumentException("The private key is not allowed to be null!"));
		
		this.privateKey = privateKey;
	}
	
	public Key getPublicKey() {
		return publicKey;
	}
	
	private void setPublicKey(Key publicKey) {
		if(publicKey == null) L.f("RSA", this.getClass(), new IllegalArgumentException("The public key is not allowed to be null!"));
		
		this.publicKey = publicKey;
	}
	
	private void generate() {
		BigInteger p, q, n, phi, e, d;

		p = BigInteger.probablePrime(RSA.DEFAULT_BIT_COUNT/2, RANDOM);
		do {
			q = BigInteger.probablePrime(RSA.DEFAULT_BIT_COUNT/2, RANDOM);
		}while(p.equals(q));
		
		n = p.multiply(q);
		phi = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));
		
		do e = new BigInteger(phi.bitLength(), RANDOM);
		while (e.compareTo(BigInteger.ONE) <= 0
		    || e.compareTo(phi) >= 0
		    || !e.gcd(phi).equals(BigInteger.ONE));
		d = e.modInverse(phi);
		
		Key privateKey = new Key(d, n, Type.PRIVATE);
		Key publicKey = new Key(e, n, Type.PUBLIC);
		setPrivateKey(privateKey);
		setPublicKey(publicKey);
	}
	
	@Override
	public String toString() {
		return getPublicKey().toString() + getPrivateKey().toString();
	}
}
