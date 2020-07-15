package server.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import server.message.data.LocalData;
import server.message.data.ProtectedData;
import server.message.data.ServerData;

/**
 *  An instance of this class represents a message with all its different attributes.
 */
public class Message implements Serializable{
	
	/** Auto-generated for serialization */
	private static final long serialVersionUID = 7382792471329161848L;
	
	/** The message's {@link LocalData} */
	private final LocalData localData;
	
	/** The message's {@link ProtectedData} */
	private ProtectedData protectedData;
	
	/** The message's {@link ProtectedData} as encrypted byte array */
	private byte[] encryptedProtectedData;
	
	
	/** The message's {@link ServerData} */
	private ServerData serverData;
	
	/** The message's {@link ServerData} as encrypted byte array */
	private byte[] encryptedServerData;
	
	
	/**
	 * Creates a fully decoded message. All parameters are not allowed to be {@code null}.
	 * @param localData the message's local data
	 * @param protectedData the message's protected data
	 * @param serverData the message's server data
	 */
	public Message(LocalData localData, ProtectedData protectedData, ServerData serverData) {
		if(protectedData == null || serverData == null || localData == null) throw new IllegalArgumentException("LocalData/ProtectedData/ServerData are not allowed to be null!");
		this.localData = localData;
		setProtectedData(protectedData);
		setServerData(serverData);
	}
	
	/**
	 * Creates a fully encrypted message. All parameters are not allowed to be {@code null}.
	 * @param localData the message's local data (cannot be encoded)
	 * @param encryptedProtectedData the message's protected data as encrypted byte array
	 * @param encryptedServerData the message's server data as encrypted byte array
	 */
	public Message(LocalData localData, byte[] encryptedProtectedData, byte[] encryptedServerData) {
		if(encryptedProtectedData == null || encryptedServerData == null || localData == null) throw new IllegalArgumentException("LocalData/ProtectedData/ServerData are not allowed to be null!");
		this.localData = localData;
		setEncryptedProtectedData(encryptedProtectedData);
		setEncryptedServerData(encryptedServerData);
	}
	
	/**
	 * Creates a semi-encrypted message. Only the protected data is encrypted. All parameters are not allowed to be {@code null}.
	 * @param localData the message's local data
	 * @param encryptedProtectedData the message's protected data as encrypted byte array
	 * @param serverData the message's server data
	 */
	public Message(LocalData localData, byte[] encryptedProtectedData, ServerData serverData) {
		if(encryptedProtectedData == null || serverData == null || localData == null) throw new IllegalArgumentException("LocalData/ProtectedData/ServerData are not allowed to be null!");
		this.localData = localData;
		setEncryptedProtectedData(encryptedProtectedData);
		setServerData(serverData);
	}

	/**
	 * Gets the message's {@link ProtectedData}.
	 *
	 * @return the message's protected data
	 */
	public ProtectedData getProtectedData() {
		return protectedData;
	}
	
	/**
	 * Sets the message's {@link ProtectedData}.
	 * @param serverData the message's {@link ProtectedData}
	 */
	public void setProtectedData(ProtectedData protectedData) {
		this.protectedData = protectedData;
	}


	/**
	 * Gets the message's {@link ServerData}.
	 *
	 * @return the message's server data
	 */
	public ServerData getServerData() {
		return serverData;
	}
	
	/**
	 * Sets the message's {@link ServerData}.
	 * @param serverData the message's {@link ServerData}
	 */
	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}
	
	/**
	 * Gets the message's {@link LocalData}.
	 *
	 * @return the message's {@link LocalData}
	 */
	public LocalData getLocalData() {
		return localData;
	}

	/**
	 * Gets the message's {@link ProtectedData} as encrypted byte array.
	 * @return the message's {@link ProtectedData} as encrypted byte array
	 */
	public byte[] getEncryptedProtectedData() {
		return encryptedProtectedData;
	}

	/**
	 * Sets the message's encrypted byte array that represents the {@link #protectedData}.
	 * @param encryptedProtectedData the message's encrypted byte array that represents the {@link #protectedData}
	 */
	public void setEncryptedProtectedData(byte[] encryptedProtectedData) {
		this.encryptedProtectedData = encryptedProtectedData;
	}

	/**
	 * Gets the message's {@link ServerData} as encrypted byte array.
	 * @return the message's {@link ServerData} as encrypted byte array
	 */
	public byte[] getEncryptedServerData() {
		return encryptedServerData;
	}

	/**
	 * Sets the message's encrypted byte array that represents the {@link #serverData}.
	 * @param encryptedServerData the message's encrypted byte array that represents the {@link #serverData}
	 */
	public void setEncryptedServerData(byte[] encryptedServerData) {
		this.encryptedServerData = encryptedServerData;
	}

	/**
	 * Encrypts the {@link #serverData}. {@link Message#getServerData()} returns {@link null} now while
	 * {@link Message#getEncryptedServerData()} returns a byte array.
	 */
	public void encryptServerData() {
		setEncryptedServerData(ServerData.encrypt(getServerData()));
		setServerData(null);
	}
	
	/**
	 * Decrypts the {@link #serverData}. {@link Message#getEncryptedServerData()} returns {@link null} now while
	 * {@link Message#getServerData()} returns a reference to an object.
	 */
	public void decryptServerData() {
		setServerData(ServerData.decrypt(getEncryptedServerData()));
		setEncryptedServerData(null);
	}
	
	/**
	 * Encrypts the {@link #protectedData}. {@link Message#getProtectedData()} returns {@link null} now while
	 * {@link Message#getEncryptedProtectedData()} returns a byte array.
	 */
	public void encryptProtectedData() {
		setEncryptedProtectedData(ProtectedData.encrypt(getProtectedData()));
		setProtectedData(null);
	}
	
	/**
	 * Decrypts the {@link #protectedData}. {@link Message#getEncryptedProtectedData()} returns {@link null} now while
	 * {@link Message#getProtectedData()} returns a reference to an object.
	 */
	public void decryptProtectedData() {
		ProtectedData d = ProtectedData.decrypt(getEncryptedProtectedData());
		System.err.println(d);
		setProtectedData(d);
		setEncryptedProtectedData(null);
	}
	
	/**
	 * Returns the message as string.
	 * @return the message as string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(getLocalData().getId());
		sb.append("] (");
		if(getServerData() != null) {
			sb.append(getServerData().getSendingId());
			sb.append(") => (");
			sb.append(getServerData().getReceivingId());
			sb.append("): \"");
		}else{
			sb.append("?) => (?): \"");
		}
		if(getProtectedData() != null) {
			sb.append(getProtectedData().getTextMessage());
			sb.append("\" (created at ");
			sb.append(new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(getProtectedData().getDate()));
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
