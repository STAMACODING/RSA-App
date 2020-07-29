package com.stamacoding.rsaApp.rsa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.stamacoding.rsaApp.log.logger.Logger;

public class Convert {
	
	/**
	 * Serializes an object.
	 * @param o the object to serialize
	 * @return the serialized object
	 */
	public static byte[] serialize(Object o) {
		if(!(o instanceof Serializable)) {
			if(o != null) Logger.error(Convert.class.getSimpleName() + ":serialize", new IllegalArgumentException("Cannot serialize a not serializable object!"));
			else Logger.error(Convert.class.getSimpleName() + ":serialize", new IllegalArgumentException("Cannot serialize a null reference!"));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(out);
			os.writeObject(o);
		} catch (Exception e) {
			Logger.error(Convert.class.getSimpleName() + ":serialize", new RuntimeException("Failed to serialize object!"));
			return null;
		} finally {
			try {
				os.close();
				out.close();
			} catch (IOException e) {
				Logger.error(Convert.class.getSimpleName(), "Failed to close output stream.");
			}
		}
		return out.toByteArray();
	}
	
	/**
	 * Deserializes an object.
	 * @param object the serialized object
	 * @return the deserialized object
	 */
	public static Object deserialize(byte[] object) {
		if(object == null) Logger.error(Convert.class.getSimpleName(), new IllegalArgumentException("byte[] object is not allowed to be null!"));
		
		ByteArrayInputStream in = new ByteArrayInputStream(object);
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(in);
			Object o = is.readObject();
			if( o == null ) Logger.error(Convert.class.getSimpleName() + ":deserialize", new RuntimeException("Failed to deserialize object!"));
			return o;
		} catch (Exception e) {
			Logger.error(Convert.class.getSimpleName(), new RuntimeException("Failed to deserialize object!"));
			return null;
		} finally {
			try {
				is.close();
				in.close();
			} catch (IOException e) {
				Logger.error(Convert.class.getSimpleName(), "Failed to close input stream.");
			}
		}
	}
	
	public static long[] byteArrayToLongArray(byte[] array) {
		if(array == null) Logger.error(Convert.class.getSimpleName(), new IllegalArgumentException("byte[] array is not allowed to be null!"));
		
		long[] longArray = new long[array.length];
		
		for(int i=0; i<longArray.length; i++) {
			longArray[i] = (long) array[i];
		}
		
		return longArray;
	}
	
	public static byte[] longArrayToByteArray(long[] array) {
		if(array == null) Logger.error(Convert.class.getSimpleName(), new IllegalArgumentException("long[] array is not allowed to be null!"));
		
		byte[] byteArray = new byte[array.length];
		
		for(int i=0; i<byteArray.length; i++) {
			if(array[i] > Byte.MAX_VALUE) {
				throw new RuntimeException("Value is too large for byte-array!");
			}
			byteArray[i] = (byte) array[i];
		}
		
		return byteArray;
	}
}
