package server.services;

import com.stamacoding.rsaApp.log.logger.Logger;

/**
 * <p>A service is basically a thread running some code until someone requests it to shutdown. 
 * To request a service to get shutdown use {@link requestShutdown()}.
 * To check if a service is still running use {@link #isRunning()}.</p>
 * <p>
 * To create your own service do the following:
 * </p>
 * <ol>
 * 	<li>Create a class extending from the {@link Service} class.</li>
 * 	<li>Implement following code at the top of your class. You should
 * 		do this because basically every service follows the 
 * 		<a href="https://de.wikibooks.org/wiki/Muster:_Java:_Singleton">singleton pattern</a>.
 * 		Remember to replace <code>CustomService</code> with your service's class name and <code>"custom-name"</code> with any
 * 		desired name of your service.
 * 	<br>
 * 	<pre><code>
 * 	private static volatile CustomService singleton = new SendService();
 *
 *	private CustomService() {
 *		super("custom-name");
 *	}
 *	
 *	public static CustomService getInstance() {
 *		return singleton;
 *	}
 *  </code></pre>
 *	</li>
 * 	<li>Override {@link #run()} to change what the service should do. Remember to call <code>super.run()</code> before doing your own stuff.</li>
 *  <li><ul>
 *  	<li>Remember also to implement the <b>shutdown-feature</b>. If {@link #isShutDownRequested()} returns <code>true</code> there service should get shutdown. E.g. you
 * 		can implement this feature by using <code>while(!requestedShutDown()){ ... }</code> instead of <code>while(true){ ... }</code>. This would automatically
 *		end the ongoing while loop if a shutdown gets requested. </li>
 *  </ul></li>
 *  <li>Now you can start your own service by calling <code>CustomService.getInstance().start()</code>. Easy, isn't it?</li>
 * </ol>
 */
public abstract class Service extends Thread{
	private volatile boolean requestedShutdown = false;
	private volatile boolean started = false;
	private volatile boolean manipulatedCrash = false;
	
	/**
	 * Creates an instance of a new service.
	 * @param serviceName the service's name
	 */
	protected Service(String serviceName) {
		super();
		setName(serviceName + "-service");
	}
	
	/**
	 * Starts the service.
	 */
	@Override
	public synchronized void start() {
		super.start();
	}
	
	/**
	 * Runs the service.
	 */
	@Override
	public void run() {
		super.run();
		this.started = true;
		this.requestedShutdown = false;
		Logger.debug(this.getClass().getSimpleName(), this.getName() + " is running");
	}
	
	/**
	 * Requests the service to get shutdown.
	 */
	public final void requestShutdown() {
		Logger.debug(this.getClass().getSimpleName(), getName() + " received shutdown request");
		this.requestedShutdown = true;
	}
	
	/**
	 * Checks if the server should get shutdown.
	 * @return whether the server should get shutdown
	 */
	public final boolean isShutDownRequested() {
		return requestedShutdown;
	}
	
	/**
	 * Checks if the service is running. Equivalent to {@link #isAlive()}.
	 * @return whether the service is running
	 */
	public boolean isRunning() {
		return this.isAlive();
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public boolean isCrashed() {
		return (isStarted() && !isRunning() && !isShutDownRequested()) || manipulatedCrash;
	}
	
	public void setCrashed() {
		manipulatedCrash = true;
	}
}

