package com.stamacoding.rsaApp.rsa.keyCreate;

import com.stamacoding.rsaApp.log.logger.Logger;

public class KeyTests {
	public static void main(String[] args) {
		KeyPair pair = new KeyPair();
		Logger.debug(KeyTests.class.getSimpleName(), pair.toString());
	}

}
