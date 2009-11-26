package org.ligi.android.dubwise;

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

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		update_ui_preferences();
	}
}
