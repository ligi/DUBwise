package org.ligi.ufo;

public interface DUBwiseNotificationListenerInterface {

	public final static byte NOTIFY_CONNECTION_CHANGED=1;
	public final static byte NOTIFY_DISCONNECT=2;
	
	
	public void processNotification(byte notification);
}
