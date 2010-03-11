/**************************************************************************
 *                                          
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

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class DUBwiseBaseActivity extends Activity implements
		OnSharedPreferenceChangeListener {

	public SharedPreferences shared_prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		shared_prefs = getSharedPreferences("DUBwise", 0);

		update_ui_preferences();

		//shared_prefs.registerOnSharedPreferenceChangeListener(this);
	}

	public void update_ui_preferences() {

		System.out.println("updating shared preferences");
		if (!shared_prefs.getBoolean("title_bar", true))
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		else
			requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (shared_prefs.getBoolean("fullscreen", true)) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

	}

	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		update_ui_preferences();
	}
}
