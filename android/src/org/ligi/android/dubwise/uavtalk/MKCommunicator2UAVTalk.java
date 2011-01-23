package org.ligi.android.dubwise.uavtalk;

public class MKCommunicator2UAVTalk {
	
	public static void init() {
		new Thread(new VesselData2UAVObjectsThread()).start();
	}
}
