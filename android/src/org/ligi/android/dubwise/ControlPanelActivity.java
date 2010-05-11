/**************************************************************************
 *                                          
 * Activity to a Control Panel
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

package org.ligi.android.dubwise;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.tracedroid.logging.Log;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.app.Activity;

public class ControlPanelActivity extends Activity implements OnCheckedChangeListener, OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);		
		setContentView(R.layout.control_panel);

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
				Log.i("switching paramset to " + setting_id);
			}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCalls.onDestroy(this);
	}

}
