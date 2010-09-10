/**************************************************************************
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
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise.blackbox;

/**
 * class to represent settings for persisting telemetry data
 * 
 * @author ligi
 *
 */
public class BlackBoxPrefs {

	public static String getPath() {
		return "/sdcard/DUBwise/BlackBox";
	}
	
	public static boolean isBlackBoxEnabled() {
		return true;
	}
}
