package com.stamacoding.rsaApp.server.global.service;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class InputOutputService<In extends InputStream, Out extends OutputStream> extends Service{
	private In in;
	private Out out;

	protected InputOutputService(String serviceName) {
		super(serviceName);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	
	protected Out getOutputStream() {
		return out;
	}

	protected void setOutputStream(Out out) {
		this.out = out;
	}

	protected In getInputStream() {
		return in;
	}

	protected void setInputStream(In in) {
		this.in = in;
	}

}
