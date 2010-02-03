package org.ligi.android.dubwise;

import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseBaseListActivity;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKDebugData;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

public class DeviceDetails extends DUBwiseBaseListActivity {

	private String[] menu_items;
	private ArrayAdapter<String> adapter;
	private final static int VALUE_COUNT=3;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		menu_items=new String[VALUE_COUNT];
		refresh_values();
		
		ActivityCalls.beforeContent(this);
		adapter=new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, menu_items);
		
	    this.setListAdapter(adapter);
	}

	
	private void refresh_values() {
		MKCommunicator mk=MKProvider.getMK();
		menu_items[0]="Type: " + mk.extended_name();
		
		menu_items[1]="Version: " + mk.version.version_str;
		menu_items[2]="Protocol: " + mk.version.proto_str;
	
	}
		
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
}
