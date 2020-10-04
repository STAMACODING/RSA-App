package com.stamacoding.rsaApp.network.global.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class IOService extends Service{
	private DataInputStream in;
	private DataOutputStream out;

	protected IOService() {}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	
	protected DataOutputStream getOutputStream() {
		return out;
	}

	protected void setOutputStream(DataOutputStream out) {
		this.out = out;
	}

	protected DataInputStream getInputStream() {
		return in;
	}

	protected void setInputStream(DataInputStream in) {
		this.in = in;
	}

}
