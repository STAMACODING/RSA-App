package server.config;

import server.services.mainService.MessageService;

/**
 * Used to declare the type of the device the {@link MessageService} is running on.
 *
 */
public enum NetworkType {
	SERVER, 
	CLIENT
}
