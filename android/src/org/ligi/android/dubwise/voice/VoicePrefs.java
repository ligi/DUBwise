/**************************************************************************
 *                                                                                     
 * Author:  Marcus -LiGi- Bueschleb   
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

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
/**
 * Class to provide/store Preferences for the VoiceOutput
 *
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class VoicePrefs {

	private static SharedPreferences shared_prefs;
	
	// Spring settings
	public final static String KEY_VOICE_ENABLED="voice_enabled";
	public final static String KEY_DO_VOICE_ALT="voice_alt";
	public final static String KEY_DO_VOICE_MAX_ALT="voice_max_alt";
	public final static String KEY_DO_VOICE_CURRENT="voice_current";
	public final static String KEY_DO_VOICE_USEDCAPACITY="voice_usedcapacity";
	public final static String KEY_DO_VOICE_SATELITES="voice_satelites";
	public final static String KEY_DO_VOICE_VOLTS="voice_volts";
	public final static String KEY_DO_VOICE_NCERR="voice_ncerr";
	public final static String KEY_DO_VOICE_CONNINFO="voice_conninfo";
	public final static String KEY_DO_VOICE_FLIGHTTIME="voice_flighttime";
	public final static String KEY_DO_VOICE_RCLOST="voice_rclost";
	
	public final static String KEY_DO_VOICE_DISTANCE2HOME="voice_distance2home";
	public final static String KEY_DO_VOICE_DISTANCE2TARGET="voice_distance2target";
	
	
	public final static String KEY_DO_VOICE_SPEED="voice_speed";
	public final static String KEY_DO_VOICE_MAX_SPEED="voice_max_speed";
	
	public final static String KEY_VOICE_PAUSE = "voice_pause3";

	public final static String KEY_VOICE_STREAM = "voice_stream";
	
	private static final int DEFAULT_PAUSE = 5000;	
	
	public static void init(Context context) {
		shared_prefs=PreferenceManager.getDefaultSharedPreferences(context)	;
	}
	
	/**
	 * @return PauseTime in ms
	 */
	public static int getPauseTimeInMS() {
		String tmp= shared_prefs.getString(KEY_VOICE_PAUSE,"");
		String[] split=tmp.split(":");
		if (split.length!=2)
			return DEFAULT_PAUSE;
		else
			try {
				return 1000*(Integer.parseInt(split[0])*60+Integer.parseInt(split[1]));
			}
			catch(Exception e)
				{ 
				return DEFAULT_PAUSE; 
				}
	}
	
	public static String getPauseTimeAsString() {
		return shared_prefs.getString(KEY_VOICE_PAUSE,"00:05");
	}
	
	public static boolean isFlightTimeEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_FLIGHTTIME, false);
	}

	public static boolean isVoiceEnabled() {
		return shared_prefs.getBoolean(KEY_VOICE_ENABLED, false);
	}
	
	public static boolean isVoiceAltEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_ALT, false);
	}
	
	public static boolean isVoiceMaxAltEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_MAX_ALT, false);
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

	public static boolean isVoiceRCLostEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_RCLOST, false);
	}

	public static boolean isConnectionInfoEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_CONNINFO, false);
	}
	
	public static boolean isDistance2TargetEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_DISTANCE2TARGET, false);
	}
	
	public static boolean isDistance2HomeEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_DISTANCE2HOME, false);
	}
	
	public static boolean isSpeedEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_SPEED, false);
	}
	
	public static boolean isMaxSpeedEnabled() {
		return shared_prefs.getBoolean(KEY_DO_VOICE_MAX_SPEED, false);
	}
	
	public static int getStreamId() {
		int res=getAllStreamNamesAsVector().indexOf(getStreamName());
		
		if (res==-1)
			return 0; // default if not found 
		
		return res;
	}
	
	public static String getStreamName() {
		return shared_prefs.getString(KEY_VOICE_STREAM,"no_name");
	}
	
	public static String[] getAllStreamNames() {
		return new String[] { "Alarm","Notification","Music","System","Voice Call" };
	}

	public static Vector<String> getAllStreamNamesAsVector() {
		Vector<String> res=new Vector<String>();
		for (String s:getAllStreamNames())
			res.add(s);
		return res;
	}
	
	public static int[] getAllStreamEnums() {
		return new int[] { AudioManager.STREAM_ALARM,AudioManager.STREAM_NOTIFICATION,AudioManager.STREAM_MUSIC,AudioManager.STREAM_SYSTEM,AudioManager.STREAM_VOICE_CALL };
	}

	public static int getStreamEnum() {
		return getAllStreamEnums()[ getStreamId()];
	}
}
