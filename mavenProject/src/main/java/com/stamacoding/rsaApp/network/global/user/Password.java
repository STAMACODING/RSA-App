package com.stamacoding.rsaApp.network.global.user;

import java.io.Serializable;
import java.util.Arrays;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.security.Security;
import com.stamacoding.rsaApp.security.rsa.Convert;

public class Password implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8095912014936434287L;
	
	
	private String salt = null;
	private char[] clearPassword = null;
	private byte[] hashedPassword = null;
	
	public Password(char[] clearPassword) {
		setClearPassword(clearPassword);
	}
	
	public Password(char[] clearPassword, String salt) {
		setClearPassword(clearPassword);
		setSalt(salt, false);
	}
	
	public Password(byte[] hashedPassword, String salt) {
		setHashedPassword(hashedPassword);
		setSalt(salt, true);
	}

	public final String getSalt() {
		return salt;
	}

	public final void setSalt(String salt, boolean isAlreadyHashed) {
		this.salt = salt;
		if(getSalt() == null) setHashedPassword(null);
		else if(!isAlreadyHashed) setHashedPassword(Security.hashPassword(getClearPassword(), getSalt()));
	}

	public final char[] getClearPassword() {
		return clearPassword;
	}

	private final void setClearPassword(char[] clearPassword) {
		if(clearPassword == null || clearPassword.length == 0 || clearPassword.length > 30) {
			L.f(this.getClass(), new IllegalArgumentException("Password cannot be null and it's length should be between 1 and 30!"));
		}
		this.clearPassword = clearPassword;
	}

	public final byte[] getHashedPassword() {
		return hashedPassword;
	}

	private final void setHashedPassword(byte[] hashedPassword) {
		if(hashedPassword == null) L.f(this.getClass(), new IllegalArgumentException("HashedPassword is not allowed to get set to null!"));
		this.hashedPassword = hashedPassword;
	}

	public final boolean isHashed() {
		return getSalt() != null;
	}
	
	public final boolean check(char[] clearPassword) {
		if(getHashedPassword() == null) L.f(this.getClass(), new IllegalStateException("Cannot check password. Password is not hashed!"));
		
		return Security.checkPassword(clearPassword, getHashedPassword());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(clearPassword);
		result = prime * result + Arrays.hashCode(hashedPassword);
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Password other = (Password) obj;
		if (!Arrays.equals(clearPassword, other.clearPassword))
			return false;
		if (!Arrays.equals(hashedPassword, other.hashedPassword))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		if(isHashed()) {
			sb.append("@#0##?#1#!#1###~");
		}else {
			sb.append("****************");
		}
		sb.append('}');
		return sb.toString();
	}
	
	public final byte[] getStorablePassword() {
		return Convert.serialize(getHashedPassword());
	}
	public final byte[] getStorableSalt() {
		return Convert.serialize(getSalt());
	}

	public static void main(String[] args) {
		Password pw = new Password("dsfsfHenri".toCharArray(), Security.generatePasswordSalt());
		System.out.println(pw.toString());
	}
}
