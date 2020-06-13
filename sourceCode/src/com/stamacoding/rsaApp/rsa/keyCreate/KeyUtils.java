package com.stamacoding.rsaApp.rsa.keyCreate;

import java.util.ArrayList;

public class KeyUtils {
	
	private static boolean isPrime(int i) {
	    for(int j = 2; j<i; j++) {
	    	if(i%j == 0)
	    		return false;
	    }
	    return true;
	}

	public static int primeNumb(int max, int excludedNumber) {
		ArrayList<Integer> primes = new ArrayList<>();
		for(int j = 2; j <= max; j++) {
		    if(isPrime(j) && j!=excludedNumber) primes.add(j);
		}
		int randomIndex = (int) (Math.random()*primes.size());
		return primes.get(randomIndex);
	}

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

	public static int primeNumb(int max) {
		return primeNumb(max, -1);
	}
}
