package com.stamacoding.rsaApp.server.server.managers;

import java.util.ArrayList;

import com.stamacoding.rsaApp.server.client.services.ClientReceiveService;
import com.stamacoding.rsaApp.server.user.User;

public class UserManager {
	/** The only instance of this class */
	private volatile static UserManager singleton = new UserManager();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ClientReceiveService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private UserManager() {
		
	}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static UserManager getInstance() {
		return singleton;
	}
	
	/** {@link ArrayList} containing all users stored in the user database */
	private volatile ArrayList<User> users = new ArrayList<User>();
	
	
	/**
	 * Gets an {@link ArrayList} containing all users managed by the user manager.
	 * @return {@link ArrayList} containing all users managed by the user manager
	 */
	public ArrayList<User> getUsers() {
		return users;
	}
	
	public void add(User...users) {
		for(int i=0; i<users.length; i++) {
			if(users[i] == null) {
				continue;
			}
			if(getUsers().contains(users[i])) {
				continue;
			}
			getUsers().add(users[i]);
		}
	}
	
	public User getUserToStoreOrUpdate() {
		for(int i=0; i<getUsers().size(); i++) {
			if(!getUsers().get(i).isStored() || getUsers().get(i).isUpdateRequested()) return getUsers().get(i);
		}
		return null;
	}
	
	public boolean isUsernameAvailable(String username) {
		for(int i=0; i<getUsers().size(); i++) {
			if(getUsers().get(i).getName().equals(username)) return false;
		}
		return true;
	}
	
	public boolean canLogin(String username, String password) {
		for(int i=0; i<getUsers().size(); i++) {
			if(getUsers().get(i).getName().equals(username) && getUsers().get(i).getPassword().equals(password)) return true;
		}
		return false;
	}
	
	public User getUser(String username) {
		for(int i=0; i<getUsers().size(); i++) {
			if(getUsers().get(i).getName().equals(username)) return getUsers().get(i);
		}
		return null;
	}
}
