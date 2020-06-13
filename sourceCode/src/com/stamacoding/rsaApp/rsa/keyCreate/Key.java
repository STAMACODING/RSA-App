package com.stamacoding.rsaApp.rsa.keyCreate;

public class Key {
	private int exp = 0;
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
}
