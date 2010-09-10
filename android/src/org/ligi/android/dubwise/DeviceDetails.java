/**************************************************************************
 *                                          
 * Activity to show Device-Details
 *                                          
 * Author:  Marcus -LiGi- Bueschleb   
 *
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
 * 
 * License:
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseBaseListActivity;
import org.ligi.ufo.MKCommunicator;
import android.os.Bundle;
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
		menu_items[0]="Type: " + mk.getExtendedConnectionName();
		
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
		ActivityCalls.onDestroy(this);
		
	}
}
