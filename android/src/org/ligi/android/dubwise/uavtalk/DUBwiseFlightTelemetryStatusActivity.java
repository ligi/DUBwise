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

package org.ligi.android.dubwise.uavtalk;

import org.ligi.android.dubwise.helper.RefreshingStringListActivity;

public class DUBwiseFlightTelemetryStatusActivity extends RefreshingStringListActivity {

	public String getStringByPosition(int pos) {
    	
		switch(pos) {
			case 0: 
				return "Last Conn: " + DUBwiseFlightTelemetry.getInstance().last_conn;
			case 1: 
				return "Bytes in: " + DUBwiseFlightTelemetry.getInstance().bytes_in;
			case 2: 
				return "Bytes out: " + DUBwiseFlightTelemetry.getInstance().bytes_out;
		
        }
		return null;
	}

	@Override
	public int getRefreshSleep() {
		return 100;
	}

}
