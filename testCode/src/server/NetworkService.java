package server;

import server.config.NetworkConfig;
import server.config.Type;
import server.services.Service;
import server.services.databaseServices.chatHistoryService.ChatHistoryService;
import server.services.databaseServices.storeService.StoreService;
import server.services.transferServices.receiveService.ReceiveService;
import server.services.transferServices.sendService.SendService;

public class NetworkService extends Service{
	private static volatile NetworkService singleton = new NetworkService();

	private NetworkService() {
		super("network-service");
	}
	
	public static NetworkService getInstance() {
		return singleton;
	}
	
	@Override
	public void run() {
		if(!NetworkConfig.isValid()) throw new RuntimeException("Invalid network configuration! Use NetworkConfig.setup() to fix");
		
		if(NetworkConfig.TYPE == Type.CLIENT) ChatHistoryService.getInstance().start();
		if(NetworkConfig.TYPE == Type.CLIENT) StoreService.getInstance().start();
		ReceiveService.getInstance().start();
		SendService.getInstance().start();
	}
	
	public static void main(String[] args) {
		NetworkConfig.setup(Type.CLIENT, (byte) 12, "127.0.0.1", 1001, 1002);
		NetworkService.getInstance().start();
	}

}
