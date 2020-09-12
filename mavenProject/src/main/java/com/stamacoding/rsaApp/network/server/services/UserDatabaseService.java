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
		User u = UserManager.getInstance().poll();
		
		if(u != null) {
			Logger.debug(this.getClass().getSimpleName(), "Got user to store/update: " + u.toString());
			
			// Update message if is is already stored in the chat database
			if(!u.isStored()) {
				if(!storeUser(u) && !u.isStored()) {
					UserManager.getInstance().add(u);
				}
			}
			
			// Store new message
			else if(!updateUser(u) && u.isUpdateRequested()) {
				UserManager.getInstance().add(u);
			}
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
		
		if(!u.getPassword().isHashed()) {
			Logger.error(this.getClass().getSimpleName(), "Cannot store user with an unhashed password!");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("UPDATE Users SET "
					+ "name = ?, "
					+ "password = ?, "
					+ "salt = ?, "
					+ "WHERE id = ?;");
			pst.setString(1, u.getName());
			pst.setString(2, u.getPassword().getHashedPassword());
			pst.setString(3, u.getPassword().getSalt());
			pst.setLong(4, u.getId());
			
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
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot store user! You aren't connected to the chat database");
			return false;
		}
		
		if(u.isStored()) {
			Logger.error(this.getClass().getSimpleName(), "Cannot store already stored user!");
			return false;
		}
		
		if(!u.getPassword().isHashed()) {
			Logger.error(this.getClass().getSimpleName(), "Cannot store user with an unhashed password!");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("INSERT INTO Users "
					+ "(name, password, salt) VALUES( "
					+ "?, ?, ?);");
			pst.setString(1, u.getName());
			pst.setString(2, u.getPassword().getHashedPassword());
			pst.setString(3, u.getPassword().getSalt());
			
			pst.executeUpdate();
			
			Statement statement = getConnection().createStatement();
			ResultSet res = statement.executeQuery("SELECT MAX(id) AS LAST FROM Users");
			long id = Long.parseLong(res.getString("LAST"));
			u.setId(id);
			
			Logger.debug(this.getClass().getSimpleName(), "Stored user: " + u.toString());
			pst.close();
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to store user (SQL exception)");
			e.printStackTrace();
		}
		return false;
	}
	
	private ArrayList<User> getUsers() {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot get users! You aren't connected to the user database");
			return null;
		}
		
		try {
			Statement stm = getConnection().createStatement();
			ResultSet res = stm.executeQuery("SELECT "
					+ "id, name, "
					+ "password, salt "
					+ "FROM Users;");
			
			if(res != null) {
				ArrayList<User> users = new ArrayList<User>();
				
				while(res.next()) {
					User u = new User(res.getLong(1), res.getString(2), new Password(res.getString(3), res.getString(4), true));
					users.add(u);
				}
				Logger.debug(this.getClass().getSimpleName(), "Got (" + users.size() + ") users");
				return users;
			}else {
				Logger.debug(this.getClass().getSimpleName(), "Got (0) users");
				return null;
			}
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to get users (SQL exception)");
			e.printStackTrace();
		}
		return null;
	}
	
	public User getUser(String username) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot get user! You aren't connected to the user database");
			return null;
		}
		try {
			PreparedStatement stm = getConnection().prepareStatement("SELECT "
					+ "id, name, "
					+ "password, salt "
					+ "FROM Users WHERE name = ?;");
			stm.setString(1, username);
			
			ResultSet res = stm.executeQuery();
			
			if(res != null && res.next()) {
				User u = new User(res.getLong(1), username, new Password(res.getString(3), res.getString(4), true));
				return u;
			}else {
				Logger.warning(this.getClass().getSimpleName(), "Didn't find any user with the name \"" + username + "\"");
				return null;
			}
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to get user (SQL exception)");
			e.printStackTrace();
		}
		return null;
	}
	
	private User getUser(long id) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot get user! You aren't connected to the user database");
			return null;
		}
		try {
			PreparedStatement stm = getConnection().prepareStatement("SELECT "
					+ "id, name, "
					+ "password, salt "
					+ "FROM Users WHERE id = ?;");
			stm.setLong(1, id);
			
			ResultSet res = stm.executeQuery();
			
			if(res != null && res.next()) {
				User u = new User(id, res.getString(2), new Password(res.getString(3), res.getString(4), true));
				return u;
			}else {
				Logger.warning(this.getClass().getSimpleName(), "Didn't find any user with the id \"" + id + "\"");
				return null;
			}
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to get user (SQL exception)");
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString() {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot print database! You aren't connected to the user database");
			return "[ NOT CONNECTED TO DATABASE ]";
		}
		ArrayList<User> users = getUsers();
		
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<64; i++) sb.append("#");
		sb.append('\n');
		sb.append(String.format("| %-18s | %-18s | %-18s |\n", "id", "name", "password"));

		for(int i=0; i<64; i++) sb.append("#");
		sb.append('\n');
		for(int i=0; i<users.size(); i++) {
			User u = users.get(i);
			sb.append(String.format("| %-18s | %-18s | %-18s |\n", 
					u.getId(),
					u.getName(),
					u.getPassword()));
			if(i+1<users.size()) {
				for(int j=0; j<64; j++) sb.append("-");
				sb.append('\n');
			}
		}
		for(int i=0; i<64; i++) sb.append("#");
		return sb.toString();
	}
	
	public boolean isPasswordCorrect(User registeredUser) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot check if username is available! You aren't connected to the user database");
			return false;
		}
		if(registeredUser.getPassword().getClearPassword() == null) {
			Logger.error(this.getServiceName(), "Users clear password is not allowed to be null!");
			return false;
		}
		
		User userFromDb = getUser(registeredUser.getName());
		if(userFromDb == null) {
			Logger.error(this.getServiceName(), "Coudn't find user with specified name (" + registeredUser.getName() + ")");
			return false;
		}
		return userFromDb.getPassword().check(registeredUser.getPassword().getClearPassword());
	}
	
	public boolean isUsernameAvailable(String username) {
		if(!isConnected()) {
			Logger.error(this.getServiceName(), "Cannot check if username is available! You aren't connected to the user database");
			return false;
		}
		try {
			PreparedStatement stm = getConnection().prepareStatement("SELECT name FROM Users WHERE name = ?;");
			stm.setString(1, username);
			
			ResultSet res = stm.executeQuery();
			
			if(res != null && res.next()) {
				Logger.debug(this.getServiceName(), "Username is not available (" + username + ")");
				return false;
			}
			Logger.debug(this.getServiceName(), "Username is available (" + username + ")");
			return true;
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to check if username is available (SQL exception)");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void initialize() {
		try {
			Statement statement = getConnection().createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS Users ("
					+ "id INTEGER PRIMARY KEY CHECK ((id > 0)), "
					+ "name VARCHAR (15) NOT NULL UNIQUE CHECK (LENGTH(name) > 0), "
					+ "password VARCHAR (" + Security.HASHED_PW_LENGTH + ") NOT NULL CHECK (LENGTH(password) = " + Security.HASHED_PW_LENGTH + " ),"
					+ "salt VARCHAR (" + Security.SALT_LENGTH + ") NOT NULL CHECK (LENGTH(salt) = " + Security.SALT_LENGTH + " ));");
		} catch (SQLException e) {
			Logger.error(this.getClass().getSimpleName(), "Failed to initialize database! (SQL Exception)");
			e.printStackTrace();
		}
		Logger.debug(this.getServiceName(), "Logging user database...\n" + this.toString());
	}
	
	public static void main(String[] args) {
		UserDatabaseService.getInstance().launch();
	}
}
