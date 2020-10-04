package com.stamacoding.rsaApp.network.global.service.executor.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.service.executor.ExecutorService;

public abstract class DatabaseService extends ExecutorService{
	private volatile Connection connection;
	private final DatabaseConfiguration configuration;
	
	protected DatabaseService(DatabaseConfiguration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(!connect()) setServiceCrashed(true);
		L.t(getServiceClass(), "Initializing...");
		initialize();
		L.d(getServiceClass(), "Initialized!");
	}
	
	protected abstract void initialize();

	private final boolean connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			setConnection(DriverManager.getConnection(
					getConfiguration().getURL(), 
					getConfiguration().getUserName(), 
					getConfiguration().getPassword()));
			
			if (getConnection() != null) {
				L.d(getServiceClass(), "Connected to database");
				return true;
			}
		} catch (SQLException | ClassNotFoundException e) {
			L.e(getServiceClass(), "Failed to connect to database", e);
		}
		return false;
	}

	protected Connection getConnection() {
		return connection;
	}

	private void setConnection(Connection connection) {
		this.connection = connection;
	}

	private DatabaseConfiguration getConfiguration() {
		return configuration;
	}

	public boolean isConnected() {
		try {
			return connection != null && !getConnection().isClosed();
		} catch (SQLException e) {
			return false;
		}
	}
	
	protected void validateConnection() {
		if(!isConnected()) {
			L.f(this.getClass(), new IllegalStateException("Cannot access database. You aren't connected!"));
		}
	}
}
