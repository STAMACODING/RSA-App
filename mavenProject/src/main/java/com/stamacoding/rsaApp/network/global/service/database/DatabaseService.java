package com.stamacoding.rsaApp.network.global.service.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.network.global.service.Service;

public abstract class DatabaseService extends Service{
	private volatile Connection connection;
	private final DatabaseConfiguration configuration;
	
	public DatabaseService(String serviceName, DatabaseConfiguration configuration) {
		super(serviceName);
		if(configuration == null) throw new IllegalArgumentException("DatabaseConfiguration configuration is not allowed to be null!");
		this.configuration = configuration;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(!connect()) setServiceCrashed(true);
		initialize();
	}

	protected abstract void initialize();

	private boolean connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			setConnection(DriverManager.getConnection(
					getConfiguration().getURL(), 
					getConfiguration().getUserName(), 
					getConfiguration().getPassword()));
			
			if (getConnection() != null) {
				Logger.debug(this.getClass().getSimpleName(), "Connected to database");
				return true;
			}
		} catch (SQLException | ClassNotFoundException e) {
			Logger.error(this.getServiceName(), "Failed to connect to database");
		}
		return false;
	}

	public Connection getConnection() {
		return connection;
	}

	private void setConnection(Connection connection) {
		this.connection = connection;
	}

	public DatabaseConfiguration getConfiguration() {
		return configuration;
	}

	public boolean isConnected() {
		try {
			return connection != null && !getConnection().isClosed();
		} catch (SQLException e) {
			return false;
		}
	}
}
