package com.stamacoding.rsaApp.server.session;

public class Session {
	private long id;
	private int userId;
	
	public Session(long id, int userId) {
		setId(userId);
		setUserId(userId);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
