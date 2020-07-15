package com.stamacoding.rsaApp.server.message.data;

/**
 * Describes whether the message has to get sent or has already been sent successfully.
 */
public enum SendState {
	/** Message should get sent. **/
	PENDING, 
	/** Message has already been sent successfully. **/
	SENT
}
