package org.ligi.android.dubwise_mk.balance;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.ufo.MKCommunicator;

import android.app.Activity;
import android.os.Bundle;

public class BalanceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    MKProvider.getMK().user_intent=MKCommunicator.USER_INTENT_3DDATA;
		setContentView(new BalanceView(this));
		super.onCreate(savedInstanceState);
	}

}
