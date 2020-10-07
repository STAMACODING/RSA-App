package com.stamacoding.rsaApp.network.server.service.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.service.executor.database.DatabaseConfiguration;
import com.stamacoding.rsaApp.network.global.service.executor.database.DatabaseService;
import com.stamacoding.rsaApp.security.rsa.Key;

public class PublicKeyDBService extends DatabaseService{	

	/** The only instance of this class */
	private volatile static PublicKeyDBService singleton = new PublicKeyDBService();
		
	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link PublicKeyDBServic#getInstance()} to get the	
	 *  only instance of this class.
	 */
	private PublicKeyDBService() {
		super(new DatabaseConfiguration(
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
					+ "publicKey STRING NOT NULL CHECK (publicKey>1))"); // check max length 
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to initialize database!", e);
		}
		L.d(this.getClass(), "Logging user database...\n" + this.toString());
	}	
	
	public static void main(String[] args) {
		PublicKeyDBService.getInstance().launch();

	}
	
	/**
	 * stores new public Key for not stored userId
	 * @param uID
	 * @param publicKey
	 * @return whether Key and userId were stored successfully
	 */
	public boolean storePublicKey( long uID, String publicKey) {
		if(!isConnected()) {
			L.e(this.getClass(), "Cannot store key! You aren't connected to the user database");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("INSERT INTO publicKeys"
					+ " (userId, publicKey)" 
					+ " VALUES(?, ?);");
			pst.setString(1, Long.toString(uID));
			pst.setString(2, String.valueOf( publicKey));
		
			pst.executeUpdate();
			
			L.d(this.getClass(), "Store key for userId: " + Long.toString(uID));
			
			pst.close();
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to store key", e);
		}
		return false;
	}
	
	/**
	 * updates the Key for a certain userId
	 * !!! this should happen very rarely and only if the private key changed
	 * @param uID
	 * @param publicKey
	 * @return whether the update was successful
	 */
	public boolean updatePublicKey(long uID, String publicKey) {
		if(!isConnected()) {
			L.e(this.getClass(), "Cannot update key! You aren't connected to the key database");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("UPDATE publicKeys SET "
					+ "publicKey = ?, "
					+ "WHERE userId = ?;");
			
			pst.setString(1,String.valueOf(publicKey));
			pst.setString(2, Long.toString(uID));
		
			pst.executeUpdate();
			
			L.d(this.getClass(), "Update key for userId: " + uID);
			
			pst.close();
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to update key", e);
		}
		return false;
	}
	
	/**
	 * Gives the stored public key for a userId
	 * @param userId
	 * @return the Key of the public Key
	 */
	public Key getPublicKey(long userId) {
		
		if(!isConnected()) {
			L.e(this.getClass(), "Cannot get Key! You aren't connected to the user database");
			return null;
		}
		
		
		try {
			PreparedStatement stm = getConnection().prepareStatement("SELECT publicKey FROM publicKeys "
					+ "WHERE userId = ?;");
			
			stm.setString(1, Long.toString(userId));
		
			ResultSet res = stm.executeQuery();
			
			L.d(this.getClass(), "Update key for userId: " + userId);
			
			stm.close();
			
			if(res != null && res.next()) {
				
				String pubKey = res.getString(1);
				
				
				Key publicKey = new Key(pubKey, publicKey); // which valid value for Type keyType ??: Type.PRIVATE => private Key; Type.PUBLIC => public Key
				
				L.d(this.getClass(), "Returns public key" + publicKey.toString()  + " for userId: " + userId + "" );
				return publicKey;
			}else {
				L.w(this.getClass(), "Didn't find any key for userID \"" + userId + "\"");
				return null;
			}
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to get key from DB", e);
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
			L.e(this.getClass(), "Cannot delete Key! You aren't connected to the user database");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("DELETE FROM publicKeys"
					+ " WHERE userId = ?;");
					
			pst.setString(1, String.valueOf(userId));
			
			pst.executeUpdate();
			
			pst.close();
			
			L.d(this.getClass(), "Deleted key for userId: " + userId);
			
			return true;
		}catch(SQLException e) {
			L.e(this.getClass(), "Failed to delete key", e);
		}
		return false;
	}

}
