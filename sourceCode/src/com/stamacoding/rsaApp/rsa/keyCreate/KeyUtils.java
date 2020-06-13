package com.stamacoding.rsaApp.rsa.keyCreate;

import java.util.ArrayList;

/**
 * holds functions like creating a prime number (in a certain area) or returning the modular inverse
 */

public class KeyUtils {
	
	private static boolean isPrime(int i) {
	    for(int j = 2; j<i; j++) {
	    	if(i%j == 0)
	    		return false;
	    }
	    return true;
	}

	/**
	 * creates a prime number in a certain area
	 * @param max maximum value of the area
	 * @param excludedNumber all numbers that will not be accepted as possible prime numbers
	 * @return
	 */
	public static int primeNumb(int max, int excludedNumber) {
		ArrayList<Integer> primes = new ArrayList<>();
		for(int j = 2; j <= max; j++) {
		    if(isPrime(j) && j!=excludedNumber) primes.add(j);
		}
		int randomIndex = (int) (Math.random()*primes.size());
		return primes.get(randomIndex);
	}

	/**
	 * returns the modular inverse in a modulo operation
	 * @param e number which stands in the base
	 * @param m number which stands in the module
	 * @return
	 */
	public static int modularInverse(int e, int m) {
		boolean gefunden = false;
		int d = 1;
		while(d <= m && !gefunden) {
		    if((e*d) % m == 1) {
		        gefunden = true;
		    }else {
		        d += 1;
		    }
		}
		if(d > m) {
		    d = -1;
		}
		return d;
	}

	/**
	 * creates a prime number in a certain area, which reaches from 2 to a maximum value
	 * @param max maximum value of the area
	 * @return
	 */
	public static int primeNumb(int max) {
		return primeNumb(max, -1);
	}
}
