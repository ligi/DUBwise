/**************************************************************************
 *                                          
 * Activity to choose the topic which the user wants to 
 * edit from the FlightSettings 
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

package org.ligi.android.dubwise.flightsettings;

import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseStringHelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.util.Log;
import android.widget.ArrayAdapter;

public class FlightSettingsTopicListActivity extends ListActivity {

	String[] menu_items;
	
	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCalls.beforeContent(this);
		
		
		  menu_items=new String[MKProvider.getMK().params.tab_stringids.length];
	        for (int i=0;i<menu_items.length;i++)
	        	menu_items[i]=getString(DUBwiseStringHelper.table[MKProvider.getMK().params.tab_stringids[i]]);
	    
	        /*
		menu_items=new String[MKProvider.getMK().params.field_stringids[act_paramset][getIntent().getIntExtra("foo",0)]];
		
		
		
		for (int i=0;i<menu_items.length;i++)
		menu_items[i]=getString(DUBwiseStringHelper.table[MKProvider.getMK().params.field_stringids[act_paramset][0]]);
		*/      
		this.setListAdapter(new ArrayAdapter<String>(this,
		 android.R.layout.simple_list_item_1, menu_items));
	}

	public void log(String msg) {
		Log.d("DUWISE", msg);
	}

	public void quit() {
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menu_items));
		// setContentView(this);
	}


	@Override 
	public void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent edit_intent= new Intent( this, FlightSettingsTopicEditActivity.class );
		edit_intent.putExtra("topic",position );
		startActivity( edit_intent);
        
				// Start the activity

	}

}
