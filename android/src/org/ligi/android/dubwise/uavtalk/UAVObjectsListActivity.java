/**************************************************************************
 *                                                                       
 * Author:  Marcus -LiGi- Bueschleb   
 *  http://ligi.de
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

package org.ligi.android.dubwise.uavtalk;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.UAVObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Activity to List all UAVObjecs
 *  
 * @author ligi the UAVObjects
 *
 */
public class UAVObjectsListActivity extends ListActivity {

		
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UAVTalkPrefs.init(this);
		
		ActivityCalls.beforeContent(this);
		UAVObjects.init();
		this.setListAdapter(new UAVObjectsArrayAdapter(this, android.R.layout.simple_list_item_1, UAVObjects.getUAVObjectArray()));
	}

	@Override 
	public void onResume() {
		ActivityCalls.afterContent(this);
		super.onResume();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
    	Intent fieldListActivity=new Intent(this,UAVObjectFieldListActivity.class);
    	fieldListActivity.putExtra("objid", UAVObjects.getUAVObjectArray()[position].getObjID());
    	startActivity(fieldListActivity);

	}
	
	class UAVObjectsArrayAdapter extends ArrayAdapter<UAVObject> {

		private Context context;
		private UAVObject[] objects;
		
		public UAVObjectsArrayAdapter(Context context, int textViewResourceId, UAVObject[] objects) {
			super(context, textViewResourceId, objects);
			this.context=context;
			this.objects=objects;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) { 
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View row;
			if (UAVTalkPrefs.isUAVObjectDescriptionDisplayEnabled()) {
				row=vi.inflate(R.layout.listitem_w_topic_and_descr, null);
				TextView label=(TextView)row.findViewById(R.id.topic); 
	            label.setText(((UAVObject)(objects[position])).getObjName());

	            TextView descr=(TextView)row.findViewById(R.id.descr); 
	            descr.setText(((UAVObject)(objects[position])).getObjDescription());

			}
			else {
				row=vi.inflate(R.layout.listitem_w_text, null);
				TextView label=(TextView)row.findViewById(R.id.Label); 
	            label.setText(((UAVObject)(objects[position])).getObjName());
			}      
            return row;
		}		
	}
	
	@Override
	protected void onDestroy() {
		ActivityCalls.onDestroy(this);
		super.onDestroy();
	}
}
