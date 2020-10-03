package com.stamacoding.rsaApp.network.global.message.data;

/**
 * Describes whether the message has to get sent or has already been sent successfully.
 */
public enum SendState {
	/** Message should get sent. **/
	PENDING, 
	/** Message has already been sent successfully. **/
	SENT,
	/** Message could not get sent. **/
	FAILED;
	
	public static int asInt(SendState sendState) {
		switch(sendState) {
		case PENDING:
			return 0;
		case SENT:
			return 1;
		case FAILED:
			return -1;
		default:
			return -1;
		}
	}
	
	public static SendState parseInt(int sendState) {
		switch(sendState) {
		case 0:
			return SendState.PENDING;
		case 1:
			return SendState.SENT;
		default:
			return SendState.FAILED;
		}
	}
}
