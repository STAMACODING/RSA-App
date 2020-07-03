package server;

import server.config.NetworkConfig;
import server.config.Type;
import server.services.Service;
import server.services.databaseServices.chatHistoryService.ChatHistoryService;
import server.services.databaseServices.storeService.StoreService;
import server.services.transferServices.receiveService.ReceiveService;
import server.services.transferServices.sendService.SendService;

/**
 * <p>{@link Service} handling all message transfers. Additionally this service stores all messages in a chat database when
 * running on a client.</p>
 * Unites the work of these services: 
 * <ul>
 * 	<li>{@link ChatHistoryService} (client)</li>
 *  <li>{@link StoreService} (client)</li>
 *  <li>{@link ReceiveService} (server, client)</li>
 *  <li>{@link SendService} (server, client)</li>
 * </ul>
 */
public class NetworkService extends Service{
	private static volatile NetworkService singleton = new NetworkService();

	private NetworkService() {
		super("network");
	}
	
	public static NetworkService getInstance() {
		return singleton;
	}
	
	/**
	 * Starts the {@link NetworkService}. The service will only stay alive if your {@link NetworkConfig} is valid.
	 */
	@Override
	public synchronized void start() {
		super.start();
	}
	
	/**
	 * Runs the {@link NetworkService}. The service will only stay alive if your {@link NetworkConfig} is valid.
	 */
	@Override
	public void run() {
		if(!NetworkConfig.isValid()) throw new RuntimeException("Invalid network configuration! Use NetworkConfig.setup() to fix");
		
		if(NetworkConfig.TYPE == Type.CLIENT) ChatHistoryService.getInstance().start();
		if(NetworkConfig.TYPE == Type.CLIENT) StoreService.getInstance().start();
		ReceiveService.getInstance().start();
		SendService.getInstance().start();
		
		while(!requestedShutDown()) {}
		
		SendService.getInstance().requestShutdown();
		ReceiveService.getInstance().requestShutdown();
		if(NetworkConfig.TYPE == Type.CLIENT) StoreService.getInstance().requestShutdown();
		if(NetworkConfig.TYPE == Type.CLIENT) ChatHistoryService.getInstance().requestShutdown();
	}
	
	public static void main(String[] args) {
		NetworkConfig.setup(Type.CLIENT, (byte) 12, "127.0.0.1", 1001, 1002);
		NetworkService.getInstance().start();
	}

}
