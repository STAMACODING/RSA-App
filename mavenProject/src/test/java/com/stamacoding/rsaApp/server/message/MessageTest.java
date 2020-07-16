package com.stamacoding.rsaApp.server.message;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.stamacoding.rsaApp.server.exceptions.NullPointerException;
import com.stamacoding.rsaApp.server.message.data.LocalData;
import com.stamacoding.rsaApp.server.message.data.ProtectedData;
import com.stamacoding.rsaApp.server.message.data.SendState;
import com.stamacoding.rsaApp.server.message.data.ServerData;

class MessageTest {

	@DisplayName("Test message's constructors")
	@Nested
	class ConstructorTest{
		
		@DisplayName("Test with valid arguments")
		@Test
		void testWithValidArguments() {
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), new ServerData((byte) 23, (byte) 11));
				assertNotNull(m.getLocalData());
				assertNotNull(m.getProtectedData());
				assertNotNull(m.getServerData());
				assertNull(m.getEncryptedProtectedData());
				assertNull(m.getEncryptedServerData());
			});
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(2, SendState.SENT), new ProtectedData("Testnachricht", 11L), new ServerData((byte) 1, (byte) 99));
				assertNotNull(m.getLocalData());
				assertNotNull(m.getProtectedData());
				assertNotNull(m.getServerData());
				assertNull(m.getEncryptedProtectedData());
				assertNull(m.getEncryptedServerData());
			});
			
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(23, SendState.PENDING), ProtectedData.encrypt(new ProtectedData("Hi Henri!", 2332L)), new ServerData((byte) 23, (byte) 11));
				assertNotNull(m.getLocalData());
				assertNull(m.getProtectedData());
				assertNotNull(m.getServerData());
				assertNotNull(m.getEncryptedProtectedData());
				assertNull(m.getEncryptedServerData());
			});
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(2, SendState.SENT), ProtectedData.encrypt(new ProtectedData("Testnachricht", 11L)), new ServerData((byte) 1, (byte) 99));
				assertNotNull(m.getLocalData());
				assertNull(m.getProtectedData());
				assertNotNull(m.getServerData());
				assertNotNull(m.getEncryptedProtectedData());
				assertNull(m.getEncryptedServerData());
			});
			
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(23, SendState.PENDING), ProtectedData.encrypt(new ProtectedData("Hi Henri!", 2332L)), ServerData.encrypt(new ServerData((byte) 23, (byte) 11)));
				assertNotNull(m.getLocalData());
				assertNull(m.getProtectedData());
				assertNull(m.getServerData());
				assertNotNull(m.getEncryptedProtectedData());
				assertNotNull(m.getEncryptedServerData());
			});
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(2, SendState.SENT), ProtectedData.encrypt(new ProtectedData("Testnachricht", 11L)), ServerData.encrypt(new ServerData((byte) 1, (byte) 99)));
				assertNotNull(m.getLocalData());
				assertNull(m.getProtectedData());
				assertNull(m.getServerData());
				assertNotNull(m.getEncryptedProtectedData());
				assertNotNull(m.getEncryptedServerData());
			});
		}
		
		@DisplayName("Test with invalid arguments")
		@Test
		void testWithInvalidArguments() {
			assertThrows(NullPointerException.class, () -> {new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), null);});
			assertThrows(NullPointerException.class, () -> {new Message(null, new ProtectedData("Hi Henri!", 2332L), new ServerData((byte) 23, (byte) 11));});
			assertThrows(NullPointerException.class, () -> {new Message(null, new ProtectedData("Hi Henri!", 2332L), null);});
			
			assertThrows(NullPointerException.class, () -> {new Message(new LocalData(23, SendState.PENDING), null, ServerData.encrypt(new ServerData((byte) 23, (byte) 11)));});
			assertThrows(NullPointerException.class, () -> {new Message(null, ProtectedData.encrypt(new ProtectedData("Hi Henri!", 2332L)), ServerData.encrypt(new ServerData((byte) 23, (byte) 11)));});
			assertThrows(NullPointerException.class, () -> {new Message(null, null, ServerData.encrypt(new ServerData((byte) 23, (byte) 11)));});
			
			assertThrows(NullPointerException.class, () -> {new Message(null, ProtectedData.encrypt(new ProtectedData("Hi Henri!", 2332L)), new ServerData((byte) 23, (byte) 11));});
		}
		
	}
	
	
	@DisplayName("Test encryptProtectedData() and decryptProtectedData()")
	@Nested
	class ProtectedDataRSATest{
		
		@DisplayName("Test encryptProtectedData()")
		@Test
		void testEncryptProtectedData() {
			Message m = new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), new ServerData((byte) 23, (byte) 11));
			assertDoesNotThrow(() -> {m.encryptProtectedData();});
			
			assertNotNull(m.getEncryptedProtectedData());
			assertNull(m.getProtectedData());
		}
		
		@DisplayName("Test decryptProtectedData()")
		@Test
		void testDecryptProtectedData() {
			ProtectedData p = new ProtectedData("Hi Henri!", 2332L);
			Message m = new Message(new LocalData(23, SendState.PENDING), ProtectedData.encrypt(p), new ServerData((byte) 23, (byte) 11));
			
			assertDoesNotThrow(() -> {m.decryptProtectedData();});
			
			assertNull(m.getEncryptedProtectedData());
			assertNotNull(m.getProtectedData());
			
			assertEquals(p, m.getProtectedData());
		}
	}
	
	@DisplayName("Test encryptServerData() and decryptServerData()")
	@Nested
	class ServerDataRSATest{
		
		@DisplayName("Test encryptServerData()")
		@Test
		void testEncryptProtectedData() {
			Message m = new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), new ServerData((byte) 23, (byte) 11));
			assertDoesNotThrow(() -> {m.encryptServerData();;});
			
			assertNotNull(m.getEncryptedServerData());
			assertNull(m.getServerData());
		}
		
		@DisplayName("Test decryptServerData()")
		@Test
		void testDecryptProtectedData() {
			ServerData d = new ServerData((byte) 68, (byte) 43);
			Message m = new Message(new LocalData(23, SendState.PENDING), ProtectedData.encrypt(new ProtectedData("Hi Henri!", 2332L)), ServerData.encrypt(d));
			
			assertDoesNotThrow(() -> {m.decryptServerData();});
			
			assertNull(m.getEncryptedServerData());
			assertNotNull(m.getServerData());
			
			assertEquals(d, m.getServerData());
		}
	}

}
