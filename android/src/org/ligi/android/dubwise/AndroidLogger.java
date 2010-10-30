package org.ligi.android.dubwise;

import org.ligi.tracedroid.Log;
import org.ligi.ufo.logging.LoggingInterface;

public class AndroidLogger implements LoggingInterface {

	@Override
	public void e(String msg) {
		Log.e(msg);
	}

	@Override
	public void i(String msg) {
		Log.i(msg);
	}

	@Override
	public void w(String msg) {
		Log.w(msg);
	}

}
