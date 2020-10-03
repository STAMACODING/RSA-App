package com.stamacoding.rsaApp.log;

import com.stamacoding.rsaApp.log.debug.Debug;
import com.stamacoding.rsaApp.log.logger.Logger;

public class Test {
	public static void main(String[] args) {
		Logger.debug(Test.class.getSimpleName(), "Hello");
	}
}
