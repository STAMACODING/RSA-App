package com.stamacoding.rsaApp.server.message;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.stamacoding.rsaApp.rsa.RSA;
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
				Message m = new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), new ServerData("henri", "bene"));
				assertNotNull(m.getLocalData());
				assertNotNull(m.getProtectedData());
				assertNotNull(m.getServerData());
				assertNull(m.getEncryptedProtectedData());
				assertNull(m.getEncryptedServerData());
			});
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(2, SendState.SENT), new ProtectedData("Testnachricht", 11L), new ServerData("timo", "bene"));
				assertNotNull(m.getLocalData());
				assertNotNull(m.getProtectedData());
				assertNotNull(m.getServerData());
				assertNull(m.getEncryptedProtectedData());
				assertNull(m.getEncryptedServerData());
			});
			
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), new ServerData("henri", "hendrik"));
				assertNotNull(m.getLocalData());
				assertNull(m.getProtectedData());
				assertNotNull(m.getServerData());
				assertNotNull(m.getEncryptedProtectedData());
				assertNull(m.getEncryptedServerData());
			});
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(2, SendState.SENT), RSA.encryptF(new ProtectedData("Testnachricht", 11L)), new ServerData("patrick", "bene"));
				assertNotNull(m.getLocalData());
				assertNull(m.getProtectedData());
				assertNotNull(m.getServerData());
				assertNotNull(m.getEncryptedProtectedData());
				assertNull(m.getEncryptedServerData());
			});
			
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), RSA.encryptF(new ServerData("sami", "dave")));
				assertNotNull(m.getLocalData());
				assertNull(m.getProtectedData());
				assertNull(m.getServerData());
				assertNotNull(m.getEncryptedProtectedData());
				assertNotNull(m.getEncryptedServerData());
			});
			assertDoesNotThrow(() -> {
				Message m = new Message(new LocalData(2, SendState.SENT), RSA.encryptF(new ProtectedData("Testnachricht", 11L)), RSA.encryptF(new ServerData("josef", "günter")));
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
			assertThrows(IllegalArgumentException.class, () -> {new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), null);});
			assertThrows(IllegalArgumentException.class, () -> {new Message(null, new ProtectedData("Hi Henri!", 2332L), new ServerData("josef", "günter"));});
			assertThrows(IllegalArgumentException.class, () -> {new Message(null, new ProtectedData("Hi Henri!", 2332L), null);});
			
			assertThrows(IllegalArgumentException.class, () -> {new Message(new LocalData(23, SendState.PENDING), null, RSA.encryptF(new ServerData("josef", "günter")));});
			assertThrows(IllegalArgumentException.class, () -> {new Message(null, RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), RSA.encryptF(new ServerData("josef", "günter")));});
			assertThrows(IllegalArgumentException.class, () -> {new Message(null, null, RSA.encryptF(new ServerData("josef", "günter")));});
			
			assertThrows(IllegalArgumentException.class, () -> {new Message(null, RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), new ServerData("josef", "günter"));});
		}
		
	}
	
	
	@DisplayName("Test encryptProtectedData() and decryptProtectedData()")
	@Nested
	class ProtectedDataRSATest{
		
		@DisplayName("Test encryptProtectedData()")
		@Test
		void testEncryptProtectedData() {
			Message m = new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), new ServerData("josef", "günter"));
			assertDoesNotThrow(() -> {m.encryptProtectedData();});
			
			assertNotNull(m.getEncryptedProtectedData());
			assertNull(m.getProtectedData());
		}
		
		@DisplayName("Test decryptProtectedData()")
		@Test
		void testDecryptProtectedData() {
			ProtectedData p = new ProtectedData("Hi Henri!", 2332L);
			Message m = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(p), new ServerData("josef", "günter"));
			
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
			Message m = new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), new ServerData("josef", "günter"));
			assertDoesNotThrow(() -> {m.encryptServerData();;});
			
			assertNotNull(m.getEncryptedServerData());
			assertNull(m.getServerData());
		}
		
		@DisplayName("Test decryptServerData()")
		@Test
		void testDecryptProtectedData() {
			ServerData d = new ServerData("josef", "günter");
			Message m = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), RSA.encryptF(d));
			
			assertDoesNotThrow(() -> {m.decryptServerData();});
			
			assertNull(m.getEncryptedServerData());
			assertNotNull(m.getServerData());
			
			assertEquals(d, m.getServerData());
		}
	}
	
	@DisplayName("Test equals()")
	@Nested
	class EqualityTest{
		
		@DisplayName("Test equal objects")
		@Test
		void testEqual() {
			Message d1 = new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), new ServerData("josef", "günter"));
			Message d2 = new Message(new LocalData(23, SendState.PENDING), new ProtectedData("Hi Henri!", 2332L), new ServerData("josef", "günter"));
			assertTrue(d2.equals(d1));
			
			Message d3 = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), RSA.encryptF(new ServerData("tim", "günter")));
			Message d4 = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), RSA.encryptF(new ServerData("tim", "günter")));
			assertTrue(d3.equals(d4));
			
			Message d5 = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), new ServerData("anette", "we"));
			Message d6 = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), new ServerData("anette", "we"));
			assertTrue(d5.equals(d6));
		}
		
		@DisplayName("Test not equal objects")
		@Test
		void testNotEqual() {
			Message d1 = new Message(new LocalData(23, SendState.SENT), new ProtectedData("Hi Henri!", 23232L), new ServerData("josef", "günter"));
			Message d2 = new Message(new LocalData(2, SendState.PENDING), new ProtectedData("Hi He22nri!", 2332L), new ServerData("günter", "josef"));
			assertFalse(d2.equals(d1));
			
			Message d3 = new Message(new LocalData(13, SendState.SENT), RSA.encryptF(new ProtectedData("Hi Henri!", 2332L)), RSA.encryptF(new ServerData("a", "b")));
			Message d4 = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(new ProtectedData("Hi Hesdfsdfnri!", 2332L)), RSA.encryptF(new ServerData("a", "b")));
			assertFalse(d3.equals(d4));
			
			Message d5 = new Message(new LocalData(23, SendState.PENDING), RSA.encryptF(new ProtectedData("Hi dsfsf!", 232232L)), new ServerData("aa", "bb2"));
			Message d6 = new Message(new LocalData(33, SendState.SENT), RSA.encryptF(new ProtectedData("Hi Hsdfsdfdfenri!", 211332L)), new ServerData("ssds2", "sd"));
			assertFalse(d5.equals(d6));
		}
	}

}
