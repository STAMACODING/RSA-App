package server.services;

import com.stamacoding.rsaApp.log.logger.Logger;

public abstract class Service extends Thread{
	private volatile boolean requestedShutdown = false;
	
	protected Service(String serviceName) {
		super();
		setName(serviceName);
	}
	
	@Override
	public void run() {
		super.run();
		Logger.debug(this.getClass().getSimpleName(), this.getName() + " is running");
	}
	
	public final void requestShutdown() {
		this.requestedShutdown = true;
	}
	
	public final boolean requestedShutDown() {
		return requestedShutdown;
	}
	
	public boolean isRunning() {
		return this.isAlive();
	}
}

