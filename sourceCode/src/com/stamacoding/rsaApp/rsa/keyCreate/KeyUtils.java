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

	public static int modularInverse(int phi, int e) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static int primeNumb(int max) {
		return primeNumb(max, -1);
	}
}
