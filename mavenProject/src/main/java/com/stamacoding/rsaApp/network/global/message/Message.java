package com.stamacoding.rsaApp.network.global.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.stamacoding.rsaApp.log.logger.Logger;
import com.stamacoding.rsaApp.network.global.message.data.LocalData;
import com.stamacoding.rsaApp.network.global.message.data.ProtectedData;
import com.stamacoding.rsaApp.network.global.message.data.SendState;
import com.stamacoding.rsaApp.network.global.message.data.ServerData;
import com.stamacoding.rsaApp.security.Security;

/**
 *  An instance of this class represents a message with all its different attributes.
 */
public class Message implements Serializable{
	
	/** Auto-generated for serialization */
	private static final long serialVersionUID = 7382792471329161848L;
	
	/** The message's {@link LocalData} */
	private LocalData localData = new LocalData(-1, SendState.PENDING);
	
	/** The message's {@link ProtectedData} */
	private ProtectedData protectedData;
	
	/** The message's {@link ProtectedData} as encrypted byte array */
	private byte[] encryptedProtectedData;
	
	/** The message's {@link ServerData} */
	private ServerData serverData;
	
	/** The message's {@link ServerData} as encrypted byte array */
	private byte[] encryptedServerData;
	
	
	private Message() {}
	
	/**
	 * Creates a fully decoded message. All parameters are not allowed to be {@code null}.
	 * @param localData the message's local data
	 * @param protectedData the message's protected data
	 * @param serverData the message's server data
	 */
	public Message(LocalData localData, ProtectedData protectedData, ServerData serverData) {
		setLocalData(localData);
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
		setLocalData(localData);
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
		setLocalData(localData);
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
	private void setProtectedData(ProtectedData protectedData) {
		if(protectedData == null) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("ProtectedData protectedData is not allowed to be null!"));
		
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
	private void setServerData(ServerData serverData) {
		if(serverData == null) Logger.error(this.getClass().getSimpleName(),  new IllegalArgumentException("ServerData serverData is not allowed to be null!"));
		
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
	private void setEncryptedProtectedData(byte[] encryptedProtectedData) {
		if(encryptedProtectedData == null) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("byte[] encryptedProtectedData is not allowed to be null!"));
		
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
	private void setEncryptedServerData(byte[] encryptedServerData) {
		if(encryptedServerData == null) Logger.error(this.getClass().getSimpleName(), new IllegalArgumentException("byte[] encryptedServerData is not allowed to be null!"));
		
		this.encryptedServerData = encryptedServerData;
	}

	/**
	 * Encrypts the {@link #serverData}. {@link Message#getServerData()} returns {@link null} now while
	 * {@link Message#getEncryptedServerData()} returns a byte array.
	 */
	public void encryptServerData() {
		if(getServerData() == null) {
			Logger.warning(this.getClass().getSimpleName(), "ServerData is already encrypted!");
			return;
		}
		
		setEncryptedServerData(Security.encryptF(getServerData()));
		this.serverData = null;
	}
	
	/**
	 * Decrypts the {@link #serverData}. {@link Message#getEncryptedServerData()} returns {@link null} now while
	 * {@link Message#getServerData()} returns a reference to an object.
	 */
	public void decryptServerData() {
		if(getServerData() != null) {
			Logger.warning(this.getClass().getSimpleName(), "ServerData is already decrypted!");
			return;
		}
		
		setServerData((ServerData) Security.decryptF(getEncryptedServerData()));
		this.encryptedServerData = null;
	}
	
	/**
	 * Encrypts the {@link #protectedData}. {@link Message#getProtectedData()} returns {@link null} now while
	 * {@link Message#getEncryptedProtectedData()} returns a byte array.
	 */
	public void encryptProtectedData() {
		if(getProtectedData() == null) {
			Logger.warning(this.getClass().getSimpleName(), "ProtectedData is already encrypted!");
			return;
		}
		
		setEncryptedProtectedData(Security.encryptF(getProtectedData()));
		this.protectedData = null;
	}
	
	/**
	 * Decrypts the {@link #protectedData}. {@link Message#getEncryptedProtectedData()} returns {@link null} now while
	 * {@link Message#getProtectedData()} returns a reference to an object.
	 */
	public void decryptProtectedData() {
		if(getProtectedData() != null) {
			Logger.warning(this.getClass().getSimpleName(), "ProtectedData is already decrypted!");
			return;
		}
		
		setProtectedData((ProtectedData) Security.decryptF(getEncryptedProtectedData()));
		this.encryptedProtectedData = null;
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
			sb.append(getServerData().getSending());
			sb.append(") => (");
			sb.append(getServerData().getReceiving());
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

	private void setLocalData(LocalData localData) {
		if(localData == null) Logger.error(this.getClass().getSimpleName(),  new IllegalArgumentException("LocalData localData is not allowed to be null!"));
		
		this.localData = localData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(encryptedProtectedData);
		result = prime * result + Arrays.hashCode(encryptedServerData);
		result = prime * result + ((protectedData == null) ? 0 : protectedData.hashCode());
		result = prime * result + ((serverData == null) ? 0 : serverData.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (!Arrays.equals(encryptedProtectedData, other.encryptedProtectedData))
			return false;
		if (!Arrays.equals(encryptedServerData, other.encryptedServerData))
			return false;
		if (protectedData == null) {
			if (other.protectedData != null)
				return false;
		} else if (!protectedData.equals(other.protectedData))
			return false;
		if (serverData == null) {
			if (other.serverData != null)
				return false;
		} else if (!serverData.equals(other.serverData))
			return false;
		return true;
	}

	public void encrypt() {
		encryptProtectedData();
		encryptServerData();
	}
	
	public void decrypt() {
		decryptProtectedData();
		decryptServerData();
	}
	
	public boolean isEncrypted() {
		return getProtectedData() == null && getServerData() == null;
	}
	
	public boolean isStored() {
		return getLocalData().getId() > 0;
	}
	
	@Override
	public Message clone(){
		Message m = new Message();
		if(getEncryptedProtectedData() != null) m.setEncryptedProtectedData(getEncryptedProtectedData().clone());
		if(getEncryptedServerData() != null) m.setEncryptedServerData(getEncryptedServerData().clone());
		if(getLocalData() != null) m.setLocalData(getLocalData().clone());
		if(getProtectedData() != null) m.setProtectedData(getProtectedData().clone());
		if(getServerData() != null) m.setServerData(getServerData().clone());
		return m;
	}
	
	public static Message exampleMessage(boolean encrypted) {
		Message m = new Message();
		m.setLocalData(new LocalData(-1, SendState.PENDING));
		m.setProtectedData(new ProtectedData(randomString(1, 15), System.currentTimeMillis()));
		m.setServerData(new ServerData(randomString(1, 15), randomString(1, 15)));
		
		if(encrypted) {
			m.encrypt();
		}
		return m;
	}
	
	private static String randomString(int minLenght, int maxLenght) {
		int length = (int) ((Math.random() * (maxLenght - minLenght)) + minLenght);
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<length; i++) {
			char randomChar = (char) ((Math.random() * (125 - 65)) + 65);
			sb.append(randomChar);
		}
		return sb.toString();
	}
}
