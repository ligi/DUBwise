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

package org.ligi.android.dubwise_mk.voice;

import org.ligi.android.common.activitys.RefreshingStringListActivity;

public class VoiceDebugActivity extends RefreshingStringListActivity {


	@Override
	public int getRefreshSleep() {
		return 100;
	}

	@Override
	public String getStringByPosition(int pos) {
		switch (pos) {
			case 0: return "next Block: " + StatusVoice.getInstance().getPauseTimeout() ;
			case 1: return "last: " + StatusVoice.getInstance().getLastSpoken();
		}
		
		return null;
	}

}
