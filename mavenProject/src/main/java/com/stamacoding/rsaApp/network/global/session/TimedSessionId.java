package com.stamacoding.rsaApp.network.global.session;

public class TimedSessionId {
	private String sessionId;
	private long time;
	
	public TimedSessionId(String sessionId, long time) {
		setSessionId(sessionId);
		setTime(time);
	}
	
	public String getSessionId() {
		return sessionId;
	}
	private void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	public long getInactivityTime() {
		return System.currentTimeMillis() - getTime();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
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
		TimedSessionId other = (TimedSessionId) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}

}
