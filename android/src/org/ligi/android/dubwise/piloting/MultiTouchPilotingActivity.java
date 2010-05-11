/**************************************************************************
 *                                          
 * Activity to Pilot the UFO via Multitouch
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


package org.ligi.android.dubwise.piloting;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.tracedroid.logging.Log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MultiTouchPilotingActivity extends Activity {
	
	private static final int MENU_SETTINGS = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		PilotingPrefs.init(this);
		
		setContentView(new MultiTouchPilotingView(this));
	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);	
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    
		MenuItem settings_menu=menu.add(0,MENU_SETTINGS,0,"Settings");
		settings_menu.setIcon(android.R.drawable.ic_menu_preferences);

		return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	    	case MENU_SETTINGS:
	    		
	    		startActivity(new Intent(this, PilotingPrefsActivity.class));
	    		return true;
	    }
	    return false;
	}

	
	public boolean onTouch(View v, MotionEvent event) {
		Log.i("touch graph"+event.getAction() + " " + MotionEvent.ACTION_UP);
		if (event.getAction()==MotionEvent.ACTION_UP)
		{
			Log.i("touch graph up !!");
		
			MKProvider.getMK().freeze_debug_buff=!MKProvider.getMK().freeze_debug_buff;
		
		}
		return true;
//		return false;
	}

	
}