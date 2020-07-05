package com.stamacoding.rsaApp.rsa.keyCreate;

import java.util.ArrayList;

/**
 * holds functions like creating a prime number (in a certain area) or returning the modular inverse
 */

public class KeyUtils {
	
	private static boolean isPrime(int n) {
	    if(n < 2) return false;
	    if(n == 2 || n == 3) return true;
	    if(n%2 == 0 || n%3 == 0) return false;
	    long sqrtN = (long)Math.sqrt(n)+1;
	    for(long i = 6L; i <= sqrtN; i += 6) {
	        if(n%(i-1) == 0 || n%(i+1) == 0) return false;
	    }
	    return true;
	}

	/**
	 * creates a prime number in a certain area
	 * @param max maximum value of the area
	 * @param excludedNumber any desired number that will not be accepted as possible prime number
	 * @return
	 */
	public static int primeNumb(int min, int max, int excludedNumber) {
		ArrayList<Integer> primes = new ArrayList<>();
		for(int j = min; j <= max; j++) {
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
	public static int primeNumb(int min, int max) {
		return primeNumb(min, max, -1);
	}
}
