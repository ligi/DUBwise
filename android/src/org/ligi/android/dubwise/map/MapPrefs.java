/**************************************************************************
 *                                          
 * Class to handle DUBwise Map Preferences
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


package org.ligi.android.dubwise.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MapPrefs {

	
	private static SharedPreferences shared_prefs;
	
	public final static String KEY_ZOOM2LEVEL="map_zoom2level";
	
	public final static String KEY_SHOW_UFO="map_show_ufo";
	public final static String KEY_SHOW_UFO_HEADING="map_show_ufo_heading";		
	public final static String KEY_SHOW_UFO_RADIUS="map_show_ufo_radius";		
		
	public final static String KEY_SHOW_HOME="map_show_home";
	public final static String KEY_SHOW_HOME_RADIUS="map_show_home_radius";
	
	public final static String KEY_SHOW_PHONE="map_show_phone";
	
	
	
	
	public static void init(Context context) {
		shared_prefs=PreferenceManager.getDefaultSharedPreferences(context)	;
	}
	
	public static int getZoom2level() {
		return shared_prefs.getInt(KEY_ZOOM2LEVEL,20);
	}


	public static void setZoom2level(int level) {
		shared_prefs.edit().putInt(KEY_ZOOM2LEVEL, level).commit();
	}
	
	public static boolean showUFO() {
		return shared_prefs.getBoolean(KEY_SHOW_UFO, true);
	}
	
	public static boolean showUFOHeading() {
		return shared_prefs.getBoolean(KEY_SHOW_UFO_HEADING, true);
	}
	
	public static boolean showUFORadius() {
		return shared_prefs.getBoolean(KEY_SHOW_UFO_RADIUS, true);
	}
	
	public static boolean showHome() {
		return shared_prefs.getBoolean(KEY_SHOW_HOME, true);
	}
	
	public static boolean showHomeRadius() {
		return shared_prefs.getBoolean(KEY_SHOW_HOME_RADIUS, true);
	}
	
	public static boolean showPhone() {
		return shared_prefs.getBoolean(KEY_SHOW_PHONE, true);
	}
}

