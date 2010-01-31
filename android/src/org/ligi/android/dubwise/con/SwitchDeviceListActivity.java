package org.ligi.android.dubwise.con;

import org.ligi.android.dubwise.con.bluetooth.BluetoothDeviceListActivity;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseBaseListActivity;
import org.ligi.ufo.FakeCommunicationAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.util.Log;

import android.widget.ArrayAdapter;

public class SwitchDeviceListActivity extends DUBwiseBaseListActivity {

	String[] menu_items = new String[] { "to Navi" , "to FC" , "to MK3MAG"};
	int[] menu_actions = new int[] { ACTIONID_SWITCH_NAVI , ACTIONID_SWITCH_FC , ACTIONID_SWITCH_MK3MAG};

	public final static int ACTIONID_SWITCH_NAVI = 0;
	public final static int ACTIONID_SWITCH_FC = 1;
	public final static int ACTIONID_SWITCH_MK3MAG  = 2;
	
	
	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCalls.beforeContent(this);
		
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

                case ACTIONID_SWITCH_NAVI:
                	MKProvider.getMK().switch_to_navi();
                	break;
                case ACTIONID_SWITCH_FC:
                	MKProvider.getMK().switch_to_fc();
                	break;
                
                case ACTIONID_SWITCH_MK3MAG:
                	MKProvider.getMK().switch_to_mk3mag();
                	break;
                
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		finish();
	}

}
