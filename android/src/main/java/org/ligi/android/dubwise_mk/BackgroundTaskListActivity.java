/**************************************************************************
 *                                          
 * Activity to show a Changing Strings in a list
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

package org.ligi.android.dubwise_mk;

import org.ligi.android.dubwise_mk.helper.DUBwiseBackgroundHandler;
import org.ligi.android.dubwise_mk.helper.DUBwiseBackgroundTask;
import org.ligi.androidhelper.base_activities.RefreshingStringBaseListActivity;


public class BackgroundTaskListActivity extends RefreshingStringBaseListActivity {

	public String getStringByPosition(int pos) {
		int i=0;
		for (DUBwiseBackgroundTask tsk : DUBwiseBackgroundHandler.getInstance().getBackgroundTasks()) {
			if (pos==(i++))
				return tsk.getName();
		}
		return null;
	}

}
