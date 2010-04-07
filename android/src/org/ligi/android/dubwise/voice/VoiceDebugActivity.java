/**************************************************************************
 *                                          
 * Activity to show Analog Values
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

package org.ligi.android.dubwise.voice;

import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseBaseListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

public class VoiceDebugActivity extends DUBwiseBaseListActivity implements Runnable{

	private String[] menu_items;
	private ArrayAdapter<String> adapter;
	private boolean running=true;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	
		menu_items=new String[2];
		refresh_values();
		
		ActivityCalls.beforeContent(this);
		adapter=new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, menu_items);
		
	    this.setListAdapter(adapter);
	       new Thread(this).start();
	}

	
	private void refresh_values() {
		menu_items[0]="next Block: " + StatusVoice.getInstance().getPauseTimeout() ;
		menu_items[1]="last: " + StatusVoice.getInstance().getLastSpoken();
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
		ActivityCalls.onDestroy(this);
		
		
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
