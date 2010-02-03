package org.ligi.android.dubwise.helper;

import org.ligi.android.dubwise.R;

import android.os.PowerManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ActivityCalls {

	
	public static void beforeContent(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		final PowerManager pm = (PowerManager) (activity.getSystemService(Context.POWER_SERVICE)); 
      /*  activity.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag"); 
        this.mWakeLock.acquire();
        
        */ 
		 pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag").acquire(); 
	        
	        
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
			activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, value);}
		else
			((ViewGroup) activity.getWindow().findViewById
					(titleContainerId)).setVisibility(View.GONE);
		} catch(Exception ex) {}
		
		
		}
	
	public static void afterContent(Activity activity) {
		System.out.println("oncreate activity" + activity);
		
		SharedPreferences shared_prefs = activity.getSharedPreferences("DUBwise", 0);
		
		System.out.println("fullscreen:" + (shared_prefs.getBoolean("fullscreen", true)));
		
		activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.top);
		
		
		{
		System.out.println("oncreate top view" + activity.getWindow().findViewById(R.layout.top));
		if (shared_prefs.getBoolean("do_title", true))
			setCustomTitle(true,R.layout.top,activity);
		else
			setCustomTitle(false,R.layout.top,activity);
		//	activity.getWindow().findViewById(R.layout.text).setVisibility(View.VISIBLE);
		//else
		//	activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		;;
		}
		/*activity.findViewById(R.layout.text).setVisibility(View.GONE);
		*/
		
		if (shared_prefs.getBoolean("do_fullscreen", true))
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


