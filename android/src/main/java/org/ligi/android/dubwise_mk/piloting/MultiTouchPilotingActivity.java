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


package org.ligi.android.dubwise_mk.piloting;

import org.ligi.android.dubwise_mk.helper.ActivityCalls;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
		ActivityCalls.afterContent(this);	
		super.onResume();
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
		menu.add(0,MENU_SETTINGS,0,"Settings").setIcon(android.R.drawable.ic_menu_preferences);
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
}