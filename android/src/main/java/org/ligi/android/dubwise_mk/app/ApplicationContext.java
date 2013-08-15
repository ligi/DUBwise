package org.ligi.android.dubwise_mk.app;

import android.app.Application;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.ufo.MKCommunicator;

public class ApplicationContext extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
    }

    public MKCommunicator getMK() {
        return MKProvider.getMK();
    }

}
