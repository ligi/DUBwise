package org.ligi.android.dubwise;

import org.ligi.android.dubwise.helper.ActivityCalls;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.app.Activity;
import android.content.SharedPreferences;

public class SettingsActivity extends Activity implements OnCheckedChangeListener, OnItemSelectedListener {

	public final static int AWAKE_NEVER=0;
	public final static int AWAKE_CONN=1;
	public final static int AWAKE_ALWAYS=2;
	private final static String[] awake_strings={"never","when connected","always" };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);		
		setContentView(R.layout.general_settings);
		
		((CheckBox) findViewById(R.id.FullScreenCheckBox)).setChecked(ActivityCalls.getSharedPreferences(this).getBoolean("do_fullscreen",true));
		((CheckBox) findViewById(R.id.TitleBarCheckBox)).setChecked(ActivityCalls.getSharedPreferences(this).getBoolean("do_title",true));
		((CheckBox) findViewById(R.id.ExpertMode)).setChecked(ActivityCalls.getSharedPreferences(this).getBoolean("expert",false));
		
		((CheckBox) findViewById(R.id.FullScreenCheckBox)).setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.TitleBarCheckBox)).setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.ExpertMode)).setOnCheckedChangeListener(this);

		
		
		Spinner awake_spinner=((Spinner) findViewById(R.id.AwakeSpinner));
		awake_spinner.setOnItemSelectedListener(this);

		
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_spinner_item , awake_strings);

 		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

 		awake_spinner.setAdapter(spinner_adapter);
	
 		awake_spinner.setSelection(ActivityCalls.getSharedPreferences(this).getInt("awake", 0));
 		ActivityCalls.afterContent(this);		
	}
	
/*

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		update();
	}
*/

	public void update() {
		SharedPreferences.Editor editor = ActivityCalls.getSharedPreferences(this).edit();

		editor.putBoolean("do_fullscreen",
				(((CheckBox) findViewById(R.id.FullScreenCheckBox)).isChecked()));
	
		editor.putBoolean("do_title",
				(((CheckBox) findViewById(R.id.TitleBarCheckBox)).isChecked()));
	
		editor.putBoolean("expert",
				(((CheckBox) findViewById(R.id.ExpertMode)).isChecked()));
	
		editor.putInt("awake", ((Spinner) findViewById(R.id.AwakeSpinner)).getSelectedItemPosition() );
		editor.commit();

	}

	public void onCheckedChanged(CompoundButton btn, boolean arg1) {
		update();
		//update_ui_preferences();
		//update();
		ActivityCalls.afterContent(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		update();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}


}
