package com.stamacoding.rsaApp.server.session;

public class Session {
	private long id;
	private SessionState state = SessionState.NONE;
	
	public Session(long id) {
		setId(id);
	}
	
	public Session(long id, SessionState state) {
		setId(id);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

	public SessionState getState() {
		return state;
	}

	public void setState(SessionState state) {
		this.state = state;
	}
}
