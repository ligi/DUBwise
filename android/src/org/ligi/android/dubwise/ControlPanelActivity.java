package org.ligi.android.dubwise;

import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.app.Activity;
import android.content.SharedPreferences;

public class ControlPanelActivity extends Activity implements OnCheckedChangeListener, OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);		
		setContentView(R.layout.control_panel);
		/*
		((CheckBox) findViewById(R.id.FullScreenCheckBox)).setChecked(ActivityCalls.getSharedPreferences(this).getBoolean("do_fullscreen",true));
		((CheckBox) findViewById(R.id.TitleBarCheckBox)).setChecked(ActivityCalls.getSharedPreferences(this).getBoolean("do_title",true));
		((CheckBox) findViewById(R.id.ExpertMode)).setChecked(ActivityCalls.getSharedPreferences(this).getBoolean("expert",false));
		
		((CheckBox) findViewById(R.id.FullScreenCheckBox)).setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.TitleBarCheckBox)).setOnCheckedChangeListener(this);
*/
		((ToggleButton) findViewById(R.id.CommingHomeToggleButton)).setOnCheckedChangeListener(this);
		((ToggleButton) findViewById(R.id.PositionHoldToggleButton)).setOnCheckedChangeListener(this);
		((ToggleButton) findViewById(R.id.AltitudeHoldToggleButton)).setOnCheckedChangeListener(this);

		((Button) findViewById(R.id.Setting1Button)).setOnClickListener(this);
		((Button) findViewById(R.id.Setting2Button)).setOnClickListener(this);
		((Button) findViewById(R.id.Setting3Button)).setOnClickListener(this);
		((Button) findViewById(R.id.Setting4Button)).setOnClickListener(this);
		((Button) findViewById(R.id.Setting5Button)).setOnClickListener(this);

		ActivityCalls.afterContent(this);		
	}
	

	public void onCheckedChanged(CompoundButton btn, boolean checked) {
		ActivityCalls.afterContent(this);
		
		if(checked) {
		if ((btn==((ToggleButton) findViewById(R.id.CommingHomeToggleButton))))
				((ToggleButton) findViewById(R.id.PositionHoldToggleButton)).setChecked(false);
		

		if ((btn==((ToggleButton) findViewById(R.id.PositionHoldToggleButton))))
				((ToggleButton) findViewById(R.id.CommingHomeToggleButton)).setChecked(false);
		}
		
		
		
	}


	@Override
	public void onClick(View arg0) {
		
		int setting_id=0;
		
		if (arg0 == findViewById(R.id.Setting1Button))
			setting_id=1;
		else if (arg0 == findViewById(R.id.Setting2Button))
			setting_id=2;
		else if (arg0 == findViewById(R.id.Setting3Button))
			setting_id=3;
		else if (arg0 == findViewById(R.id.Setting4Button))
			setting_id=4;
		else if (arg0 == findViewById(R.id.Setting5Button))
			setting_id=5;
		
		if (setting_id!=0) 
			{
				MKProvider.getMK().set_active_paramset(setting_id);
				Log.i("DUBwise","switching paramset to " + setting_id);
			}
		
	}


}
