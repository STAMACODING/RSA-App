package com.stamacoding.rsaApp.network.server.managers;

import java.util.ArrayList;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.client.services.ClientReceiveService;
import com.stamacoding.rsaApp.network.global.user.User;

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
				L.w(getClass(), "Cannot add null user!");
				continue;
			}
			if(getUsers().contains(users[i])) {
				L.w(getClass(), "You tried to add an already existing user!");
				continue;
			}
			getUsers().add(users[i]);
			L.t(getClass(), "Added user: " + users[i].toString());
		}
	}
	
	public User poll() {
		for(int i=0; i<getUsers().size(); i++) {
			if(!getUsers().get(i).isStored() || getUsers().get(i).isUpdateRequested()) {
				User u = getUsers().get(i);
				L.t(getClass(), "Found user to store/update: " + u.toString());
				getUsers().remove(u);
				L.t(getClass(), "Removed user: " + u.toString());
				return u;
			}
		}
		return null;
	}
}
