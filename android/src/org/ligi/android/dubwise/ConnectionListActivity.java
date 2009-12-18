package org.ligi.android.dubwise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.util.Log;

import android.widget.ArrayAdapter;

public class ConnectionListActivity extends DUBwiseBaseListActivity {

	String[] menu_items = new String[] { "Fake Connection","Connect via Bluetooth","connect via TCP/IP" ,"disconnect"};
	int[] menu_actions = new int[] { ACTIONID_FAKE , ACTIONID_BT , ACTIONID_TCP , ACTIONID_DISCONN};

	public final static int ACTIONID_FAKE = 0;
	public final static int ACTIONID_BT = 1;
	public final static int ACTIONID_TCP = 2;
	public final static int ACTIONID_DISCONN = 3;
	
	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCalls.beforeContent(this);
		// menu_items[0]=settings.getString("conn_host","--");
		
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
                    MKProvider.getMK().connect_to( "fake", "fake" );
                    finish();
                    break;
                case ACTIONID_BT:
                    startActivity( new Intent( this, ServerListActivity.class ) );
                    break;
                case ACTIONID_TCP:
                    startActivity( new Intent( this, ConnectViaTCPActivity.class ) );
                    break;
                case ACTIONID_DISCONN:
                    MKProvider.getMK().close_connections( true );   
                    break;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start the activity

	}

}
