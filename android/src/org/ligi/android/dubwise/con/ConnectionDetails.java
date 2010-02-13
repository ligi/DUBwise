package org.ligi.android.dubwise.con;

import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseBaseListActivity;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKDebugData;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

public class ConnectionDetails extends DUBwiseBaseListActivity implements Runnable{

	private String[] menu_items;
	private ArrayAdapter<String> adapter;
	private boolean running=true;
	private final static int VALUE_COUNT=12;
	
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
		menu_items[0]="Time: " + mk.conn_time_in_s() + " s";
		menu_items[1]="Bytes in: " + mk.stats.bytes_in;
		menu_items[2]="Bytes out: " + mk.stats.bytes_out;
		menu_items[3]="CRC Fails: " + mk.stats.crc_fail;
		menu_items[4]="Debug Data: " + mk.stats.debug_data_count + "/" + mk.stats.debug_data_request_count;
		menu_items[5]="Analog Names: " + mk.stats.debug_names_count + "/" + mk.stats.debug_name_request_count;
		menu_items[6]="LCD Data: " + mk.stats.lcd_data_count + "/" + mk.stats.lcd_data_request_count;
		menu_items[7]="ExternalControl: " + mk.stats.external_control_confirm_frame_count + "/" + mk.stats.external_control_request_count;
		menu_items[8]="Version: " + mk.stats.version_data_count +"/" + mk.stats.version_data_request_count;
		menu_items[9]="Params: " + mk.stats.params_data_count +"/" + mk.stats.params_data_request_count;
		
		menu_items[10]="Angles: " + mk.stats.angle_data_count;
		menu_items[11]="Motortest: " + mk.stats.motortest_request_count;
		
		
		
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
