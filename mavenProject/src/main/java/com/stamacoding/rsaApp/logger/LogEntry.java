package com.stamacoding.rsaApp.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

/**
 * An instance of this class represents a single log entry with all its meta information.
 *
 */
public class LogEntry {
	/**
	 * The time the log entry was originally created.
	 */
	private long time;
	
	/**
	 * The entry's log level describes the importance of a log and how it affects the program execution.
	 * @see Level
	 * @see L
	 */
	private Level level;
	
	/**
	 * The entry's class of origin is the class where the log entry was created.
	 */
	private Class<?> classOfOrigin;
	/**
	 * The log entry's message.
	 */
	private String message;
	
	/**
	 * The tag of a log entry is a string that assigns the log entry to a more
	 * abstract context.
	 */
	private String tag;
	
	/**
	 * Only for (fatal) errors: Exception that is related to the (fatal) error.
	 */
	private Exception e;
	
	LogEntry(long t, Level l, String tag, Class<?> c, String m, Exception e){
		setTime(t);
		setLevel(l);
		setClassOfOrigin(c);
		setMessage(m);
		setException(e);
		setTag(tag);
	}
	
	/**
	 * Gets the log entry's tag. The tag of a log entry is a string that assigns the log entry to a more
	 * abstract context.
	 * @return the log entry's tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the log entry's tag. The tag of a log entry is a string that assigns the log entry to a more
	 * abstract context.
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Sets the time the log entry was originally created.
	 * @param t
	 */
	public void setTime(long t) {
		this.time = t;
	}
	
	/**
	 * Gets the time the log entry was originally created.
	 * @return the time the log entry was originally created
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Gets the log entry's log level.
	 * The entry's log level describes the importance of a log and how it affects the program execution.
	 * @return the log entry's log level
	 */
	public Level getLevel() {
		return level;
	}
	
	/**
	 * Sets the log entry's log level.
	 * The entry's log level describes the importance of a log and how it affects the program execution.
	 * @param l the level to set
	 */
	public void setLevel(Level l) {
		this.level = l;
	}
	
	/**
	 * Gets the entry's class of origin. The class of origin is the class where the log entry originally was created.
	 * @return
	 */
	public Class<?> getClassOfOrigin() {
		return classOfOrigin;
	}
	
	/**
	 * Sets the entry's class of origin. The class of origin is the class where the log entry originally was created.
	 * @param c
	 */
	public void setClassOfOrigin(Class<?> c) {
		this.classOfOrigin = c;
	}
	
	/**
	 * Gets the entry's message.
	 * @return the entry's message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the entry's message.
	 * @param m the message to set
	 */
	public void setMessage(String m) {
		this.message = m;
	}
	
	/**
	 * Use only for (fatal) errors: Gets the exception that is related to the (fatal) error.
	 * @return the exception that is related to the (fatal) error
	 */
	public Exception getException() {
		return e;
	}
	
	/**
	 * Use only for (fatal) errors: Sets the exception that is related to the (fatal) error.
	 * @param e the exception to set
	 */
	public void setException(Exception e) {
		if(Level.asInt(getLevel()) < Level.asInt(Level.ERROR) && e != null) {
			throw new LoggerException("It's not possible to assign an exception to a log entry which is not a (fatal) error.");
		}
		this.e = e;
	}
	
	/**
	 * Returns a string representation of the log entry. Is used to write the log entry to a log file or print it to the console. 
	 */
	@Override
	public String toString() {
		return toString(true);
	}

	/**
	 * Returns a string representation of the log entry. Is used to write the log entry to a log file or print it to the console.
	 * 
	 * @param taggingEnables whether the tag feature is enabled 
	 * @see L.Config
	 */
	public String toString(boolean taggingEnabled) {
		StringBuilder sb = new StringBuilder();
		sb.append(new SimpleDateFormat("[ dd.MM.yyyy | HH:mm:ss.SSS | X ][ ").format(getTime()));
		if(taggingEnabled) {
			if(getTag() != null) {
				sb.append(String.format("%-20s : ", getTag()));
			}else {
				sb.append(String.format("%-20s : ", ""));
			}
		}
		sb.append(String.format("%-25s", getClassOfOrigin().getSimpleName()));
		
		String level = "";
		switch(getLevel()) {
		case TRACE:
			level = "TRACE";
			break;
		case DEBUG:
			level = "DEBUG";
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
		sb.append(String.format(" ][ %-7s ]: ", level));
		sb.append(getMessage());
		sb.append("\n");
		
		return sb.toString();
	}
	
	/**
	 * Generates a string representation of the exception's stack trace.
	 * @return a string representation of the exception's stack trace
	 */
	public String getStackTrace() {
	    if(getException() != null) {
	    	final StringWriter sw = new StringWriter();
		    final PrintWriter pw = new PrintWriter(sw, true);
		    getException().printStackTrace(pw);
		    return sw.getBuffer().toString();
	    }else {
	    	return null;
	    }
	}
}
