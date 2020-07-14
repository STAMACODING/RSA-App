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

/**
 *  Contains static functions that are useful for the server team.
 */
public class NetworkUtils {
	
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
	
	/**
	 * Serializes an object.
	 * @param o the object to serialize
	 * @return the serialized object
	 */
	public static byte[] serialize(Object o) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(out);
			os.writeObject(o);
		} catch (Exception e) {
			Logger.error(NetworkUtils.class.getSimpleName(), "Failed to serialize object!");
			return null;
		}
		return out.toByteArray();
	}
	
	/**
	 * Deserializes an object.
	 * @param object the serialized object
	 * @return the deserialized object
	 */
	public static Object deserialize(byte[] object) {
		ByteArrayInputStream in = new ByteArrayInputStream(object);
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(in);
			return is.readObject();
		} catch (Exception e) {
			Logger.error(NetworkUtils.class.getSimpleName(), "Failed to deserialize object!");
			return null;
		}
	}
}
