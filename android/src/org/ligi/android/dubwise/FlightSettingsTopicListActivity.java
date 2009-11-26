package org.ligi.android.dubwise;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.util.Log;

import android.net.Uri;

import android.widget.ArrayAdapter;

//import com.google.android.maps.MapView;

import android.content.SharedPreferences;

import org.ligi.ufo.MKCommunicator;

public class FlightSettingsTopicListActivity extends ListActivity {

	String[] menu_items;
	
	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		menu_items=new String[2];
		menu_items[0]=""+MKProvider.getMK().params.tab_stringids[0];
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

				// Start the activity

	}

}
