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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * class to represent settings for persisting telemetry data
 * 
 * @author ligi
 *
 */
public class BlackBoxPrefs {

	private static SharedPreferences shared_prefs;
	private static SharedPreferences.Editor editor;
	
	public static String KEY_PATH="blackbox_path";
	public static String KEY_ENABLED="blackbox_enabled";
	
	public static void init(Context context) {
		shared_prefs=PreferenceManager.getDefaultSharedPreferences(context)	;
		editor=shared_prefs.edit();
	}

	public static String getPath() {
		return shared_prefs.getString(KEY_PATH,"/sdcard/DUBwise/BlackBox");
	}
	
	public static boolean isBlackBoxEnabled() {
		return shared_prefs.getBoolean(KEY_ENABLED, true);
	}
}
