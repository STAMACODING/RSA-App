package com.stamacoding.rsaApp.network.global.session;

import com.stamacoding.rsaApp.network.global.TextUtils;

public class Session {
	private String id;
	private LoginState state = LoginState.NONE;
	
	public Session(String id) {
		setId(id);
	}
	
	public Session(String sessionID, LoginState state) {
		setId(sessionID);
		setState(state);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public LoginState getState() {
		return state;
	}

	public void setState(LoginState state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		if(getId() != null) {
			sb.append(TextUtils.setLength(getId(), 12));
		}else {
			sb.append("null");
		}
		sb.append(" : ");
		if(getState() != null) {
			sb.append(getState().toString());
		}else {
			sb.append("null");
		}
		sb.append(" ]");
		return sb.toString();
	}
}
