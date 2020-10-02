package com.stamacoding.rsaApp.network.server.services;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.network.client.services.ChatDatabaseService;
import com.stamacoding.rsaApp.network.global.service.database.DatabaseConfiguration;
import com.stamacoding.rsaApp.network.global.service.database.DatabaseService;
import com.stamacoding.rsaApp.network.global.user.Password;
import com.stamacoding.rsaApp.network.global.user.User;
import com.stamacoding.rsaApp.network.server.managers.UserManager;
import com.stamacoding.rsaApp.security.Security;
import com.stamacoding.rsaApp.security.rsa.Key;

public  abstract class PublicKeyDBService extends DatabaseService{	

	/** The only instance of this class */
	private volatile static PublicKeyDBService singleton = new PublicKeyDBService();
		
	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link PublicKeyDBServic#getInstance()} to get the	
	 *  only instance of this class.
	 */
	private PublicKeyDBService() {
		super(PublicKeyDBService.class.getSimpleName(), new DatabaseConfiguration(
				"jdbc:sqlite:ServerDatabase.db",
				"root",
				"root"));
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static PublicKeyDBService getInstance() {
	
		return singleton;
	}
	
	

}
