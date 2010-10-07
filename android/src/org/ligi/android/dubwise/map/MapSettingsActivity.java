/**************************************************************************
 *                                          
 * this file is a part of DUBwise
 *                            
 * Author:  Marcus -LiGi- Bueschleb   
 * http://ligi.de
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

package org.ligi.android.dubwise.map;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.helper.ActivityCalls;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Settings Activity for the DUBwise Map
 * 
 * @author ligi
 *
 */
public class MapSettingsActivity extends Activity implements OnCheckedChangeListener, OnItemSelectedListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);		
		setContentView(R.layout.map_settings);
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
		editor.commit();
	}

	public void onCheckedChanged(CompoundButton btn, boolean arg1) {
		update();
		ActivityCalls.afterContent(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		update();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}