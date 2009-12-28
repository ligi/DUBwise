package org.ligi.android.dubwise;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
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

		int act_paramset=MKProvider.getMK().params.act_paramset;
		
		
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent edit_intent= new Intent( this, FlightSettingsTopicEditActivity.class );
		edit_intent.putExtra("topic",position );
		startActivity( edit_intent);
        
				// Start the activity

	}

}
