package com.stamacoding.rsaApp.network.global.user;

import java.io.Serializable;

import org.mindrot.jbcrypt.BCrypt;

import com.stamacoding.rsaApp.security.Security;

public class Password implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8095912014936434287L;
	
	
	private String salt = null;
	private String clearPassword = null;
	private String hashedPassword = null;
	
	public Password(String clearPassword) {
		setClearPassword(clearPassword);
	}
	
	public Password(String password, String salt, boolean isAlreadyHashed) {
		if(isAlreadyHashed) {
			setHashedPassword(password);
			setSalt(salt, true);
		}else {
			setClearPassword(password);
			setSalt(salt, false);
		}
	}

	public final String getSalt() {
		return salt;
	}

	public final void setSalt(String salt, boolean isAlreadyHashed) {
		this.salt = salt;
		if(getSalt() == null) setHashedPassword(null);
		else if(!isAlreadyHashed) setHashedPassword(Security.hashPassword(getClearPassword(), getSalt()));
	}

	public final String getClearPassword() {
		return clearPassword;
	}

	private final void setClearPassword(String clearPassword) {
		if(clearPassword == null || clearPassword.length() == 0 || clearPassword.length() > 30) {
			throw new IllegalArgumentException("Password cannot be null and it's length should be between 1 and 30!");
		}
		this.clearPassword = clearPassword;
	}

	public final String getHashedPassword() {
		return hashedPassword;
	}

	private final void setHashedPassword(String hashedPassword) {
		if(hashedPassword == null) throw new IllegalArgumentException("HashedPassword is not allowed to get set to null!");
		this.hashedPassword = hashedPassword;
	}

	public final boolean isHashed() {
		return getSalt() != null;
	}
	
	public final boolean check(String clearPassword) {
		if(getHashedPassword() == null) throw new IllegalStateException("Cannot check password. Password is not hashed!");
		else return Security.checkPassword(clearPassword, getHashedPassword());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clearPassword == null) ? 0 : clearPassword.hashCode());
		result = prime * result + ((hashedPassword == null) ? 0 : hashedPassword.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
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
		if (clearPassword == null) {
			if (other.clearPassword != null)
				return false;
		} else if (!clearPassword.equals(other.clearPassword))
			return false;
		if (hashedPassword == null) {
			if (other.hashedPassword != null)
				return false;
		} else if (!hashedPassword.equals(other.hashedPassword))
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
			sb.append(getHashedPassword());
		}else {
			sb.append(getClearPassword());
		}
		sb.append('}');
		return sb.toString();
	}

	public static void main(String[] args) {
		Password pw = new Password("dsfsfHenri", Security.generatePasswordSalt(), false);
		System.out.println(pw.toString());
	}
}
