package com.stamacoding.rsaApp.rsa.keyCreate;

public class KeyPair {
	private Key priv;
	private Key publ;
	
	public KeyPair() {
		Key priv = new Key(0, 0);
		Key publ = new Key(0, 0);
		setPriv(priv);
		setPubl(publ);
	}

	public Key getPriv() {
		return priv;
	}
	public void setPriv(Key priv) {
		this.priv = priv;
	}
	public Key getPubl() {
		return publ;
	}
	public void setPubl(Key publ) {
		this.publ = publ;
	}
}
