package org.ligi.android.dubwise;

import org.ligi.ufo.MKCommunicator;



public final class MKProvider  {

	static MKCommunicator mk=null;
	
	public static MKCommunicator getMK() {
		if (mk==null)
			mk=new MKCommunicator();
		
		return mk;
	}
}
