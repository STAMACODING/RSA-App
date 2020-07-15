package com.stamacoding.rsaApp.server.config;

import com.stamacoding.rsaApp.server.services.mainService.MessageService;

/**
 * Used to declare the type of the device the {@link MessageService} is running on.
 */
public enum NetworkType {
	
	/** Server  */
	SERVER, 
	
	/** Client  */
	CLIENT
}
