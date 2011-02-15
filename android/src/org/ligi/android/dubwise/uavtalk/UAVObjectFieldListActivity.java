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
import org.openpilot.uavtalk.UAVObjectFieldDescription;
import org.openpilot.uavtalk.UAVObjects;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Activity to List the Fields of an UAVObject with unit and value 
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UAVObjectFieldListActivity extends ListActivity implements Runnable{

	private int objid; 
	private UAVObjectsFieldDescriptionArrayAdapter my_adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		objid=this.getIntent().getIntExtra("objid", 0);
		
		ActivityCalls.beforeContent(this);
		UAVObjects.init();
		my_adapter=new UAVObjectsFieldDescriptionArrayAdapter(this, android.R.layout.simple_list_item_1, UAVObjects.getObjectByID(objid).getFieldDescriptions());
		this.setListAdapter(my_adapter);
		
		new Thread(this).start();
	}
	
	@Override
	public void onDestroy() {
		ActivityCalls.onDestroy(this);
		thread_running=false;
		super.onDestroy();

	}

	@Override 
	public void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (UAVObjects.getObjectByID(objid).getFieldDescriptions()[position].getElementNames().length>1) {
			Intent fieldArrayListActivity=new Intent(this,UAVObjectFieldArrayListActivity.class);
			fieldArrayListActivity.putExtra("objid", objid);
			fieldArrayListActivity.putExtra("fieldid", position);
	    	startActivity(fieldArrayListActivity);
		} else {
			UAVObjectFieldEdit.editField(this,objid, position,(byte)0,my_adapter);
		}
	}
	
	
	class UAVObjectsFieldDescriptionArrayAdapter extends ArrayAdapter<UAVObjectFieldDescription> {

		private Context context;
		private UAVObjectFieldDescription[] objects;
		
		public UAVObjectsFieldDescriptionArrayAdapter(Context context, int textViewResourceId, UAVObjectFieldDescription[] objects) {
			super(context, textViewResourceId, objects);
			this.context=context;
			this.objects=objects;
			//this.notifyDataSetChanged()
		}
		
		public View getView(int position, View convertView, ViewGroup parent) { 
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=vi.inflate(R.layout.listitem_w_text, null); 
            TextView label=(TextView)row.findViewById(R.id.Label); 
            UAVObjectFieldDescription act_obj=((UAVObjectFieldDescription)(objects[position]));
            String txt=act_obj.getName() + ": ";  
            
            if (act_obj.getElementNames().length>1) {
            	txt+=">>";
            }
            else {
                txt+=UAVObjectFieldHelper.getFieldValueStr(act_obj);
                if (UAVTalkPrefs.isUAVTalkUnitDisplayEnabled())
                	txt+=" " + act_obj.getUnit();
            }
            
			label.setText(txt  );
	
            return row;
		}		
	}

	
	/* section to notify the adapter of changes */
	private Handler hndl=new Handler();
	private boolean thread_running=true;
	
	@Override
	public void run() {
		while (thread_running) {
			hndl.post(new Runnable() {
				@Override
				public void run() {
					my_adapter.notifyDataSetChanged();
				}
			});
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}
}
