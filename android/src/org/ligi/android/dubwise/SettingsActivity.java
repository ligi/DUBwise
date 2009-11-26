package org.ligi.android.dubwise;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.app.Activity;
import android.content.SharedPreferences;

public class SettingsActivity extends Activity implements OnCheckedChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);		
		setContentView(R.layout.general_settings);
		
		((CheckBox) findViewById(R.id.FullScreenCheckBox)).setChecked(ActivityCalls.getSharedPreferences(this).getBoolean("do_fullscreen",true));
		((CheckBox) findViewById(R.id.TitleBarCheckBox)).setChecked(ActivityCalls.getSharedPreferences(this).getBoolean("do_title",true));
		
		((CheckBox) findViewById(R.id.FullScreenCheckBox)).setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.TitleBarCheckBox)).setOnCheckedChangeListener(this);
		
		ActivityCalls.afterContent(this);		
	}
	


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		update();
	}


	public void update() {
		SharedPreferences.Editor editor = ActivityCalls.getSharedPreferences(this).edit();

		editor.putBoolean("do_fullscreen",
				(((CheckBox) findViewById(R.id.FullScreenCheckBox)).isChecked()));
	
		editor.putBoolean("do_title",
				(((CheckBox) findViewById(R.id.TitleBarCheckBox)).isChecked()));
	
		
		editor.commit();

	}

	@Override
	public void onCheckedChanged(CompoundButton btn, boolean arg1) {
		update();
		//update_ui_preferences();
		//update();
		ActivityCalls.afterContent(this);
	}


}
