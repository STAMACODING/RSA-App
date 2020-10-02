package com.stamacoding.rsaApp.network.client.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.stamacoding.rsaApp.logger.L;
import com.stamacoding.rsaApp.network.global.NetworkUtils;
import com.stamacoding.rsaApp.network.global.service.InputOutputService;

public abstract class ClientSocketService extends InputOutputService{
	private Socket socketConnection;
	private int port;
	private String serverIp;

	protected ClientSocketService(String serviceName, String serverIp, int port) {
		setServerIp(serverIp);
		setPort(port);
	}

	@Override
	public void onRepeat() {
		super.onRepeat();
		try {
			setSocketConnection(new Socket(getServerIp(), getPort()));
			getSocketConnection().setSoTimeout(5000);
			setOutputStream(new DataOutputStream(getSocketConnection().getOutputStream()));
			setInputStream(new DataInputStream(getSocketConnection().getInputStream()));
			
			L.d(this.getClass(), "Connected to server (" + getServerIp() + " : " + getPort() + ")");
			
			onAccept();
			
			getOutputStream().flush();
			getOutputStream().close();
			getInputStream().close();
			getSocketConnection().close();
			
			L.d(this.getClass(), "Closed connection to server");
		} catch (IOException e) {
			L.e(this.getClass(), "Failed to connect to server", e);
		}
	}

	protected abstract void onAccept();

	public Socket getSocketConnection() {
		return socketConnection;
	}

	private void setSocketConnection(Socket serverConnection) {
		this.socketConnection = serverConnection;
	}

	public int getPort() {
		return port;
	}

	private void setPort(int port) {
		if(port <= 0) L.f(this.getClass(), new IllegalArgumentException("The port is not allowed to be smaller than 0!"));
		this.port = port;
	}

	public String getServerIp() {
		return serverIp;
	}

	private void setServerIp(String serverIp) {
		if(!NetworkUtils.isValidInet4Address(serverIp)) L.f(this.getClass(), new IllegalArgumentException("Invalid server ip!"));
		this.serverIp = serverIp;
	}

}
