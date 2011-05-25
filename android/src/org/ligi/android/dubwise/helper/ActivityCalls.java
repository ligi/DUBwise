/**************************************************************************
 *                                          
 * Class for common calls within a DUBwise Activity
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

package org.ligi.android.dubwise.helper;

import org.ligi.android.dubwise.DUBwisePrefs;
import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.StartupConnectionService;
import org.ligi.android.dubwise.blackbox.BlackBox;
import org.ligi.android.dubwise.blackbox.BlackBoxPrefs;
import org.ligi.android.dubwise.map.MapPrefs;
import org.ligi.android.dubwise.voice.StatusVoice;
import org.ligi.android.dubwise.voice.VoicePrefs;
import org.ligi.tracedroid.logging.Log;

import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ActivityCalls {

	private static WakeLock mWakeLock;
	public static boolean did_init=false;
	
	public static void beforeContent(Activity activity) {
		DUBwisePrefs.init(activity);
		activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		if (DUBwisePrefs.keepLightNow()) {
			if (mWakeLock==null) {
				final PowerManager pm = (PowerManager) (activity.getSystemService(Context.POWER_SERVICE)); 
				mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "DUBwise"); 
			} 
			mWakeLock.acquire();
		}
		
		// do only once
		if (!did_init){
			//BluetoothMaster.init(activity);
			VoicePrefs.init(activity);
			MapPrefs.init(activity);
			StatusVoice.getInstance().init(activity);
			BlackBoxPrefs.init(activity);

			if (VoicePrefs.isVoiceEnabled())
				DUBwiseBackgroundHandler.getInstance().addAndStartTask(StatusVoice.getInstance());

			// start the default connection
			StartupConnectionService.start(activity);
			
			if (BlackBoxPrefs.isBlackBoxEnabled())
					DUBwiseBackgroundHandler.getInstance().addAndStartTask(BlackBox.getInstance());		
			
			did_init=true;
		}
	}
	
	public static void onDestroy(Activity activity) {
		if ((mWakeLock!=null)&&(mWakeLock.isHeld()))
			mWakeLock.release();
	}
	
	private static void setCustomTitle(boolean set_new,int value,Activity activity) {
		try {
		// retrieve value for
		//com.android.internal.R.id.title_container
		int titleContainerId = (Integer) Class.forName(
		"com.android.internal.R$id").getField
		("title_container").get(null);

		// remove all views from titleContainer
		((ViewGroup) activity.getWindow().findViewById
				(titleContainerId)).removeAllViews();

		// add new custom title view
		if (set_new) 
			{((ViewGroup) activity.getWindow().findViewById
					(titleContainerId)).setVisibility(View.VISIBLE	);
			activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, value);
			}
		else
			((ViewGroup) activity.getWindow().findViewById
					(titleContainerId)).setVisibility(View.GONE);
		} catch(Exception ex) {}
	} // end of setCustomTitle
	
	public static void afterContent(Activity activity) {
		
		Log.i("fullscreen:" + DUBwisePrefs.isFullscreenEnabled());
		Log.i("status:" + DUBwisePrefs.isStatusBarEnabled());
		
		activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top);
		
		Log.i("oncreate top view" + activity.getWindow().findViewById(R.layout.top));

		if (DUBwisePrefs.isStatusBarEnabled())
			setCustomTitle(true,R.layout.top,activity);
		else
			setCustomTitle(false,R.layout.top,activity);
		
		//	activity.getWindow().findViewById(R.layout.text).setVisibility(View.VISIBLE);
		//else
		//	activity.requestWindowFeature(Window.FEATURE_NO_TITLE);

		/*activity.findViewById(R.layout.text).setVisibility(View.GONE);
		*/
		
		if (DUBwisePrefs.isFullscreenEnabled())
			activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		else
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		// http://www.anddev.org/viewtopic.php?p=12381
	}

	public static SharedPreferences getSharedPreferences(Activity activity) {
		return activity.getSharedPreferences("DUBwise", 0);
	}
}
