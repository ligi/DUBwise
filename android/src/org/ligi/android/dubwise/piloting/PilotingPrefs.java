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


package org.ligi.android.dubwise.piloting;


import org.ligi.android.dubwise.con.MKProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PilotingPrefs {

	private static SharedPreferences shared_prefs;
	private static SharedPreferences.Editor editor;
	
	
	public static String KEY_LEFT_PAD_SPRING_HORIZONTAL="lh_spring";
	public static String KEY_LEFT_PAD_SPRING_VERTICAL="lv_spring";
	
	public static String KEY_RIGHT_PAD_SPRING_HORIZONTAL="rh_spring";
	public static String KEY_RIGHT_PAD_SPRING_VERTICAL="rv_spring";
		
	public static void init(Context context) {
		shared_prefs=PreferenceManager.getDefaultSharedPreferences(context)	;
		editor=shared_prefs.edit();
	}
		
	public static boolean hasRightPadVerticalSpring() {
		return shared_prefs.getBoolean(KEY_RIGHT_PAD_SPRING_VERTICAL, false);
	}
	public static boolean hasRightPadHorizontalSpring() {
		return shared_prefs.getBoolean(KEY_RIGHT_PAD_SPRING_HORIZONTAL, false);
	}

	public static boolean hasLeftPadVerticalSpring() {
		return shared_prefs.getBoolean(KEY_LEFT_PAD_SPRING_VERTICAL, false);
	}
	public static boolean hasLeftPadHorizontalSpring() {
		return shared_prefs.getBoolean(KEY_LEFT_PAD_SPRING_HORIZONTAL, false);
	}
	
}

