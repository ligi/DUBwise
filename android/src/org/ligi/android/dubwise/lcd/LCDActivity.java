/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ligi.android.dubwise.lcd;

import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.ufo.MKCommunicator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class LCDActivity extends Activity implements OnTouchListener {
	
	LCDView lcd_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		
		MKProvider.getMK().user_intent=MKCommunicator.USER_INTENT_LCD;
		lcd_view=new LCDView(this);
		lcd_view.setOnTouchListener(this);
		setContentView(lcd_view);
		 // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, new GraphView(this));
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);	
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public final static int MENU_PREV=0;
	public final static int MENU_NEXT=1;
	
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    
		MenuItem prev_menu=menu.add(0,MENU_PREV,0,"Previous");
		prev_menu.setIcon(android.R.drawable.btn_minus);
		
		MenuItem next_menu=menu.add(0,MENU_NEXT,0,"NEXT");
		next_menu.setIcon(android.R.drawable.btn_plus);
		
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	    case MENU_NEXT:
	    	MKProvider.getMK().LCD.LCD_NEXTPAGE();
	    	return true;
	    case MENU_PREV:
	    	MKProvider.getMK().LCD.LCD_PREVPAGE();
	    	return true;
	    }
	    return false;
	}

	//@Override
	public boolean onTouch( View v, MotionEvent event ) {
		Log.i("DUBwise","LCD Touch");
		if (event.getAction()==MotionEvent.ACTION_UP)
		{
		if (event.getX()>lcd_view.getWidth()/2) {
			
			Log.i("DUBwise","LCD Nextpage");
			MKProvider.getMK().LCD.LCD_NEXTPAGE();
		}
		else
			MKProvider.getMK().LCD.LCD_PREVPAGE();
		lcd_view.invalidate();
		}
		return true;
	 }
}