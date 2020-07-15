package com.stamacoding.rsaApp.server.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NetworkConfigTest {

	@Test
	@DisplayName("Test the NetworkConfig's setup() method")
	public void testSetup() {
		NetworkConfig.setup(NetworkType.CLIENT, (byte) 2, "192.168.2.122", 100, 99, 0);
		assertEquals(NetworkConfig.TYPE, NetworkType.CLIENT);
		assertEquals(NetworkConfig.Client.ID, 2);
		assertEquals(NetworkConfig.Server.IP, "192.168.2.122");
		assertEquals(NetworkConfig.Server.SEND_PORT, 100);
		assertEquals(NetworkConfig.Server.RECEIVE_PORT, 99);
		
		NetworkConfig.setup(NetworkType.SERVER, (byte) 4, "Kuchen", 1111, 2323, 1000);
		assertEquals(NetworkConfig.TYPE, NetworkType.SERVER);
		assertEquals(NetworkConfig.Client.ID, 4);
		assertEquals(NetworkConfig.Server.IP, "Kuchen");
		assertEquals(NetworkConfig.Server.SEND_PORT, 1111);
		assertEquals(NetworkConfig.Server.RECEIVE_PORT, 2323);
		assertEquals(NetworkConfig.Client.QUERY_MESSAGES_INTERVAL, 1000);
	}
	
	@Nested
	@DisplayName("Test the NetworkConfig's isValid() method")
	public class testIsValid{
		@DisplayName("Test isValid() using a valid setup")
		@Test
		public void testValidSetup() {
			NetworkConfig.setup(NetworkType.CLIENT, (byte) 2, "192.168.2.122", 100, 99, 0);
			assertTrue(NetworkConfig.isValid());
		}
		
		@DisplayName("Test isValid() using an invalid server ip")
		@Test
		public void testInvalidServerIp() {
			NetworkConfig.setup(NetworkType.CLIENT, (byte) 2, "Kuchen", 100, 99, 0);
			assertFalse(NetworkConfig.isValid());
		}
		
		@DisplayName("Test isValid() using an invalid queryMessagesInterval")
		@Test
		public void testInvalidQueryMessagesInterval() {
			NetworkConfig.setup(NetworkType.CLIENT, (byte) 2, "192.168.2.122", 100, 99, -5);
			assertFalse(NetworkConfig.isValid());
		}
		
		@DisplayName("Test isValid() using an invalid sendPort")
		@Test
		public void testInvalidSendPort() {
			NetworkConfig.setup(NetworkType.CLIENT, (byte) 2, "192.168.2.122", -2, 99, 0);
			assertFalse(NetworkConfig.isValid());
		}
		
		@DisplayName("Test isValid() using an invalid receivePort")
		@Test
		public void testInvalidReceivePort() {
			NetworkConfig.setup(NetworkType.SERVER, (byte) 2, "192.168.2.122", 100, -1, 0);
			assertFalse(NetworkConfig.isValid());
		}
		
		@DisplayName("Test isValid() using an invalid setup: receive- and sendPort are the same")
		@Test
		public void testDoublePortUsage() {
			NetworkConfig.setup(NetworkType.CLIENT, (byte) 2, "192.168.2.122", 100, 100, 0);
			assertFalse(NetworkConfig.isValid());
		}
		
		@DisplayName("Test isValid() using an invalid clientId")
		@Test
		public void testInvalidClientID() {
			NetworkConfig.setup(NetworkType.CLIENT, (byte) -3, "192.168.2.122", 100, 99, 0);
			assertFalse(NetworkConfig.isValid());
		}
		
		@DisplayName("Test isValid() using a null network type")
		@Test
		public void testNullNetworkType() {
			NetworkConfig.setup(null, (byte) -3, "192.168.2.122", 100, 99, 0);
			assertFalse(NetworkConfig.isValid());
		}
		
		@DisplayName("Test isValid() using a null serverIp")
		@Test
		public void testNullIp() {
			NetworkConfig.setup(NetworkType.CLIENT, (byte) -3, null, 100, 99, 0);
			assertFalse(NetworkConfig.isValid());
		}
	}

}
