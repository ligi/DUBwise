package org.ligi.android.dubwise_mk.app;

import android.app.Application;

import org.ligi.android.dubwise_mk.app.App;
import org.ligi.ufo.MKCommunicator;

public class App extends Application {

    static MKCommunicator mk = null;

    public static MKCommunicator getMK() {
        if (mk == null) {
            mk = new MKCommunicator();
        }

        return mk;
    }

    public static void disposeMK() {
        mk = null;
    }
}
