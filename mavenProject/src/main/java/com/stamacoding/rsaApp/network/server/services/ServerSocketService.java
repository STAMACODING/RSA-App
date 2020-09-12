package com.stamacoding.rsaApp.network.server.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.network.global.service.InputOutputService;
import com.stamacoding.rsaApp.network.global.service.Service;

/**
 *  Abstract {@link Service} adapted to work with a {@link ServerSocket}. Use {@link #getServerSocket()} to
 *  gets the server's socket. Use {@link #getPort()} to get the server's port.
 */
public abstract class ServerSocketService extends InputOutputService{
	
	/** The service's {@link ServerSocket} */
	private ServerSocket serverSocket;
	
	private Socket clientSocket;
	
	/** The {@link #serverSocket}'s port. */
	private int port;
	
	/**
	 * Creates an instance of this class.
	 * @param port the {@link #serverSocket}'s port 
	 */
	protected ServerSocketService(String serviceName, int port) {
		super(serviceName);
		setPort(port);
	}

	/**
	 * Initializes a new {@link ServerSocket} with the given {@link #port}.
	 * @see Service#onStart()
	 */
	@Override
	public final void onStart() {
		try {
			setServerSocket(new ServerSocket(getPort()));
			Logger.debug(getServiceName(), "Successfully set server socket");
		} catch (IOException e) {
			Logger.error(getServiceName(), "Could not set server socket");
			setServiceCrashed(true);
		}
	}
	
	@Override
	public final void onRepeat() {
		super.onRepeat();
		try {
			setClientSocket(getServerSocket().accept());
			getClientSocket().setSoTimeout(5000);
			setInputStream(new DataInputStream(getClientSocket().getInputStream()));
			setOutputStream(new DataOutputStream(getClientSocket().getOutputStream()));
			Logger.debug(this.getServiceName(), "Accepted client connection");
			
			onAccept();
			
			getOutputStream().flush();
			getInputStream().close();
			getOutputStream().close();
			getClientSocket().close();
			Logger.debug(this.getServiceName(), "Closed client connection");
		} catch (IOException e) {
			Logger.error(this.getServiceName(), "Failed to accept connection from client");
			e.printStackTrace();
		}
	}

	protected abstract void onAccept();

	/**
	 * Closes the used {@link #serverSocket}.
	 * @see Service#onStop()
	 */
	@Override
	public final void onStop() {
		try {
			if(getServerSocket() != null) getServerSocket().close();
			Logger.debug(getServiceName(), "Closed server socket");
		} catch (IOException e) {
			Logger.error(getServiceName(), "Could not close server socket");
		}
	}

	/**
	 * Closes the used {@link #serverSocket}.
	 * @see Service#onCrash()
	 */
	@Override
	public final void onCrash() {
		try {
			if(getServerSocket() != null) getServerSocket().close();
			Logger.debug(getServiceName(), "Closed server socket");
		} catch (IOException e) {
			Logger.error(getServiceName(), "Could not close server socket");
		}
	}

	/**
	 * Gets the service's {@link ServerSocket}.
	 * @return the service's {@link ServerSocket}
	 */
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	/**
	 * Sets the service's {@link ServerSocket}.
	 * @param serverSocket the service's {@link ServerSocket}
	 */
	private void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	private void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * Gets the {@link #serverSocket}'s port.
	 * @return the {@link #serverSocket}'s port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the {@link #serverSocket}'s port.
	 * @param port the {@link #serverSocket}'s port.
	 */
	protected void setPort(int port) {
		this.port = port;
	}
}
