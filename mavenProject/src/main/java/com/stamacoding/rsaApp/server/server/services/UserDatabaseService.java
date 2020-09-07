package com.stamacoding.rsaApp.server.server.services;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.server.Service;
import com.stamacoding.rsaApp.server.client.services.ChatDatabaseService;
import com.stamacoding.rsaApp.server.server.managers.UserManager;
import com.stamacoding.rsaApp.server.user.User;

public class UserDatabaseService extends Service{
	/** The only instance of this class */
	private volatile static UserDatabaseService singleton = new UserDatabaseService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link ChatDatabaseService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private UserDatabaseService() {
		super(UserDatabaseService.class.getSimpleName());
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
	
	// TODO
	private void updateUser(User u) {
		// TODO Auto-generated method stub
		Logger.debug(this.getClass().getSimpleName(), "Updated user: " + u.toString());
		u.setUpdateRequested(false);
	}

	// TODO
	private void storeUser(User u) {
		// TODO Auto-generated method stub
		Logger.debug(this.getClass().getSimpleName(), "Stored user: " + u.toString());
		u.setId(23);
	}
}
