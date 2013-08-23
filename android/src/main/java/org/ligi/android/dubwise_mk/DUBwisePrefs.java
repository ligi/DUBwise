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


package org.ligi.android.dubwise_mk;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.ligi.android.dubwise_mk.app.App;

public class DUBwisePrefs {

    private static SharedPreferences shared_prefs;
    private static SharedPreferences.Editor editor;


    public static String KEY_FULLSCREEN = "fullscreen";
    public static String KEY_STATUSBAR = "status_bar";
    public static String KEY_KEEPLIGHT = "keeplight";
    public static String KEY_EXPERTMODE = "expert_mode";
    public static String KEY_VERBOSELOG = "verbose_log";

    public static String KEY_COCKPIT_SHOW_ALT = "cockpit_m";
    public static String KEY_COCKPIT_SHOW_CURRENT = "cockpit_A";
    public static String KEY_COCKPIT_SHOW_USEDCAPACITY = "cockpit_mAh";
    public static String KEY_COCKPIT_SHOW_FLIGHTTIME = "cockpit_sec";

    public static String KEY_INVERT_ARTIFICIAL_HORIZON = "invert_horizon";


    public static String KEY_STARTCONNTYPE = "startconntype";
    public static String KEY_STARTCONNBLUETOOTHMAC = "startconnbtmac";
    public static String KEY_STARTCONNBLUETOOTHNAME = "startconnbtname";


    public final static int STARTCONNTYPE_NONE = 0;
    public final static int STARTCONNTYPE_BLUETOOTH = 1;
    public final static int STARTCONNTYPE_TCP = 2;
    public final static int STARTCONNTYPE_SIMULATION = 3;


    public final static int KEEPLIGHT_NEVER = 0;
    public final static int KEEPLIGHT_CONN = 1;
    public final static int KEEPLIGHT_ALWAYS = 1;

    public final static String KEEPLIGHT_DEFAULT = "never";

    public static void init(Context context) {

        shared_prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = shared_prefs.edit();
    }

    public static String[] getKeepLightOptionStrings() {
        return new String[]{"never", "when connected", "always"};
    }

    public static String getKeepLightString() {
        return shared_prefs.getString(KEY_KEEPLIGHT, KEEPLIGHT_DEFAULT);
    }


    public static boolean keepLightNow() {
        if (getKeepLightString().equals("never"))
            return false;

        if (getKeepLightString().equals("always"))
            return true;

        // when connected
        return App.getMK().isConnected();
    }

    public static boolean isFullscreenEnabled() {
        return shared_prefs.getBoolean(KEY_FULLSCREEN, false);
    }

    public static boolean isArtificialHorizonInverted() {
        return shared_prefs.getBoolean(KEY_INVERT_ARTIFICIAL_HORIZON, false);
    }

    public static boolean isExpertModeEnabled() {
        return shared_prefs.getBoolean(KEY_EXPERTMODE, false);
    }

    public static boolean isVerboseLoggingEnabled() {
        return shared_prefs.getBoolean(KEY_VERBOSELOG, false);
    }

    public static boolean isStatusBarEnabled() {
        return shared_prefs.getBoolean(KEY_STATUSBAR, true);
    }

    public static int getStartConnType() {
        return shared_prefs.getInt(KEY_STARTCONNTYPE, STARTCONNTYPE_NONE);
    }

    public static void setStartConnType(int type) {
        editor.putInt(KEY_STARTCONNTYPE, type).commit();
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

    public static void setStartConnBluetootName(String name) {
        editor.putString(KEY_STARTCONNBLUETOOTHNAME, name);
        editor.commit();
    }

    // cockipt seciton
    public static boolean showFlightTime() {
        return shared_prefs.getBoolean(KEY_COCKPIT_SHOW_FLIGHTTIME, true);
    }

    public static boolean showUsedCapacity() {
        return shared_prefs.getBoolean(KEY_COCKPIT_SHOW_USEDCAPACITY, true);
    }

    public static boolean showAlt() {
        return shared_prefs.getBoolean(KEY_COCKPIT_SHOW_ALT, true);
    }

    public static boolean showCurrent() {
        return shared_prefs.getBoolean(KEY_COCKPIT_SHOW_CURRENT, true);
    }


}

