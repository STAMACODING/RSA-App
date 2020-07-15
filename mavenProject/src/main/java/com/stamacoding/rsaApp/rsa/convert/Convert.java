package com.stamacoding.rsaApp.rsa.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Convert {
	
	public static byte[] serialize(Object o) {
		 try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
		         ObjectOutput out = new ObjectOutputStream(bos)) {
		        out.writeObject(o);
		        return bos.toByteArray();
		    } catch (IOException e) {
				return null;
			} 

		
	}
	
	public static Object deserialize(byte[] serializedObject) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject);
		        ObjectInput in = new ObjectInputStream(bis)) {
		        return in.readObject();
		    } catch (IOException e) {
				return null;
			} catch (ClassNotFoundException e) {
				return null;
			} 
	}
	
	public static long[] byteArrayToLongArray(byte[] array) {
		long[] longArray = new long[array.length];
		
		for(int i=0; i<longArray.length; i++) {
			longArray[i] = (long) array[i];
		}
		
		return longArray;
	}
	
	public static byte[] longArrayToByteArray(long[] array) {
		byte[] byteArray = new byte[array.length];
		
		for(int i=0; i<byteArray.length; i++) {
			if(array[i] > Byte.MAX_VALUE) {
				throw new RuntimeException("Value is too large for byte-array!");
			}
			byteArray[i] = (byte) array[i];
		}
		
		return byteArray;
	}


	public static void main(String[] args) {
		Katze gunter = new Katze();
		gunter.alter = 100;
		byte[] gunterAlsByteArray = Convert.serialize(gunter);
		
		for(byte b : gunterAlsByteArray) {
			System.out.println(b);
		}
		
		gunter = (Katze) Convert.deserialize(gunterAlsByteArray);
		System.out.println(gunter.alter);
		System.out.println(gunter.name);
	}
}
