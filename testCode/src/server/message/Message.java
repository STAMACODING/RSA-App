package server.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import server.message.data.LocalData;
import server.message.data.ProtectedData;
import server.message.data.ServerData;

/**
 * An instance of this class represents a message with all its different attributes.
 *
 */
public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7382792471329161848L;
	/**
	 * 
	 */
	private final LocalData localData;
	
	/**
	 * This data cannot be decoded by the server.
	 */
	private ProtectedData messageData;
	private byte[] encodedMessageData;
	
	/**
	 * This data can be decoded by the server.
	 */
	private ServerData messageMeta;
	private byte[] encodedMessageMeta;
	
	
	public Message(LocalData localData, ProtectedData messageData, ServerData messageMeta) {
		if(messageData == null || messageMeta == null || localData == null) throw new IllegalArgumentException("LocalData/ProtectedData/ServerData are not allowed to be null!");
		this.localData = localData;
		setMessageData(messageData);
		setMessageMeta(messageMeta);
	}
	
	public Message(LocalData localData, byte[] encodedMessageData, byte[] encodedMessageMeta) {
		if(messageData == null || messageMeta == null || localData == null) throw new IllegalArgumentException("LocalData/ProtectedData/ServerData are not allowed to be null!");
		this.localData = localData;
		setEncodedMessageData(encodedMessageData);
		setEncodedMessageMeta(encodedMessageMeta);
	}
	
	public Message(LocalData localData, byte[] encodedMessageData, ServerData messageMeta) {
		if(messageData == null || messageMeta == null || localData == null) throw new IllegalArgumentException("LocalData/ProtectedData/ServerData are not allowed to be null!");
		this.localData = localData;
		setEncodedMessageData(encodedMessageData);
		setMessageMeta(messageMeta);
	}


	public ProtectedData getMessageData() {
		return messageData;
	}

	public ServerData getMessageMeta() {
		return messageMeta;
	}

	public byte[] getEncodedMessageData() {
		return encodedMessageData;
	}

	private void setEncodedMessageData(byte[] encodedMessageData) {
		this.encodedMessageData = encodedMessageData;
	}

	public byte[] getEncodedMessageMeta() {
		return encodedMessageMeta;
	}

	private void setEncodedMessageMeta(byte[] encodedMessageMeta) {
		this.encodedMessageMeta = encodedMessageMeta;
	}

	private void setMessageData(ProtectedData messageData) {
		this.messageData = messageData;
	}

	private void setMessageMeta(ServerData messageMeta) {
		this.messageMeta = messageMeta;
	}
	
	public LocalData getLocalData() {
		return localData;
	}

	public void encodeMessageMeta() {
		setEncodedMessageMeta(ServerData.encode(getMessageMeta()));
		setMessageMeta(null);
	}
	
	public void decodeMessageMeta() {
		setMessageMeta(ServerData.decode(getEncodedMessageMeta()));
		setEncodedMessageMeta(null);
	}
	
	public void encodeMessageData() {
		setEncodedMessageData(ProtectedData.encode(getMessageData()));
		setMessageData(null);
	}
	
	public void decodeMessageData() {
		setMessageData(ProtectedData.decode(getEncodedMessageData()));
		setEncodedMessageData(null);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(getLocalData().getId());
		sb.append("] (");
		if(getMessageMeta() != null) {
			sb.append(getMessageMeta().getSendingId());
			sb.append(") => (");
			sb.append(getMessageMeta().getReceivingId());
			sb.append("): \"");
		}else{
			sb.append("?) => (?): \"");
		}
		if(getMessageData() != null) {
			sb.append(getMessageData().getTextMessage());
			sb.append("\" (created at ");
			sb.append(new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(getMessageData().getDate()));
			sb.append(")");
		}else {
			sb.append("????????\" (created at ????)");
		}
		sb.append(" [");
		sb.append(getLocalData().getSendState().toString());
		sb.append("]");
		return sb.toString();
	}
}
