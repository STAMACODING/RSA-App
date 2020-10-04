package com.stamacoding.rsaApp.network.global.user;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.TextUtils;
import com.stamacoding.rsaApp.security.passwordHashing.PasswordHasher;

public class Password implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8095912014936434287L;
	
	
	private byte[] salt = null;
	private char[] clearPassword = null;
	private byte[] hashedPassword = null;
	
	public Password(char[] clearPassword) {
		setClearPassword(clearPassword);
	}
	
	public Password(char[] clearPassword, byte[] salt) {
		setClearPassword(clearPassword);
		setSalt(salt);
	}
	
	public Password(byte[] hashedPassword, byte[] salt) {
		setHashedPassword(hashedPassword);
		setSaltWithoutAutoHashing(salt);
	}
	
	public Password(String hashedPassword, String salt) {
		setHashedPassword(Base64.getDecoder().decode(hashedPassword));
		setSaltWithoutAutoHashing(Base64.getDecoder().decode(salt));
	}

	public final byte[] getSalt() {
		return salt;
	}
	public final String getSaltAsString() {
		if(salt == null) return null;
		return Base64.getEncoder().encodeToString(salt);
	}

	public final void setSalt(byte[] salt) {
		this.salt = salt;
		if(getSalt() == null) setHashedPassword(null);
		else setHashedPassword(PasswordHasher.hashPassword(getClearPassword(), getSalt()));
	}
	
	private final void setSaltWithoutAutoHashing(byte[] salt) {
		this.salt = salt;
		if(getSalt() == null) setHashedPassword(null);;
	}

	public final char[] getClearPassword() {
		return clearPassword;
	}

	public final void setClearPassword(char[] clearPassword) {
		if(clearPassword == null || clearPassword.length == 0 || clearPassword.length > 30) {
			L.f(this.getClass(), new IllegalArgumentException("Password cannot be null and it's length should be between 1 and 30!"));
		}
		this.clearPassword = clearPassword;
		if(getSalt() != null) {
			setHashedPassword(PasswordHasher.hashPassword(getClearPassword(), getSalt()));
		}
	}

	public final byte[] getHashedPassword() {
		return hashedPassword;
	}
	public final String getHashedPasswordAsString() {
		if(hashedPassword == null) return null;
		return Base64.getEncoder().encodeToString(hashedPassword);
	}

	private final void setHashedPassword(byte[] hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public final boolean isHashed() {
		return getSalt() != null;
	}
	
	public final boolean check(char[] clearPassword) {
		if(getSalt() == null) L.f(this.getClass(), new IllegalStateException("Cannot check password. Salt is null!"));
		
		return PasswordHasher.checkPassword(clearPassword, getHashedPassword(), getSalt());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(clearPassword);
		result = prime * result + Arrays.hashCode(hashedPassword);
		result = prime * result + Arrays.hashCode(salt);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Password other = (Password) obj;
		if (!Arrays.equals(clearPassword, other.clearPassword))
			return false;
		if (!Arrays.equals(hashedPassword, other.hashedPassword))
			return false;
		if (!Arrays.equals(salt, other.salt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		if(getClearPassword() != null) {
			sb.append('(');
			sb.append(TextUtils.setLength(String.valueOf(getClearPassword()), 12));
			if(getSalt() != null) {
				sb.append(", ");
				sb.append(TextUtils.setLength(getSaltAsString(), 10));
			}
			sb.append(") § ");
		}
		if(getHashedPassword() != null) {
			sb.append(TextUtils.setLength(getHashedPasswordAsString(), 20));
		}
		sb.append(" ]");
		return sb.toString();
	}
}
