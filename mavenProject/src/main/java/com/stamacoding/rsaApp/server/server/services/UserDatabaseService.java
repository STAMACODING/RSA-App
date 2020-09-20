package com.stamacoding.rsaApp.server.server.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.client.services.ChatDatabaseService;
import com.stamacoding.rsaApp.server.client.services.database.DatabaseConfiguration;
import com.stamacoding.rsaApp.server.client.services.database.DatabaseService;
import com.stamacoding.rsaApp.server.message.data.LocalData;
import com.stamacoding.rsaApp.server.server.managers.UserManager;
import com.stamacoding.rsaApp.server.user.User;

public class UserDatabaseService extends DatabaseService{
	/** The only instance of this class */
	private volatile static UserDatabaseService singleton = new UserDatabaseService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ChatDatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private UserDatabaseService() {
		super(UserDatabaseService.class.getSimpleName(), new DatabaseConfiguration(
				"jdbc:sqlite:ServerDatabase.db",
				"root",
				"root"));
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static UserDatabaseService getInstance() {
		return singleton;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
	}

	@Override
	public void onRepeat() {
		// Check if there is any message to store or update
		User u = UserManager.getInstance().getUserToStoreOrUpdate();
		
		if(u != null) {
			Logger.debug(this.getClass().getSimpleName(), "Got user to store/update: " + u.toString());
			
			// Update message if is is already stored in the chat database
			if(!u.isStored()) storeUser(u);
			
			// Store new message
			else updateUser(u);
		}
	}
	
	/**
	 * updates the login Data of a user in the DB
	 * @param u the Object of the new User
	 * @return
	 */
	private boolean updateUser(User u) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot update User! You aren't connected to the user database");
			return false;
		}
		
		if(!u.isStored()) {
			Logger.error(this.getServiceName(), "Cannot update unstored user!");
			return false;
			}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("UPDATE userLoginSystem SET "
					+ "name = ?, "
					+ "password = ?, "
					+ "WHERE id = ?;");
			
			int userID = (int) u.getUserId();
			pst.setString(1, u.getName());
			pst.setString(2, u.getPassword());
			pst.setInt(3, userID);
			
			pst.executeUpdate();
			
			Logger.debug(this.getClass().getSimpleName(), "Updated user: " + u.toString());
			u.setUpdateRequested(false);
			pst.close();
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to update user (SQL exception)");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * stores a completely new user to the userLoginDB
	 * @param u the Object of the new user
	 * @return wheteher the storing proccess was executed corretly
	 */
	private boolean storeUser(User u) {
		// TODO Auto-generated method stub
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot store user! You aren't connected to the chat database");
			return false;
		}
		
		if(u.isStored()) {
			Logger.error(this.getClass().getSimpleName(), "Cannot store already stored user!");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("INSERT INTO userLoginData "
					+ "(name, password, updateRequested) VALUES( "
					+ "?, ?, ?);");
			pst.setString(1, u.getName());
			pst.setString(2, u.getPassword());
			pst.setBoolean(3, u.isUpdateRequested());
			
			
			pst.executeUpdate();
			
			Statement statement = getConnection().createStatement();
			ResultSet res = statement.executeQuery("SELECT MAX(id) AS LAST FROM Messages");
			long id = Long.parseLong(res.getString("LAST"));
			
			Logger.debug(this.getClass().getSimpleName(), "Stored user: " + u.toString());
			pst.close();
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to store user (SQL exception)");
			e.printStackTrace();
		}
		return false;
		//Logger.debug(this.getClass().getSimpleName(), "Stored user: " + u.toString());
		//u.setUserId(23);
	}

	private boolean delteUser(User u) {
		if(!u.isStored()) {
		Logger.error(this.getClass().getSimpleName(), "Cannot delete an unstored user!");
		return false;
		}
		
	return deleteUser(u.getUserId());
	}

	private boolean deleteUser(long id){
	if(!isConnected()) {
		Logger.error(this.getServiceName(), "Cannot delete user! You aren't connected to the chat database");
		return false;
	}
	
	try {
		int userId = (int) id;
		PreparedStatement pst = getConnection().prepareStatement("DELETE FROM usersLoginData WHERE id = ?;");
		pst.setLong(1, userId);
		
		pst.executeUpdate();
		pst.close();
		Logger.debug(this.getClass().getSimpleName(), "Deleted user using id(" + id + ")");
		return true;
	} catch (SQLException e) {
		Logger.error(this.getClass().getSimpleName(), "Failed to delete user and suddenly nothing works as it should.)");
		e.printStackTrace();
	}
	return false;
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}

}
