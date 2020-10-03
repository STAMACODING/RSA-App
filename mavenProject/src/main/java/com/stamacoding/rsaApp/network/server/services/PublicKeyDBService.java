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

public class PublicKeyDBService extends DatabaseService{	

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

	@Override
	protected void initialize() {
		try {
			Statement statement = getConnection().createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS publicKeys ("
					+ "userId INTEGER PRIMARY KEY CHECK (userId > 0), "
					+ "exponent INT NOT NULL CHECK (exponent>1),"
					+ "mod INT NOT NULL CHECK (mod>1) )"); // check max length 
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to initialize database! (SQL Exception)");
			e.printStackTrace();
		}
		Logger.debug(this.getServiceName(), "Logging user database...\n" + this.toString());
	}	
	
	public static void main(String[] args) {
		PublicKeyDBService.getInstance().launch();

	}
	
	/**
	 * stores new public Key for not stored userId
	 * @param uID
	 * @param pubKey
	 * @return whether Key and userId were stored successfully
	 */
	public boolean storePublicKey(long uID, int exponent, int mod) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot store Key! You aren't connected to the user database");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("Insert into publicKeys "
					+ "(userId, exponent, mod)" 
					+ "Values(?, ?, ?)");
			pst.setString(1, Long.toString(uID));
			pst.setString(2, String.valueOf( exponent));
			pst.setString(3, String.valueOf( mod));
		
			pst.executeUpdate();
			
			Logger.debug(this.getClass().getSimpleName(), "Store key for userId: " + Long.toString(uID));
			
			pst.close();
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to store key (SQL exception)");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * updates the Key for a certain userId
	 * !!! this should happen very rarely and only if the private key changed
	 * @param uID
	 * @param pubKey
	 * @return whether the update was successful
	 */
	public boolean updatePublicKey(long uID, int exponent, int mod) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot update Key! You aren't connected to the user database");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("Update publicKeys  Set"
					+ "exponent = ?"
					+ "mod = ?"
					+ "where userId = ?");
			
			
			pst.setString(1,String.valueOf(exponent));
			pst.setString(2, String.valueOf(mod));
			pst.setString(3, Long.toString(uID));
		
			pst.executeUpdate();
			
			Logger.debug(this.getClass().getSimpleName(), "Update key for userId: " + Long.toString(uID));
			
			pst.close();
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to update key (SQL exception)");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * gives the stored public key for a userId
	 * @param userId
	 * @return the long of the public Key
	 */
	public Key getPublicKey(long userId) {
		
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot get Key! You aren't connected to the user database");
			return null;
		}
		
		
		try {
			PreparedStatement stm = getConnection().prepareStatement("SELECT exponent, mod from publicKeys "
					+ "where userId = ?");
			
			stm.setString(1, Long.toString(userId));
		
			ResultSet res = stm.executeQuery();
			
			Logger.debug(this.getClass().getSimpleName(), "Update key for userId: " + Long.toString(userId));
			
			stm.close();
			
			if(res != null && res.next()) {
				
				int exponent = res.getInt(1);
				int mod = res.getInt(2);
				
				Key publicKey = new Key(exponent, mod);
				
				Logger.debug(this.getClass().getSimpleName(), "Returns Public Key" + publicKey.toString()  + " for userId: " + Long.toString(userId) + "" );
				return publicKey;
			}else {
				Logger.warning(this.getClass().getSimpleName(), "Didn't find any key for userID \"" + Long.toString(userId) + "\"");
				return null;
			}
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to get key from DB(SQL exception)");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * deletes key for userId
	 * !!! this should almost never be used!!
	 * 
	 * @param userId
	 * @return
	 */
	public boolean deltePublicKey(long userId) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot delete Key! You aren't connected to the user database");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("Delete from publicKeys"
					+ " where userId = ?");
					
			pst.setString(1,Long.toString(userId));
			
			pst.executeUpdate();
			
			pst.close();
			
			Logger.debug(this.getClass().getSimpleName(), "Delted key for userId: " + Long.toString(userId));
			
			return true;
		}catch(SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to delete key (SQL exception)");
			e.printStackTrace();
		}
		
		return false;
	}

}
