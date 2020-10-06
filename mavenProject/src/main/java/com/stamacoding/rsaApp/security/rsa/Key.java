package com.stamacoding.rsaApp.security.rsa;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Base64;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.TextUtils;

/**
 * object represents a key, depending on the incoming values for exponent and module
 */
public class Key implements Serializable {
	public enum Type{
		PRIVATE, PUBLIC
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 6419692290127189035L;
	
	private BigInteger exp;
	private BigInteger mod;
	private final int bitCount = RSA.DEFAULT_BIT_COUNT;
	private Type keyType;
	
	public Key(String keyAsString, Type keyType) {
		parse(keyAsString);
		this.keyType = keyType;
	}
	
	public Key(BigInteger exp, BigInteger mod, Type keyType) {
		if(exp == null) L.f(getClass(), new IllegalArgumentException("e/d is not allowed to be null!"));
		if(mod == null) L.f(getClass(), new IllegalArgumentException("n is not allowed to be null!"));
		if(keyType == null) L.f(getClass(), new IllegalArgumentException("Key type is not allowed to be null!"));
		this.exp = exp;
		this.mod = mod;
		this.keyType = keyType;
	}
	
	public Type getKeyType() {
		return keyType;
	}

	public BigInteger getExp() {
		return exp;
	}
	
	public BigInteger getMod() {
		return mod;
	}
	
	public int getBitCount() {
		return bitCount;
	}

	public String stringValue() {
		return Base64.getEncoder().encodeToString(getExp().toByteArray()) + "§" + Base64.getEncoder().encodeToString(getMod().toByteArray());
	}
	
	private void parse(String s) {
		int border = s.lastIndexOf('§');
		
		byte[] exp = Base64.getDecoder().decode(s.substring(0, border));
		byte[] mod = Base64.getDecoder().decode(s.substring(border+1));
		
		this.exp = new BigInteger(exp);
		this.mod = new BigInteger(mod);
	}
	
	public String toString() {
		int width = 80;
		StringBuilder sb = new StringBuilder();
		sb.append(TextUtils.heading("RSA " + getKeyType().toString() +  " KEY " + getBitCount() + " BIT", '#', width, true));
		String string = stringValue();
		
		double estimatedLineCount = (double) string.length() / width;
		double gap = (estimatedLineCount-(int)estimatedLineCount);
		int lines = gap > 0 ? (int) (estimatedLineCount + 1) : (int) estimatedLineCount;
		
		int start = 0;
		for(int i=0; i<lines; i++) {
			int end = start + width;
			if(end > string.length()) {
				sb.append(string.substring(start, string.length()));
			}else {
				sb.append(string.substring(start, end));
			}
			start += width;
			sb.append('\n');
		}
		sb.append(TextUtils.heading("KEY END", '#', width, true));
		return sb.toString();
	}
}
