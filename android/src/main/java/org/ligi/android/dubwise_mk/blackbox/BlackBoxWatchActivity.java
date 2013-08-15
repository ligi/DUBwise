package org.ligi.android.dubwise_mk.blackbox;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.androidhelper.base_activities.RefreshingStringBaseListActivity;

public class BlackBoxWatchActivity extends RefreshingStringBaseListActivity {

    @Override
    public String getStringByPosition(int pos) {
        switch (pos) {
            case 0:
                if (!BlackBoxPrefs.isBlackBoxEnabled())
                    return "not enabled";
                if (!MKProvider.getMK().isConnected())
                    return "not connected";
                if (!MKProvider.getMK().isFlying())
                    return "not flying";
                return "recording";
            case 1:
                return "fname:" + BlackBox.getInstance().getActFileName();
            case 2:
                return "record:" + BlackBox.getInstance().getActRecords();
        }
        return null;
    }

}
