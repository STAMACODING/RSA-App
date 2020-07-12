package server;

import java.io.Serializable;

public class Katze implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2966045277953461397L;
	
	
	int alter;
	String name;
	
	public Katze(int alter, String name) {
		this.alter = alter;
		this.name = name;
	}
	
	public static void main(String[] args) {
		Katze k = new Katze(43, "Günter");
		
		byte[] katzeAlsByteArray = Utils.Serialization.serialize(k);
		for(byte b : katzeAlsByteArray) {
			System.out.println(b);
		}
		
		k = (Katze) Utils.Serialization.deserialize(katzeAlsByteArray);
		
		System.out.println(k.name);
	}
}
