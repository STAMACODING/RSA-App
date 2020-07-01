package server;

public interface MessageDBInterface {
	public void addMessageToDb(UnstoredMessage unstoredMessage); 
	public void updateMessage(Message updatedMessage);
	public Message[] getMessagesFromDb();
}
