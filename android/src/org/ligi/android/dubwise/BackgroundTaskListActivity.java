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

package org.ligi.android.dubwise;

import org.ligi.android.common.activitys.RefreshingStringListActivity;
import org.ligi.android.dubwise.helper.DUBwiseBackgroundHandler;
import org.ligi.android.dubwise.helper.DUBwiseBackgroundTask;


public class BackgroundTaskListActivity extends RefreshingStringListActivity {

	public String getStringByPosition(int pos) {
		int i=0;
		for (DUBwiseBackgroundTask tsk : DUBwiseBackgroundHandler.getInstance().getBackgroundTasks()) {
			if (pos==(i++))
				return tsk.getName();
		}
		return null;
	}

}
