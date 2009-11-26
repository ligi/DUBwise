package org.ligi.android.dubwise;

import android.os.Bundle;
import android.widget.CheckBox;


import android.app.Activity;

import android.content.SharedPreferences;

public class GraphSettingsActivity extends Activity {

	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		
		SharedPreferences settings = getSharedPreferences("DUBWISE", 0);

		setContentView(R.layout.graph_settings);
		
		((CheckBox) findViewById(R.id.LegendCheckBox)).setChecked(settings.getBoolean("do_legend",true));
		((CheckBox) findViewById(R.id.GridCheckBox)).setChecked(settings.getBoolean("do_grid",true));
		System.out.println("do grid" + settings.getBoolean("do_grid",true));

	}

	@Override
	protected void onResume() {
	
		super.onResume();
		ActivityCalls.afterContent(this);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		SharedPreferences settings = getSharedPreferences("DUBWISE", 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putBoolean("do_grid",
				(((CheckBox) findViewById(R.id.GridCheckBox)).isChecked()));
		editor.putBoolean("do_legend",
				(((CheckBox) findViewById(R.id.LegendCheckBox)).isChecked()));

		System.out.println("saving !!! " + (((CheckBox) findViewById(R.id.LegendCheckBox)).isChecked()));
		editor.commit();
	}

}
