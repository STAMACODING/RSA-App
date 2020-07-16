package com.stamacoding.rsaApp.server.message;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.stamacoding.rsaApp.server.message.data.LocalData;
import com.stamacoding.rsaApp.server.message.data.ProtectedData;
import com.stamacoding.rsaApp.server.message.data.SendState;
import com.stamacoding.rsaApp.server.message.data.ServerData;

class AbstractMessageManagerTest {

	@DisplayName("Test manage()")
	@Test
	void testManage() {
		Message m = new Message(new LocalData(3, SendState.PENDING), new ProtectedData("Hallo Tim!", 2443444L), new ServerData((byte) 23, (byte) 11));
		AbstractMessageManager manager = new AbstractMessageManager() {};
		assertDoesNotThrow(() -> {manager.manage(m);});
		assertEquals(m, manager.getAllMessages().get(0));
	}

}
