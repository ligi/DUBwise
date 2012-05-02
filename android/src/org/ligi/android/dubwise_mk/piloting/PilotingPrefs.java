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


package org.ligi.android.dubwise_mk.piloting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PilotingPrefs {

	private static SharedPreferences shared_prefs;
	
	// Spring settings
	public static String KEY_LEFT_PAD_SPRING_HORIZONTAL="lh_spring";
	public static String KEY_LEFT_PAD_SPRING_VERTICAL="lv_spring";
	
	public static String KEY_RIGHT_PAD_SPRING_HORIZONTAL="rh_spring";
	public static String KEY_RIGHT_PAD_SPRING_VERTICAL="rv_spring";
	
	// external Control Mapping
	public static String KEY_LEFT_PAD_EC_MAPPING_HORIZONTAL="lh_ecmap";
	public static String KEY_LEFT_PAD_EC_MAPPING_VERTICAL="lv_ecmap";
	
	public static String KEY_RIGHT_PAD_EC_MAPPING_HORIZONTAL="rh_ecmap";
	public static String KEY_RIGHT_PAD_EC_MAPPING_VERTICAL="rv_ecmap";
	
	public static String DEFAULT_LEFT_PAD_EC_MAPPING_HORIZONTAL="yaw";
	public static String DEFAULT_LEFT_PAD_EC_MAPPING_VERTICAL="gas";
	
	public static String DEFAULT_RIGHT_PAD_EC_MAPPING_HORIZONTAL="roll";
	public static String DEFAULT_RIGHT_PAD_EC_MAPPING_VERTICAL="nick";
	
	// serial Channel Mapping
	public static String KEY_LEFT_PAD_SC_MAPPING_HORIZONTAL="lh_scmap";
	public static String KEY_LEFT_PAD_SC_MAPPING_VERTICAL="lv_scmap";
	
	public static String KEY_RIGHT_PAD_SC_MAPPING_HORIZONTAL="rh_scmap";
	public static String KEY_RIGHT_PAD_SC_MAPPING_VERTICAL="rv_scmap";

	public static String DEFAULT_LEFT_PAD_SC_MAPPING_HORIZONTAL="Serial 1";
	public static String DEFAULT_LEFT_PAD_SC_MAPPING_VERTICAL="Serial 2";
	
	public static String DEFAULT_RIGHT_PAD_SC_MAPPING_HORIZONTAL="Serial 3";
	public static String DEFAULT_RIGHT_PAD_SC_MAPPING_VERTICAL="Serial 4";

	
	
	
	
	
	public static String KEY_SENDSC="send_sc";
	public static String KEY_SENDEC="send_ec";
	
	
	public static String KEY_SHOWVALUES="show_values";
	
	public static void init(Context context) {
		shared_prefs=PreferenceManager.getDefaultSharedPreferences(context)	;
	}
	
	public static boolean isExternControlEnabled() {
		return shared_prefs.getBoolean(KEY_SENDEC, false);
	}
	
	public static boolean isSerialChannelsEnabled() {
		return shared_prefs.getBoolean(KEY_SENDSC, false);
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

	
	public static String getLeftPadHorizontalECMappingStr() {
		return shared_prefs.getString(KEY_LEFT_PAD_EC_MAPPING_HORIZONTAL, DEFAULT_LEFT_PAD_EC_MAPPING_HORIZONTAL);
	}
	
	public static String getRightPadHorizontalECMappingStr() {
		return shared_prefs.getString(KEY_RIGHT_PAD_EC_MAPPING_HORIZONTAL, DEFAULT_RIGHT_PAD_EC_MAPPING_HORIZONTAL);
	}
	
	public static String getLeftPadVerticalECMappingStr() {
		return shared_prefs.getString(KEY_LEFT_PAD_EC_MAPPING_VERTICAL, DEFAULT_LEFT_PAD_EC_MAPPING_VERTICAL);
	}
	
	public static String getRightPadVerticalECMappingStr() {
		return shared_prefs.getString(KEY_RIGHT_PAD_EC_MAPPING_VERTICAL, DEFAULT_RIGHT_PAD_EC_MAPPING_VERTICAL);
	}
	
	

	public static String getLeftPadHorizontalSCMappingStr() {
		return shared_prefs.getString(KEY_LEFT_PAD_SC_MAPPING_HORIZONTAL, DEFAULT_LEFT_PAD_SC_MAPPING_HORIZONTAL);
	}
	
	public static String getRightPadHorizontalSCMappingStr() {
		return shared_prefs.getString(KEY_RIGHT_PAD_SC_MAPPING_HORIZONTAL, DEFAULT_RIGHT_PAD_SC_MAPPING_HORIZONTAL);
	}
	
	public static String getLeftPadVerticalSCMappingStr() {
		return shared_prefs.getString(KEY_LEFT_PAD_SC_MAPPING_VERTICAL, DEFAULT_LEFT_PAD_SC_MAPPING_VERTICAL);
	}
	
	public static String getRightPadVerticalSCMappingStr() {
		return shared_prefs.getString(KEY_RIGHT_PAD_SC_MAPPING_VERTICAL, DEFAULT_RIGHT_PAD_SC_MAPPING_VERTICAL);
	}
	
	
	public static boolean showValues() {
		return shared_prefs.getBoolean(KEY_SHOWVALUES, false);
	}

	public static String[] getExternControlMappingStrings() {
		return new String[] { "Nick" ,"Roll" ,"Yaw", "Gas"  };
	}
	
	public static String[] getSerialChannelMappingStrings() {
		String[] res=new String[12];
		for ( int i=0;i<12;i++)
			res[i]="Serial " + (i+1);
		
		return res;
	}
}

