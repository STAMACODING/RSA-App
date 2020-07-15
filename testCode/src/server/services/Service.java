package server.services;

import com.stamacoding.rsaApp.log.logger.Logger;

/**
 * <p>A service offers the ability to run some code structured and repeatedly on a different thread.
 * To specify the code that should be run repeatedly override {@link #onRepeat()}. To run the service use {@link #launch()}.</p>
 * 
 * <p>Added to that the service offers the {@link #onStart()} method. 
 * This method is called once before the code in the {@link #onRepeat()} method is called repeatedly.
 * Override this method to initialize some stuff.</p>
 * 
 * <p>Moreover a service has the ability to get stopped safely. To stop a service use {@link #setStopRequested(boolean)}.
 * As the method's name tells by using this method you are only able to request the service to stop. 
 * This is because the service finishes its last run of the {@link #onRepeat()} method before it is really stopped. To do some stuff, when the service gets
 * stopped override the {@link #onStop()} method.</p>
 * 
 * <p>Another feature is that a service can report that it has crashed using the {@link #setServiceCrashed(boolean)} method. 
 * If the service reports that it has crashed, the service finishes its last run of the {@link #onRepeat()} method and calls the {@link #onCrash()}
 * method after that. To do some stuff, when the service gets stopped override the {@link #onCrash()} method.</p>
 * 
 * <p>One last thing a service can do is restarting itself when it has crashed. By default this feature
 * is enabled but you can disable it by using {@link #setRestartingOnCrash(boolean)}. To do some stuff,
 * when the service restarts override {@link #onRestart()}. You also can manually restart the service by calling {@link #restart()}.</p>
 */
public abstract class Service{
	
	/** The service's thread  */
	private volatile Thread servicesThread;
	
	/** Stores whether service should get stopped */
	private volatile boolean stopRequested = false;
	
	/** Stores whether the service crashed */
	private volatile boolean serviceCrashed = false;
	
	/** Stores whether the service restart feature is enabled */
	private volatile boolean restartingOnCrash = true;
	
	private final String serviceName;
	
	
	/**
	 * Creates an instance of a new service.
	 */
	protected Service(String serviceName) {
		this.serviceName = serviceName;
	}
	
	/**
	 * Launches the service.
	 */
	public final synchronized void launch() {
		if(getServicesThread() != null) {
			Logger.error(getServiceName(), "Service is already running. Use restart() to restart the service.");
			new RuntimeException("Service is already running. Use restart() to restart the service.").printStackTrace();
			return;
		}
		Thread servicesThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Logger.debug(getServiceName(), "Starting");
				onStart();
				Logger.debug(getServiceName(), "Started");
				while(!isStopRequested() && !isServiceCrashed()) {
					onRepeat();
				}
				if(isServiceCrashed()) {
					Logger.debug(getServiceName(), "Crashing");
					onCrash();
					Logger.debug(getServiceName(), "Crashed");
					if(isRestartingOnCrash()) {
						restart();
					}
				}else {
					Logger.debug(getServiceName(), "Stopping");
					onStop();
				}
			}
			
		});
		servicesThread.start();
	}
	
	/**
	 * Restarts the service.
	 */
	public final synchronized void restart() {
		Logger.debug(getServiceName(), "Restarting");
		if(getServicesThread() != null) {
			if(!getServicesThread().isInterrupted()) {
				getServicesThread().interrupt();
				Logger.debug(getServiceName(), "Interrupted service's thread to restart");
			}
			setServicesThread(null);
		}
		setStopRequested(false);
		setServiceCrashed(false);
		onRestart();
		Logger.debug(getServiceName(), "Launching service now...");
		launch();
	}
	
	
	/**
	 * Gets called once when the service starts.
	 */
	public void onStart() {}
	
	/**
	 * Gets called repeatedly after the {@link #onStart()} method was called.
	 */
	public void onRepeat() {}
	
	/**
	 * Gets called when the {@link #stopRequested} attribute is set to {@code true} and the service finished its last run of the {@link #onRepeat()} method.
	 */
	public void onStop() {}
	
	/**
	 * Gets called when the {@link #serviceCrashed} attribute is set to {@code true} and the service finished its last run of the {@link #onRepeat()} method.
	 */
	public void onCrash() {}
	
	/**
	 * Gets called when the service restarts (before the {@link #onStart()} method).
	 */
	public void onRestart() {}
	

	/**
	 * Gets the service's thread.
	 * @return the service's thread
	 */
	private Thread getServicesThread() {
		return servicesThread;
	}

	/**
	 * Sets the service's thread.
	 * @param servicesThread the service's thread
	 */
	private void setServicesThread(Thread servicesThread) {
		this.servicesThread = servicesThread;
	}

	/**
	 * Gets whether the service has crashed.
	 * @return whether the service has crashed
	 */
	public boolean isServiceCrashed() {
		return serviceCrashed;
	}

	/**
	 * Sets whether the service has crashed.
	 * @param serviceCrashed whether the service has crashed
	 */
	protected void setServiceCrashed(boolean serviceCrashed) {
		this.serviceCrashed = serviceCrashed;
	}

	/**
	 * Gets whether the service should stop.
	 * @return whether the service should stop
	 */
	public boolean isStopRequested() {
		return stopRequested;
	}

	/**
	 * Sets whether service should stop.
	 * @param stopRequested whether the service should stop
	 */
	public void setStopRequested(boolean stopRequested) {
		this.stopRequested = stopRequested;
	}

	/**
	 * Gets whether the service's restart-on-crash feature is enabled.
	 * @return whether the service's restart-on-crash feature is enabled
	 */
	public boolean isRestartingOnCrash() {
		return restartingOnCrash;
	}

	/**
	 * Enables or disables the service's restart-on-crash feature.
	 * @param restartingOnCrash whether the service's restart-on-crash feature should be enabled
	 */
	public void setRestartingOnCrash(boolean restartingOnCrash) {
		this.restartingOnCrash = restartingOnCrash;
	}

	/**
	 * Gets the service's name.
	 * @return the service's name
	 */
	public String getServiceName() {
		return serviceName;
	}
}

