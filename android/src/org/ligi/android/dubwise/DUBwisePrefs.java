

package org.ligi.android.dubwise;


/**
 * gobandroid 
 * by Marcus -Ligi- Bueschleb 
 * http://ligi.de
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as 
 * published by the Free Software Foundation; 
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 **/


import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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

