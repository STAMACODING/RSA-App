package com.stamacoding.rsaApp.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import com.stamacoding.rsaApp.log.logger.Logger;

public class LogEntry {
	private long t;
	private Level l;
	private Class<?> c;
	private String m;
	private Exception e;
	
	LogEntry(long t, Level l, Class<?> c, String m, Exception e){
		setTime(t);
		setLevel(l);
		setClassOfOrigin(c);
		setMessage(m);
		setException(e);
	}
	
	public void setTime(long t) {
		this.t = t;
	}
	
	public long getTime() {
		return t;
	}
	
	public Level getLevel() {
		return l;
	}
	
	public void setLevel(Level l) {
		this.l = l;
	}
	
	public Class<?> getClassOfOrigin() {
		return c;
	}
	
	public void setClassOfOrigin(Class<?> c) {
		this.c = c;
	}
	
	public String getMessage() {
		return m;
	}
	
	public void setMessage(String m) {
		this.m = m;
	}
	
	public Exception getException() {
		return e;
	}
	
	public void setException(Exception e) {
		this.e = e;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(new SimpleDateFormat("[ dd.MM.yyyy | HH:mm:ss.SSS | X ][ ").format(getTime()));
		sb.append(getClassOfOrigin().getSimpleName());
		
		String level = "";
		switch(getLevel()) {
		case DEBUG_HIGH:
			level = "DEBUG_H";
			break;
		case DEBUG_LOW:
			level = "DEBUG_L";
			break;
		case DEBUG_MEDIUM:
			level = "DEBUG_M";
			break;
		case ERROR:
			level = "ERROR";
			break;
		case FATAL:
			level = "FATAL";
			break;
		case INFO:
			level = "INFO";
			break;
		case WARNING:
			level = "WARNING";
			break;
		}
		sb.append(String.format(" ][ %-5s ]: ", level));
		sb.append(getMessage());
		sb.append("\n");
		
		return sb.toString();
	}
	
	public String getStackTrace() {
	    final StringWriter sw = new StringWriter();
	    final PrintWriter pw = new PrintWriter(sw, true);
	    getException().printStackTrace(pw);
	    return sw.getBuffer().toString();
	}
}
