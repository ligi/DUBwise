package org.ligi.android.dubwise;

import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseBaseListActivity;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKDebugData;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

public class AnalogValuesActivity extends DUBwiseBaseListActivity implements Runnable{

	private String[] menu_items;
	private ArrayAdapter<String> adapter;
	private boolean running=true;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set user intent to rawdebug so that analog values get resolved
		MKProvider.getMK().user_intent=MKCommunicator.USER_INTENT_RAWDEBUG;
		menu_items=new String[MKDebugData.MAX_VALUES];
		refresh_values();
		
		ActivityCalls.beforeContent(this);
		adapter=new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, menu_items);
		
	    this.setListAdapter(adapter);
	       new Thread(this).start();
	}

	
	private void refresh_values() {
		for (int i=0;i<MKDebugData.MAX_VALUES;i++)
			menu_items[i]=MKProvider.getMK().debug_data.names[i]+" " + MKProvider.getMK().debug_data.analog[i];
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
