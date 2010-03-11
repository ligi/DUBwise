/**************************************************************************
 *                                          
 * Class to handle DUBwise Preferences
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


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DUBwisePrefs {

	private static SharedPreferences shared_prefs;
	private static SharedPreferences.Editor editor;
	
	
	public static String KEY_FULLSCREEN="fullscreen";
	public static String KEY_KEEPLIGHT="keeplight";
	public static String KEY_STARTCONNTYPE="startconntype";
	public static String KEY_STARTCONNBLUETOOTHMAC="startconnbtmac";
	public static String KEY_STARTCONNBLUETOOTHNAME="startconnbtname";

	
	public final static int STARTCONNTYPE_NONE=0;
	public final static int STARTCONNTYPE_BLUETOOTH=1;
	public final static int STARTCONNTYPE_TCP=2;
	public final static int STARTCONNTYPE_FAKE=3;
	
	
	public static void init(Context context) {
		shared_prefs=PreferenceManager.getDefaultSharedPreferences(context)	;
		editor=shared_prefs.edit();
	}
	

	public static boolean getFullscreenEnabled() {
		return shared_prefs.getBoolean(KEY_FULLSCREEN, false);
	}

	public static int getStartConnType() {
		return shared_prefs.getInt(KEY_STARTCONNTYPE, STARTCONNTYPE_NONE);
	}

	public static void setStartConnType(int type) {
		editor.putInt(KEY_STARTCONNTYPE, type);
		editor.commit();
	}

	public static String getStartConnBluetootMAC() {
		return shared_prefs.getString(KEY_STARTCONNBLUETOOTHMAC, "");
	}


	public static String getStartConnBluetootName() {
		return shared_prefs.getString(KEY_STARTCONNBLUETOOTHNAME, "");
	}


	public static void setStartConnBluetootMAC(String mac) {
		editor.putString(KEY_STARTCONNBLUETOOTHMAC, mac);
		editor.commit();
	}


	public static  void setStartConnBluetootName(String name) {
		editor.putString(KEY_STARTCONNBLUETOOTHNAME, name);
		editor.commit();
	}


	
}

