package com.stamacoding.rsaApp.server.global.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class InputOutputService extends Service{
	private DataInputStream in;
	private DataOutputStream out;

	protected InputOutputService(String serviceName) {
		super(serviceName);
	}
	
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
