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
	
	/** {@link ArrayList} containing all users to be stored or updated */
	private volatile ArrayList<User> users = new ArrayList<User>();
	
	
	/**
	 * Gets an {@link ArrayList} containing all users to be stored or updated.
	 * @return {@link ArrayList} containing all users to be stored or updated
	 */
	private ArrayList<User> getUsers() {
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
	
	public User poll() {
		for(int i=0; i<getUsers().size(); i++) {
			if(!getUsers().get(i).isStored() || getUsers().get(i).isUpdateRequested()) {
				User u = getUsers().get(i);
				getUsers().remove(u);
				return u;
			}
		}
		return null;
	}
}
