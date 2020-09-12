package com.stamacoding.rsaApp.network.global.service.database;

public class DatabaseConfiguration {
	/** The URL to the database file. */
	private final String URL;
	
	/** The used user name to interact with the database. */
	private final String userName;
	
	/** The used password to interact with the database. */
	private final String password;
	

	public DatabaseConfiguration(String URL, String userName, String password) {
		if(URL == null) throw new IllegalArgumentException("String URL is not allowed to be null!");
		if(userName == null) throw new IllegalArgumentException("String userName is not allowed to be null!");
		if(password == null) throw new IllegalArgumentException("String password is not allowed to be null!");
		
		this.URL = URL;
		this.userName = userName;
		this.password = password;
	}

	public String getURL() {
		return URL;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
}
