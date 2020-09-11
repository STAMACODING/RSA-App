package com.stamacoding.rsaApp.server.global;

import java.util.regex.Pattern;

/**
 *  Contains static functions that are useful for the server team.
 */
public class NetworkUtils {

	private static final Pattern IPv4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
			"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
			"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
			"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
	
	/**
	 * Validates an ip-address.
	 * @param ip the ip to validate
	 * @return whether the ip-address is valid
	 */
	public static boolean isValidInet4Address(String ip) {
		if (ip == null) {
			return false;
		}
		return IPv4_PATTERN.matcher(ip).matches();
	}
	
	public static void main(String[] args) {
		System.out.println(isValidInet4Address("172.8.9.28"));
	}
}
