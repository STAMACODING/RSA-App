package com.stamacoding.rsaApp.network.server.service.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.service.message.ChatDatabaseService;
import com.stamacoding.rsaApp.network.global.TextUtils;
import com.stamacoding.rsaApp.network.global.service.executor.database.DatabaseConfiguration;
import com.stamacoding.rsaApp.network.global.service.executor.database.DatabaseService;
import com.stamacoding.rsaApp.network.global.user.Password;
import com.stamacoding.rsaApp.network.global.user.User;
import com.stamacoding.rsaApp.security.passwordHashing.PasswordHasher;

public class UserDatabaseService extends DatabaseService{
	/** The only instance of this class */
	private volatile static UserDatabaseService singleton = new UserDatabaseService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ChatDatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private UserDatabaseService() {
		super(new DatabaseConfiguration(
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
	
	/**
	 * updates the login Data of a user in the DB
	 * @param u the Object of the new User
	 * @return
	 */
	public boolean updateUser(User u) {
		validateThread();
		validateConnection();
		
		if(!u.isStored()) {
			L.e(this.getClass(), "Cannot update unstored user!");
			return false;
		}
		
		if(!u.getPassword().isHashed()) {
			L.e(this.getClass(), "Cannot store user with an unhashed password!");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("UPDATE Users SET "
					+ "name = ?, "
					+ "password = ?, "
					+ "salt = ?, "
					+ "WHERE id = ?;");
			pst.setString(1, u.getName());
			pst.setString(2, u.getPassword().getHashedPasswordAsString());
			pst.setString(3, u.getPassword().getSaltAsString());
			pst.setLong(4, u.getId());
			
			pst.executeUpdate();
			
			L.i(this.getClass(), "Updated user: " + u.toString());
			u.setUpdateRequested(false);
			pst.close();
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to update user", e);
		}
		return false;
	}

	/**
	 * stores a completely new user to the userLoginDB
	 * @param u the Object of the new user
	 * @return whether the storing process was executed correctly
	 */
	public boolean storeUser(User u) {
		validateThread();
		validateConnection();
		
		if(u.isStored()) {
			L.e(this.getClass(), "Cannot store already stored user!");
			return false;
		}
		
		if(!u.getPassword().isHashed()) {
			L.e(this.getClass(), "Cannot store user with an unhashed password!");
			return false;
		}
		
		try {
			PreparedStatement pst = getConnection().prepareStatement("INSERT INTO Users "
					+ "(name, password, salt) VALUES( "
					+ "?, ?, ?);");
			pst.setString(1, u.getName());
			pst.setString(2, u.getPassword().getHashedPasswordAsString());
			pst.setString(3, u.getPassword().getSaltAsString());
			
			pst.executeUpdate();
			
			Statement statement = getConnection().createStatement();
			ResultSet res = statement.executeQuery("SELECT MAX(id) AS LAST FROM Users");
			long id = Long.parseLong(res.getString("LAST"));
			u.setId(id);
			
			L.i(this.getClass(), "Stored user: " + u.toString());
			pst.close();
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to store user", e);
		}
		return false;
	}
	
	public ArrayList<User> getUsers() {
		validateThread();
		validateConnection();
		
		try {
			Statement stm = getConnection().createStatement();
			ResultSet res = stm.executeQuery("SELECT "
					+ "id, name, "
					+ "password, salt "
					+ "FROM Users;");
			
			if(res != null) {
				ArrayList<User> users = new ArrayList<User>();
				
				while(res.next()) {
					User u = new User(res.getLong(1), res.getString(2), new Password(res.getString(3), res.getString(4)));
					users.add(u);
				}
				L.d(this.getClass(), "Got (" + users.size() + ") users");
				return users;
			}else {
				L.d(this.getClass(), "Got (0) users");
				return null;
			}
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to get users", e);
		}
		return null;
	}
	
	public User getUser(String username) {
		validateThread();
		validateConnection();
		
		
		try {
			PreparedStatement stm = getConnection().prepareStatement("SELECT "
					+ "id, name, "
					+ "password, salt "
					+ "FROM Users WHERE name = ?;");
			stm.setString(1, username);
			
			ResultSet res = stm.executeQuery();
			
			if(res != null && res.next()) {
				User u = new User(res.getLong(1), username, new Password(res.getString(3), res.getString(4)));
				return u;
			}else {
				L.w(this.getClass(), "Didn't find any user with the name \"" + username + "\"");
				return null;
			}
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to get user", e);
		}
		return null;
	}
	
	public User getUser(long id) {
		validateThread();
		validateConnection();
		
		try {
			PreparedStatement stm = getConnection().prepareStatement("SELECT "
					+ "id, name, "
					+ "password, salt "
					+ "FROM Users WHERE id = ?;");
			stm.setLong(1, id);
			
			ResultSet res = stm.executeQuery();
			
			if(res != null && res.next()) {
				User u = new User(id, res.getString(2), new Password(res.getString(3), res.getString(4)));
				return u;
			}else {
				L.w(this.getClass(), "Didn't find any user with the id \"" + id + "\"");
				return null;
			}
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to get user by id", e);
		}
		return null;
	}
	
	
	private boolean deleteUser(User u) {
		if(!u.isStored()) {
			L.w(this.getClass(), "Cannot delete an unstored user!");
			return false;
		}
		return deleteUser(u.getId());
	}

	private boolean deleteUser(long id){
		validateThread();
		validateConnection();
		
		try {
			int userId = (int) id;
			PreparedStatement pst = getConnection().prepareStatement("DELETE FROM Users WHERE id = ?;");
			pst.setLong(1, userId);
			
			pst.executeUpdate();
			pst.close();
			L.i(this.getClass(), "Deleted user using id(" + id + ")");
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to delete user", e);
		}
		return false;
	}
	
	
	public String toString() {
		validateThread();
		validateConnection();
		
		ArrayList<User> users = getUsers();
		
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<64; i++) sb.append("#");
		sb.append('\n');
		sb.append(String.format("| %-18s | %-18s | %-18s |\n", "id", "name", "password"));

		for(int i=0; i<64; i++) sb.append("#");
		sb.append('\n');
		
		if(users.size() == 0) {
			sb.append("\t Database is empty!\n");
		}
		
		for(int i=0; i<users.size(); i++) {
			User u = users.get(i);
			sb.append(String.format("| %-18s | %-18s | %-18s |\n", 
					TextUtils.cut(String.valueOf(u.getId()), 18),
					TextUtils.cut(u.getName().toString(), 18),
					TextUtils.cut(u.getPassword().toString(), 18)));
			if(i+1<users.size()) {
				for(int j=0; j<64; j++) sb.append("-");
				sb.append('\n');
			}
		}
		for(int i=0; i<64; i++) sb.append("#");
		return sb.toString();
	}
	
	public boolean isPasswordCorrect(User registeredUser) {
		validateThread();
		validateConnection();
		
		if(registeredUser.getPassword().getClearPassword() == null) {
			L.e(this.getClass(), "Users clear password is not allowed to be null!");
			return false;
		}
		
		User userFromDb = getUser(registeredUser.getName());
		if(userFromDb == null) {
			L.e(this.getClass(), "Coudn't find user with specified name (" + registeredUser.getName() + ")");
			return false;
		}
		return userFromDb.getPassword().check(registeredUser.getPassword().getClearPassword());
	}
	
	public boolean isUsernameAvailable(String username) {
		validateThread();
		validateConnection();
		
		try {
			PreparedStatement stm = getConnection().prepareStatement("SELECT name FROM Users WHERE name = ?;");
			stm.setString(1, username);
			
			ResultSet res = stm.executeQuery();
			
			if(res != null && res.next()) {
				L.d(this.getClass(), "Username is not available (" + username + ")");
				return false;
			}
			L.d(this.getClass(), "Username is available (" + username + ")");
			return true;
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to check if username is available", e);
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
					+ "password TEXT NOT NULL, "
					+ "salt VARCHAR (" + PasswordHasher.SALT_STRING_LENGTH + ") NOT NULL CHECK (LENGTH(salt) = " + PasswordHasher.SALT_STRING_LENGTH + " ));");
		} catch (SQLException e) {
			L.e(this.getClass(), "Failed to initialize database!", e);
		}
		L.i(this.getClass(), "Logging user database...\n" + this.toString());
	}
	
	public static void main(String[] args) {
		UserDatabaseService.getInstance().launch();
		UserDatabaseService.getInstance().execute(new Callable<Object>() {
			
			@Override
			public Object call() throws Exception {
				UserDatabaseService.getInstance().storeUser(new User("Josef", new Password("Joseef123".toCharArray(), PasswordHasher.generateSalt())));
				return null;
			}
		});
	}
}
