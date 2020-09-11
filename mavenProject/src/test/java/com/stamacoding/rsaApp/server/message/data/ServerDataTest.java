package com.stamacoding.rsaApp.server.message.data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.stamacoding.rsaApp.server.global.message.data.ServerData;

class ServerDataTest {

	@DisplayName("Test the server data's constructor")
	@Nested
	class ConstructorTest{
		
		@DisplayName("Test with valid arguments")
		@Test
		void testWithValidArguments() {
			assertDoesNotThrow(() -> {
				ServerData s = new ServerData("josef", "günter");
				assertEquals("josef", s.getSending());
				assertEquals("günter", s.getReceiving());
			});
			assertDoesNotThrow(() -> {
				ServerData s = new ServerData("sd", "sdfsaf");
				assertEquals("sd", s.getSending());
				assertEquals("sdfsaf", s.getReceiving());
			});
		}
		
		@DisplayName("Test with invalid arguments")
		@Test
		void testWithInvalidArguments() {
			assertThrows(IllegalArgumentException.class, () -> {new ServerData(null, null);});
			assertThrows(IllegalArgumentException.class, () -> {new ServerData("ddsf", null);});
			assertThrows(IllegalArgumentException.class, () -> {new ServerData(null, "dfsf");});
			
			assertThrows(IllegalArgumentException.class, () -> {new ServerData("", "");});
			assertThrows(IllegalArgumentException.class, () -> {new ServerData("ddsf", "");});
			assertThrows(IllegalArgumentException.class, () -> {new ServerData("", "dfsf");});
		}
		
	}
	
	@DisplayName("Test equals()")
	@Nested
	class EqualityTest{
		
		@DisplayName("Test equal objects")
		@Test
		void testEqual() {
			ServerData d1 = new ServerData("josef", "maria");
			ServerData d2 = new ServerData("josef", "maria");
			
			assertTrue(d2.equals(d1));
		}
		
		@DisplayName("Test not equal objects")
		@Test
		void testNotEqual() {
			ServerData d1 = new ServerData("tim", "berka");
			ServerData d2 = new ServerData("tom", "berka");
			
			assertFalse(d2.equals(d1));
			
			ServerData d3 = new ServerData("tim", "berka");
			ServerData d4 = new ServerData("tim", "bohan");
			
			assertFalse(d3.equals(d4));
			
			ServerData d5 = new ServerData("tim", "berka");
			ServerData d6 = new ServerData("josef", "maria");
			
			assertFalse(d5.equals(6));
		}
	}

}
