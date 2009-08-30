package org.ligi.android;

import android.app.Activity;
import android.content.SharedPreferences;

public class AndroidMKCommunicator extends org.ligi.ufo.MKCommunicator

{

    public  AndroidMKCommunicator(android.content.Context ctx)
    {
	super();
	SharedPreferences settings;
	settings =ctx.getSharedPreferences("DUBwise", 0);
	this.connect_to(settings.getString("conn_host","10.0.2.2")+":"+(settings.getString("conn_port","9876")),"unnamed");
    }



}
