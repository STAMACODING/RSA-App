package com.stamacoding.rsaApp.server;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.stamacoding.rsaApp.server.exceptions.NullPointerException;

class NetworkUtilsTest {
	
	class InvalidObject{}

	@DisplayName("Test serialize()")
	@Nested
	class SerializeTest {
		
		@DisplayName("Test valid object")
		@Test
		void testValid(){
			long[] objectBefore = new long[]{23, 11, 3, 4, 54, 999, 1223323L, 23};
			byte[] serialized = null;
			assertDoesNotThrow(() -> { NetworkUtils.serialize(objectBefore); }); 
			serialized = NetworkUtils.serialize(objectBefore);
			
			assertNotNull(serialized);
		}
		
		@DisplayName("Test null object")
		@Test
		void testNull(){
			assertThrows(NullPointerException.class, () -> {NetworkUtils.serialize(null); });
		}
		
		@DisplayName("Test invalid object")
		@Test
		void testInvalid(){
			assertThrows(RuntimeException.class, () -> {NetworkUtils.serialize(new InvalidObject()); });
		}
	}

}
