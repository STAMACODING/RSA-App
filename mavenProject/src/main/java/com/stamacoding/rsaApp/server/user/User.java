package com.stamacoding.rsaApp.server.user;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4715340391545299952L;
	
	
	private long id;
	private String name;
	private String password;
	
	public User(String name, String password) {
		setId(-1);
		setName(name);
		setPassword(password);
	}
	
	public User(long id, String name, String password) {
		setId(id);
		setName(name);
		setPassword(password);
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(long id) {
		this.id = id;
	}
}
