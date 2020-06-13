package com.stamacoding.rsaApp.rsa.keyCreate;

public class KeyUtils {

	public static int primeNumb(int i, int p) {
		// TODO Auto-generated method stub
		return 0;
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

	public static int primeNumb(int i) {
		// TODO Auto-generated method stub
		return 0;
	}
}