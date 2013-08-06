package org.ligi.android.dubwise_mk.app;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.ufo.MKCommunicator;

import android.app.Application;

public class ApplicationContext extends Application {

	@Override
	public void onCreate() {
		
		super.onCreate();
	}
	
	public MKCommunicator getMK() {
		return MKProvider.getMK();
	}

}
