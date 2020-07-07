package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.stamacoding.rsaApp.log.logger.Logger;

import server.message.Message;

public class Utils {
	
	public static class Ip{
		/**
		 * Gets the current device's local ip address.
		 * @return the current device's local ip address
		 */
		public static String getIpAdress() {
			try {
				return Inet4Address.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static class RSA{
		
		public static byte[] encode(String textMessage) {
			return new byte[] {23, 12, 111, 2, 11};
		}

		public static String decode(byte[] byteMessage) {
			return "Hallo Welt";
		}
		
	}
	
	public static class Serialization{
		public static byte[] serialize(Object o) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream os;
			try {
				os = new ObjectOutputStream(out);
				os.writeObject(o);
			} catch (Exception e) {
				Logger.error(Serialization.class.getSimpleName(), "Failed to serialize object!");
				return null;
			}
			return out.toByteArray();
		}
		
		public static Object deserialize(byte[] object) {
			ByteArrayInputStream in = new ByteArrayInputStream(object);
			ObjectInputStream is = null;
			try {
				is = new ObjectInputStream(in);
				return is.readObject();
			} catch (Exception e) {
				Logger.error(Serialization.class.getSimpleName(), "Failed to deserialize object!");
				return null;
			}
		}
	}
}
