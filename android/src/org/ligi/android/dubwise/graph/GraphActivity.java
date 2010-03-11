/**************************************************************************
 *                                          
 * Activity to Display the Graph
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

package org.ligi.android.dubwise.graph;

import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GraphActivity extends Activity implements OnTouchListener {

	GraphView graph_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		
		graph_view=new GraphView(this);
		graph_view.setOnTouchListener(this);
		
		setContentView( graph_view);

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

	public final static int MENU_GRID=0;
	public final static int MENU_FREEZE=1;
	public final static int MENU_SETTINGS=2;
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    
		MenuItem settings_menu=menu.add(0,MENU_SETTINGS,0,"Settings");
		settings_menu.setIcon(android.R.drawable.ic_menu_preferences);
		
		
		MenuItem freeze_menu=menu.add(0,MENU_FREEZE,0,(MKProvider.getMK().freeze_debug_buff?"unfreeze":"freeze"));
		freeze_menu.setIcon(android.R.drawable.ic_media_pause);
		/*
	    Menu features_menu=menu.addSubMenu("Features");
	    features_menu.add(0, MENU_LEGEND, 0, "New Game").setCheckable(true);
	    features_menu.add(0, MENU_GRID, 0, "Quit").setCheckable(true);
	    */
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
/*	    case MENU_LEGEND:
	    	item.setChecked(!item.isChecked());
	       // newGame();
	        return true;
	    case MENU_GRID:
	    	item.setChecked(!item.isChecked());
	        //quit();
	        return true;
	*/        
	    case MENU_FREEZE:
	    	MKProvider.getMK().freeze_debug_buff=!MKProvider.getMK().freeze_debug_buff;
	    	break;
	    	
	    case MENU_SETTINGS:
	        //quit();
	    	startActivity(new Intent(this, GraphSettingsActivity.class));
	        return true;
	    }
	    return false;
	}

	
	public boolean onTouch(View v, MotionEvent event) {
		Log.i("DUBwise","touch graph"+event.getAction() + " " + MotionEvent.ACTION_UP);
		if (event.getAction()==MotionEvent.ACTION_UP)
		{
			Log.i("DUBwise","touch graph up !!");
		
			MKProvider.getMK().freeze_debug_buff=!MKProvider.getMK().freeze_debug_buff;
		
		}
		return true;
//		return false;
	}

}