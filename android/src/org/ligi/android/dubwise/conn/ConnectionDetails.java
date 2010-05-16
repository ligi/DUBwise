/**************************************************************************
 *                                          
 * Activity to show a Connection Details
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

package org.ligi.android.dubwise.conn;

import org.ligi.android.dubwise.conn.bluetooth.BluetoothCommunicationAdapter;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseBaseListActivity;
import org.ligi.ufo.MKCommunicator;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

public class ConnectionDetails extends DUBwiseBaseListActivity implements Runnable{

	private String[] menu_items;
	private ArrayAdapter<String> adapter;
	private boolean running=true;
	private final static int VALUE_COUNT=15;
	
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
	       new Thread(this).start();
	}

	
	private void refresh_values() {
		MKCommunicator mk=MKProvider.getMK();
		int pos=0;
		
		menu_items[pos++]="Time: " + mk.getConnectionTime() + " s";
		menu_items[pos++]="Bytes in: " + mk.stats.bytes_in;
		menu_items[pos++]="Bytes out: " + mk.stats.bytes_out;
		menu_items[pos++]="CRC Fails: " + mk.stats.crc_fail;
		menu_items[pos++]="Debug Data: " + mk.stats.debug_data_count + "/" + mk.stats.debug_data_request_count;
		menu_items[pos++]="Analog Names: " + mk.stats.debug_names_count + "/" + mk.stats.debug_name_request_count;
		menu_items[pos++]="LCD Data: " + mk.stats.lcd_data_count + "/" + mk.stats.lcd_data_request_count;
		menu_items[pos++]="ExternalControl: " + mk.stats.external_control_confirm_frame_count + "/" + mk.stats.external_control_request_count;
		menu_items[pos++]="Version: " + mk.stats.version_data_count +"/" + mk.stats.version_data_request_count;
		menu_items[pos++]="Params: " + mk.stats.params_data_count +"/" + mk.stats.params_data_request_count;
		menu_items[pos++]="OSD: " + mk.stats.navi_data_count ;
		menu_items[pos++]="Angles: " + mk.stats.angle_data_count;
		menu_items[pos++]="Motortest: " + mk.stats.motortest_request_count;
		menu_items[pos++]="Other: " + mk.stats.other_data_count;
		
		

		if (mk.getCommunicationAdapter() instanceof BluetoothCommunicationAdapter)
			menu_items[pos++]="BTRssi" + ((BluetoothCommunicationAdapter)(mk.getCommunicationAdapter())).getRSSI();
		else
			menu_items[pos++]="NO BT RSSI" ;
		
	}
		
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		running=false;
	}
	 final Handler mHandler = new Handler();
	    
	 	// Create runnable for posting
	   final Runnable mUpdateResults = new Runnable() {
	       public void run() {
	    	   adapter.notifyDataSetChanged();
	       }
	    };
	@Override
	public void run() {
		while (running) {
			refresh_values();
			mHandler.post(mUpdateResults);
			try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
	}

}
