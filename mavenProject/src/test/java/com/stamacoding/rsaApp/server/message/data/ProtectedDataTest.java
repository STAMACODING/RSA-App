package com.stamacoding.rsaApp.server.message.data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.stamacoding.rsaApp.server.global.message.data.ProtectedData;

class ProtectedDataTest {

	@DisplayName("Test the protected data's constructor")
	@Nested
	class ConstructorTest{
		
		@DisplayName("Test with valid arguments")
		@Test
		void testWithValidArguments() {
			assertDoesNotThrow(() -> {
				ProtectedData p = new ProtectedData("Hi Henri!", 23232323L);
				assertEquals("Hi Henri!", p.getTextMessage());
				assertEquals(23232323L, p.getDate());
			});
			assertDoesNotThrow(() -> {
				ProtectedData p = new ProtectedData("Josef, du bists?!", 122L);
				assertEquals("Josef, du bists?!", p.getTextMessage());
				assertEquals(122L, p.getDate());
			});
		}
		
		@DisplayName("Test with textMessage set to null")
		@Test
		void testNullMessage() {
			assertThrows(IllegalArgumentException.class, () -> {new ProtectedData(null, 23232323L);});
		}
		
		@DisplayName("Test with an invalid date")
		@Test
		void testInvalidTime() {
			assertThrows(IllegalArgumentException.class, () -> {new ProtectedData("Hi Henri!", -23);});
			assertThrows(IllegalArgumentException.class, () -> {new ProtectedData("Hi Henri!", -232323);});
		}
		
	}
	
	@DisplayName("Test equals()")
	@Nested
	class EqualityTest{
		
		@DisplayName("Test equal objects")
		@Test
		void testEqual() {
			ProtectedData d1 = new ProtectedData("Hello World!", 2133L);
			ProtectedData d2 = new ProtectedData("Hello World!", 2133L);
			
			assertTrue(d2.equals(d1));
		}
		
		@DisplayName("Test not equal objects")
		@Test
		void testNotEqual() {
			ProtectedData d1 = new ProtectedData("HellWorld!", 2133L);
			ProtectedData d2 = new ProtectedData("Hello World!", 2133L);
			assertFalse(d2.equals(d1));
			
			ProtectedData d3 = new ProtectedData("Hello World!", 23);
			ProtectedData d4 = new ProtectedData("Hello World!", 2133L);
			assertFalse(d3.equals(d4));
			
			ProtectedData d5 = new ProtectedData("Hello Josef!", 23);
			ProtectedData d6 = new ProtectedData("Hello World!", 2133L);
			assertFalse(d5.equals(d6));
		}
	}

}
