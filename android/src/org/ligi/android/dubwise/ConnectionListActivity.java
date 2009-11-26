package org.ligi.android.dubwise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.util.Log;

import android.widget.ArrayAdapter;

//import com.google.android.maps.MapView;

import android.content.SharedPreferences;

import org.ligi.ufo.MKCommunicator;

public class ConnectionListActivity extends DUBwiseBaseListActivity {

	// DUBwiseView canvas;
	boolean do_sound;
	boolean fullscreen;
	// MKCommunicator mk;
	String[] menu_items = new String[] { "Fake Connection","Connect via Bluetooth","connect via TCP/IP" };
	int[] menu_actions = new int[] { ACTIONID_FAKE , ACTIONID_BT , ACTIONID_TCP};

	public final static int ACTIONID_FAKE = 0;
	public final static int ACTIONID_BT = 1;
	public final static int ACTIONID_TCP = 2;
	
	

	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCalls.beforeContent(this);
		// menu_items[0]=settings.getString("conn_host","--");
		MKCommunicator mk=new MKCommunicator();
		this.setListAdapter(new ArrayAdapter<String>(this,
		 android.R.layout.simple_list_item_1, menu_items));
		
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	public void log(String msg) {
		Log.d("DUWISE", msg);
	}

	public void quit() {
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menu_items));
		// setContentView(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		try {

			switch (menu_actions[position]) {

			case ACTIONID_FAKE:
				MKProvider.getMK().connect_to("fake", "fake");
				finish();
				break;
			case ACTIONID_BT:
				startActivity(new Intent(this, ServerListActivity.class));
				break;
			case ACTIONID_TCP:
				startActivity(new Intent(this, ConnectViaTCPActivity.class));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start the activity

	}

}
