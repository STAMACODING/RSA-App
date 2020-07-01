package server;

public interface MessageDBInterface {
	public void addMessageToDb(Message m); 
	public void updateMessage(Message mUpdatedMessage);
	public Message[] getMessagesFromDb();
}
