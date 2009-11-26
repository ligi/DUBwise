package org.ligi.android.dubwise;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class DUBwiseBaseListActivity extends ListActivity implements OnSharedPreferenceChangeListener {

	public SharedPreferences shared_prefs;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		shared_prefs = getSharedPreferences("DUBwise", 0);

		update_ui_preferences();
		
		
//		 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        
//	      getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.text);


		
	//	shared_prefs.registerOnSharedPreferenceChangeListener(this);
	}

	public void update_ui_preferences() {

		if (!shared_prefs.getBoolean("title_bar", true))
			requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (shared_prefs.getBoolean("fullscreen", true))
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		update_ui_preferences();
	}
}
