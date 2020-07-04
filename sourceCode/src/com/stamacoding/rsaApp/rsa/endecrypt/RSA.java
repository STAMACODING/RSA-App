package com.stamacoding.rsaApp.rsa.endecrypt;

public class RSA {
	
	static String Text = "Sex Penis";
	static int e = 17, d = 145, n = 391;
	static long binaerzahl_1 = 0, binaerzahl_2 = 0;
	static char alphabet [] = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z', ' '};
	static long[] bloecke = new long [Text.length()];
	static byte[] encryptedData = new byte [Text.length()*2];
	
	public static void hashtagUmwandelnInBloecke () {
		
		for (int i = 0; i < Text.length(); i++) {
			for (int k = 0; k < 53; k++) {
				if (Text.charAt(i) == alphabet[k]) {				//Wenn Text = buchstaben im Alphabet
					bloecke[i] = k;									//Text in Zahlen umwandeln, die auf den Array bloecke gelegt werden
					k = 53;											//Abbruchbedingung
				}
			}
		}
		
	}
	
	public static void hashtagverschluesseln () {
		
		hashtagUmwandelnInBloecke();
		for (int i = 0; i < bloecke.length; i++) {					//Text.length() = bloecke.length

			bloecke[i] = schnellesPotenzieren(bloecke[i], e);			//schnelles Potenzieren
			String string = hashtagInBinaerZahlUmwandeln(bloecke[i]), string_1 = "", string_2 = "";	//in Bin�rzahl umwandeln
			for (int z = 0; z < 16; z++) {							//bloecke[i] halbieren in zwei gleich lange (8 Zeichen lang) Zeichenketten
				
				if (z < 8) {										//die ersten 8 Zeichen von Bloecke[i]
					string_1 = string_1 + string.charAt(z);			//auf string_1 speichern
				} else {											//die letzten 8 Zeichen von Bloecke[i]
					string_2 = string_2 + string.charAt(z);			//auf string_2 speichern
					
				}							
			}
			binaerzahl_1 = hashtagUmwandelnInDezimalzahl(string_1) - 128;	//Bin�rzahl in long-Format speichern
			binaerzahl_2 = hashtagUmwandelnInDezimalzahl(string_2) - 128;	//Bin�rzahl in long-Format speichern
			encryptedData[i*2] = (byte) (binaerzahl_1);				//Bin�rzahl in Byte-Array f�llen
			encryptedData[i*2+1] = (byte) (binaerzahl_2);			//Bin�rzahl in Byte-Array f�llen
			System.out.println("Bytes: " + encryptedData[i*2] + ", " + encryptedData[i*2+1]);
		}
	}
	
	public static void hashtagEntschluesseln () {
		
		String bizahl_1 = "";
		String bizahl_2 = "";
		String Nachricht = "";
		for (int i = 0; i < encryptedData.length/2; i++) {
			binaerzahl_1 = (long) (encryptedData[i*2]) + 128;
			binaerzahl_2 = (long) (encryptedData[i*2+1]) + 128;
			bizahl_1 = hashtagInBinaerZahlUmwandeln(binaerzahl_1);
			bizahl_2 = hashtagInBinaerZahlUmwandeln(binaerzahl_2);
			for (int x = 0;x<8;x++){
				bizahl_1 = bizahl_1 + bizahl_2.charAt(x+8);
			}
			System.out.println(bizahl_1);
			long bizahl_3 = hashtagUmwandelnInDezimalzahl(bizahl_1);
			System.out.println(bizahl_3);
			bizahl_3 = schnellesPotenzieren(bizahl_3, d);
			Nachricht += alphabet[(int) bizahl_3];
			System.out.println(Nachricht);
		}
		
	}
	
	public static String hashtagInBinaerZahlUmwandeln (long dezimalzahl) {
		
		String binaerzahl = "";
				
		dezimalzahl *= 2;
		do {																	//damit die n�chste Zeile nicht schon f�r den n�chsten Schleifendurchlauf arbeitet
			dezimalzahl = (dezimalzahl - (dezimalzahl % 2) ) / 2;				//man rechnet nicht den Rest sondern jeweils immer die �brig bleibende Zahl aus, z.B.: 12 / 6 = 2
			binaerzahl = Long.toString(dezimalzahl % 2) + binaerzahl;
		} while ((dezimalzahl - (dezimalzahl % 2) ) / 2 != 0);					//Schleife solange durchf�hren, bis �brig bleibende Zahl durch 2 gleich 0 ist
		
		String string  = binaerzahl;
		while (string.length() < 16) {							//Bedingung: L�nge von Bloecke[i] <= 16
			string = 0 + string;								//L�nge mit Nullen auf 16 Stellen f�llen
		}
		
		return string;											//R�ckgabe von Block[i] in Bin�rzahl
	}
	
	public static long hashtagUmwandelnInDezimalzahl(String x) {
		
		long ergebnis = 0;
		int number = x.length()-1;															//number ist die Potenz
		
		for (int la = 0; la < x.length(); la++) {
			long zwischenE = 0;													//zwischenE ist Ziffer von x potenziert mit number
			if (Long.parseLong("" + x.charAt(la)) == 1){						//gucken, ob Ziffer von x = 1 ist, ansonsten ist bei Umrechnung in Bin�rsystem sowieso an der Stelle nur 2^number * 0, weil x ja nur 0 oder 1 ist
				zwischenE = 1;
				for (int li = 0; li < number; li++) {
					zwischenE *= 2;												//zwischenE = eine Ziffer von x mit number potenzieren
				}
			}
			number--;															//Potenz(/number) senken, da die Zahl von links nach rechts betrachtet wird
			ergebnis += zwischenE;												//zwischenE auf abspeichern
		}																		//an Byte Array anpassen, geht von -128 bis 127
		return ergebnis;
		
	}
	
	public static long schnellesPotenzieren (long x , int exponent) {
		
		long ergebnis = x;
		
		for (int lz = 1; lz < exponent; lz++) {
			ergebnis = (ergebnis * x) % n;
		}
		return ergebnis;
	}

	public static void main(String[] args) {
		
		byte[] encryptData = new byte [2];
		encryptData[0] = 30;
		encryptData[1] = -107;
		hashtagverschluesseln();
		hashtagEntschluesseln();
	}
}