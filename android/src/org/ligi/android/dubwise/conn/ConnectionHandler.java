package org.ligi.android.dubwise.conn;

import java.io.IOException;

import org.ligi.java.io.CommunicationAdapterInterface;

/**
 * takes a CommunicationAdapter, decides what kind of connection it is 
 * and provides the adapter and the conn type
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class ConnectionHandler {

	public final static byte CONNTYPE_NONE=0;
	public final static byte CONNTYPE_MK=1;
	public final static byte CONNTYPE_UAVTALK=2; 
	
	private static byte conn_type=CONNTYPE_NONE;
	
	private static CommunicationAdapterInterface comm_adapter=null;
	
	public static boolean isUAVTalkConnection() {
		return conn_type==CONNTYPE_UAVTALK;
	}
	
	public static boolean isMKConnection() {
		return conn_type==CONNTYPE_MK;
	}
		
	public static byte getConnectionType() {
		return conn_type;
	}

	/**
	 * sets which connection type is behind comm_adapter 
	 * and forwards the comm_adapter to the according connection handler
	 * 
	 * @param new_conn_type
	 */
	private static void setConnectionType(byte new_conn_type) {
		conn_type=new_conn_type;
		switch ( conn_type ) {
			case CONNTYPE_MK:
				MKProvider.getMK().setCommunicationAdapter(comm_adapter);
				break;
		}
	}
	   
	public static void setCommunicationAdapter(CommunicationAdapterInterface ca) {
		comm_adapter=ca;

		class ConnTypeChecker implements Runnable{
		
			CommunicationAdapterInterface my_ca;
			public ConnTypeChecker(CommunicationAdapterInterface ca) {
				my_ca=ca;
			}
			
			public void run() {
				long s_time= System.currentTimeMillis();
				while(s_time+10000>System.currentTimeMillis()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					if (my_ca.available()==0)
						ConnectionHandler.setConnectionType((byte)CONNTYPE_NONE);
					else
						ConnectionHandler.setConnectionType((byte)CONNTYPE_MK);
				} catch (IOException e) {}
			}
		}

		new Thread(new ConnTypeChecker(ca)).start();
	}
}
