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


package org.ligi.android.dubwise.voice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class VoicePrefs {

	private static SharedPreferences shared_prefs;
	
	// Spring settings
	public static String KEY_VOICE_ENABLED="voice_enabled";
	public static String KEY_DO_VOICE_ALT="voice_alt";
	public static String KEY_DO_VOICE_CURRENT="voice_current";
	public static String KEY_DO_VOICE_USEDCAPACITY="voice_usedcapacity";
	public static String KEY_DO_VOICE_SATELITES="voice_satelites";
	public static String KEY_DO_VOICE_VOLTS="voice_volts";
	public static String KEY_DO_VOICE_NCERR="voice_ncerr";
	public static String KEY_DO_VOICE_CONNINFO="voice_conninfo";
	
	
	
	
	public static void init(Context context) {
		shared_prefs=PreferenceManager.getDefaultSharedPreferences(context)	;
	}
	

	public static boolean isVoiceEnabled() {
		return shared_prefs.getBoolean(KEY_VOICE_ENABLED, false);
	}
	
	
	public static boolean isVoiceAltEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_ALT, false);
	}
	
	public static boolean isVoiceVoltsEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_VOLTS, false);
	}
	
	public static boolean isVoiceUsedCapacityEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_USEDCAPACITY, false);
	}
	
	public static boolean isVoiceSatelitesEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_SATELITES, false);
	}
	
	public static boolean isVoiceNaviErrorEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_NCERR, false);
	}

	public static boolean isVoiceCurrentEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_CURRENT, false);
	}

	public static boolean isConnectionInfoEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_CONNINFO, false);
	}

}

