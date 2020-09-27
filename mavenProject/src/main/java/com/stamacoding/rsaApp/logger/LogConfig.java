package com.stamacoding.rsaApp.logger;

import com.stamacoding.rsaApp.logger.file.FileMode;

public class LogConfig {
	private Level fileLogLevel = Level.INFO; 
	private Level consoleLogLevel = Level.DEBUG_HIGH; 
	private FileMode fileMode = FileMode.SINGLE;
	private long maxFileSize = 2 * 1024 * 1024;
	
	public void setup(Level fileLogLevel, Level consoleLogLevel, FileMode fileMode, long maxFileSize) {
		setFileLogLevel(fileLogLevel);
		setConsoleLogLevel(consoleLogLevel);
		setFileMode(fileMode);
		setMaxFileSize(maxFileSize);
	}
	
	public Level getFileLogLevel() {
		return fileLogLevel;
	}
	public void setFileLogLevel(Level fileLogLevel) {
		if(fileLogLevel == null) throw new IllegalArgumentException("The file log level cannot be null!");
		
		this.fileLogLevel = fileLogLevel;
	}
	public Level getConsoleLogLevel() {
		return consoleLogLevel;
	}
	public void setConsoleLogLevel(Level consoleLogLevel) {
		if(consoleLogLevel == null) throw new IllegalArgumentException("The console log level cannot be null!");
		
		this.consoleLogLevel = consoleLogLevel;
	}
	public FileMode getFileMode() {
		return fileMode;
	}
	public void setFileMode(FileMode fileMode) {
		if(fileMode == null) throw new IllegalArgumentException("The file mode cannot be null!");
		
		this.fileMode = fileMode;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		if(maxFileSize <= 0) throw new IllegalArgumentException("The max file size cannot be smaller than 1 byte!");
		
		this.maxFileSize = maxFileSize;
	}
}
