package server.services.mainService;

import server.config.NetworkConfig;
import server.config.NetworkType;
import server.services.Service;
import server.services.databaseService.DatabaseService;
import server.services.transferServices.receiveService.ClientReceiveService;
import server.services.transferServices.receiveService.ServerReceiveService;
import server.services.transferServices.sendService.ClientSendService;
import server.services.transferServices.sendService.ServerSendService;

/**
 * <p>{@link Service} handling all message transfers. Additionally this service stores all messages in a chat database when
 * running on a client.</p>
 * Unites the work of these services when running on client: 
 * <ul>
 *  <li>{@link DatabaseService}</li>
 *  <li>{@link ClientReceiveService}</li>
 *  <li>{@link ClientSendService}</li>
 * </ul>
 * Unites the work of these services when running on server: 
 * <ul>
 *  <li>{@link ServerReceiveService}</li>
 *  <li>{@link ServerSendService}</li>
 * </ul>
 */
public class MessageService extends Service{
	
	/** The only instance of this class */
	private static volatile MessageService singleton = new MessageService();

	/**
	 *  Creates an instance of this class. Gets automatically called once at the start to define the service's {@link #singleton}. Use {@link MessageService#getInstance()} to get the
	 *  only instance of this class.
	 */
	private MessageService() {}
	
	/**
	 * Gets the only instance of this class.
	 * @return the only instance of this class
	 */
	public static MessageService getInstance() {
		return singleton;
	}

	/**
	 * Starts all services if the {@link NetworkConfig} is valid.
	 */
	@Override
	public void onStart() {
		if(!NetworkConfig.isValid()) throw new RuntimeException("Invalid network configuration! Use NetworkConfig.setup() to fix");
		
		if(NetworkConfig.TYPE == NetworkType.CLIENT){
			DatabaseService.getInstance().launch();
			ClientReceiveService.getInstance().launch();
			ClientSendService.getInstance().launch();
		}else {
			ServerReceiveService.getInstance().launch();
			ServerSendService.getInstance().launch();
		}
	}
	
	/**
	 * Stops all services.
	 */
	@Override
	public void onStop() {
		if(NetworkConfig.TYPE == NetworkType.CLIENT){
			DatabaseService.getInstance().setStopRequested(true);
			ClientReceiveService.getInstance().setStopRequested(true);
			ClientSendService.getInstance().setStopRequested(true);
		}else {
			ServerReceiveService.getInstance().setStopRequested(true);
			ServerSendService.getInstance().setStopRequested(true);
		}
	}
	
	public static void main(String[] args) {
		NetworkConfig.setup(NetworkType.CLIENT, (byte) 32, "192.234.122", 1001, 1002);
		MessageService.getInstance().launch();
	}
}
