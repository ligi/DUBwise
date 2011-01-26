package org.ligi.android.dubwise.blackbox;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.helper.RefreshingStringListActivity;

public class BlackBoxWatchActivity extends RefreshingStringListActivity {

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
				return "fname:" + BlackBox.getInstance().act_fname;
			case 2:
				return "record:" + BlackBox.getInstance().act_records;
		}
		return null;
	}

}