package org.ligi.android.dubwise.helper;

public interface DUBwiseBackgroundTask extends Runnable {

	public void start();
	public void stop();
	public String getName();
	public String getDescription();
	

}
