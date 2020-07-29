package com.stamacoding.rsaApp.server.message.data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ServerDataTest {

	@DisplayName("Test the server data's constructor")
	@Nested
	class ConstructorTest{
		
		@DisplayName("Test with valid arguments")
		@Test
		void testWithValidArguments() {
			assertDoesNotThrow(() -> {
				ServerData s = new ServerData((byte) 12, (byte) 23);
				assertEquals((byte) 12, s.getSendingId());
				assertEquals((byte) 23, s.getReceivingId());
			});
			assertDoesNotThrow(() -> {
				ServerData s = new ServerData((byte) 100, (byte) 125);
				assertEquals((byte) 100, s.getSendingId());
				assertEquals((byte) 125, s.getReceivingId());
			});
		}
		
		@DisplayName("Test with invalid arguments")
		@Test
		void testWithInvalidArguments() {
			assertThrows(IllegalArgumentException.class, () -> {new ServerData((byte) -23, (byte) -100);});
			assertThrows(IllegalArgumentException.class, () -> {new ServerData((byte) 12, (byte) -77);});
			assertThrows(IllegalArgumentException.class, () -> {new ServerData((byte) -12,(byte) 23);});
		}
		
	}
	
	@DisplayName("Test equals()")
	@Nested
	class EqualityTest{
		
		@DisplayName("Test equal objects")
		@Test
		void testEqual() {
			ServerData d1 = new ServerData((byte) 25, (byte) 77);
			ServerData d2 = new ServerData((byte) 25, (byte) 77);
			
			assertTrue(d2.equals(d1));
		}
		
		@DisplayName("Test not equal objects")
		@Test
		void testNotEqual() {
			ServerData d1 = new ServerData((byte) 11, (byte) 77);
			ServerData d2 = new ServerData((byte) 25, (byte) 77);
			
			assertFalse(d2.equals(d1));
			
			ServerData d3 = new ServerData((byte) 25, (byte) 23);
			ServerData d4 = new ServerData((byte) 25, (byte) 77);
			
			assertFalse(d3.equals(d4));
			
			ServerData d5 = new ServerData((byte) 1, (byte) 2);
			ServerData d6 = new ServerData((byte) 25, (byte) 77);
			
			assertFalse(d5.equals(6));
		}
	}

}
