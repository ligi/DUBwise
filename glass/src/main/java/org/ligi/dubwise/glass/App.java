package org.ligi.dubwise.glass;

import android.app.Application;

import org.ligi.ufo.MKCommunicator;

public class App extends Application {

    static MKCommunicator mk;

    public static MKCommunicator getMK() {
        if (mk==null) {
            mk=new MKCommunicator();
        }

        return mk;
    }

}
