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

import org.ligi.android.dubwise.helper.ActivityCalls;

import android.app.Activity;
import android.os.Bundle;

public class MultiTouchPilotingActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		
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

	
}