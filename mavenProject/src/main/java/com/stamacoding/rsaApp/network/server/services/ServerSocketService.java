package com.stamacoding.rsaApp.network.server.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.stamacoding.rsaApp.logger.L;
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
	protected ServerSocketService(int port) {
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
			L.d(getServiceClass(), "Successfully set server socket");
		} catch (IOException e) {
			L.e(getServiceClass(), "Could not set server socket", e);
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
			L.d(getServiceClass(), "Accepted client connection");
			
			onAccept();
			
			getOutputStream().flush();
			getInputStream().close();
			getOutputStream().close();
			getClientSocket().close();
			L.d(getServiceClass(), "Closed client connection");
		} catch (IOException e) {
			L.e(getServiceClass(), "Failed to accept connection from client", e);
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
			L.d(getServiceClass(), "Closed server socket");
		} catch (IOException e) {
			L.e(getServiceClass(), "Could not close server socket", e);
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
			L.d(getServiceClass(), "Closed server socket");
		} catch (IOException e) {
			L.e(getServiceClass(), "Could not close server socket", e);
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
