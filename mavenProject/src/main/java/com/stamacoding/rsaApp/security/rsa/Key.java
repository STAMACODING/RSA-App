package com.stamacoding.rsaApp.security.rsa;

import java.io.Serializable;

/**
 * object represents a key, depending on the incoming values for exponent and module
 */
public class Key implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6419692290127189035L;
	/**
	 * holds the exponent in a modulo operation
	 */
	private int exp = 0;
	/**
	 * holds the modulo number in a modulo operation
	 */
	private int mod = 0;
	
	public Key(int exp, int mod) {
		setExp(exp);
		setMod(mod);
	}
	
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getMod() {
		return mod;
	}
	public void setMod(int mod) {
		this.mod = mod;
	}
	
	@Override
	public String toString() {
		return "(" + getExp() + ", " + getMod() + ")";
	}
}
