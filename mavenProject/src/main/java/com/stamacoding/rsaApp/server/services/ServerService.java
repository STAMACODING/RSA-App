package com.stamacoding.rsaApp.server.services;

import java.io.IOException;
import java.net.ServerSocket;

import com.stamacoding.rsaApp.log.logger.Logger;

/**
 *  Abstract {@link Service} adapted to work with a {@link ServerSocket}. Use {@link #getServerSocket()} to
 *  gets the server's socket. Use {@link #getPort()} to get the server's port.
 */
public abstract class ServerService extends Service{
	
	/** The service's {@link ServerSocket} */
	private ServerSocket serverSocket;
	
	/** The {@link #serverSocket}'s port. */
	private int port;
	
	/**
	 * Creates an instance of this class.
	 * @param port the {@link #serverSocket}'s port 
	 */
	protected ServerService(String serviceName, int port) {
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

	/**
	 * Closes the used {@link #serverSocket}.
	 * @see Service#onStop()
	 */
	@Override
	public final void onStop() {
		try {
			getServerSocket().close();
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
			getServerSocket().close();
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
	protected void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
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
