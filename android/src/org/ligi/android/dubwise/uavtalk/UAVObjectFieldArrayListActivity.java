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

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Activity to list the elements of an array from an UAVObjects-field
 *  
 * @author ligi the UAVObjects
 *
 */
public class UAVObjectFieldArrayListActivity extends ListActivity {

	private int objid; 
	private int fieldid; 
	private myArrayAdapter ma;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		objid=this.getIntent().getIntExtra("objid", 0);
		fieldid=this.getIntent().getIntExtra("fieldid", 0);
		
		ActivityCalls.beforeContent(this);
		UAVObjects.init();
		ma=new myArrayAdapter(this, android.R.layout.simple_list_item_1, UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid].getElementNames());
		this.setListAdapter(ma);
	}

	@Override 
	public void onResume() {
		ActivityCalls.afterContent(this);
		super.onResume();
	}
	
    @Override
	protected void onDestroy() {
		ActivityCalls.onDestroy(this);
		super.onDestroy();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		UAVObjectFieldEdit.editField(this,objid, fieldid,(byte)position,ma);
	}
	
	class myArrayAdapter extends ArrayAdapter<String> {

		private Context context;
		public myArrayAdapter(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
			this.context=context;
		}


		public View getView(int position, View convertView, ViewGroup parent) { 
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=vi.inflate(R.layout.listitem_w_text, null); 
            TextView label=(TextView)row.findViewById(R.id.Label); 
         
            String txt=UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid].getElementNames()[position] + " : ";
            txt+=UAVObjectFieldHelper.getFieldValueStr(UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid], (byte)position);
            if (UAVTalkPrefs.isUAVTalkUnitDisplayEnabled())
            	txt+=" " +UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid].getUnit();
            label.setText(txt);
            return row;
            
		}
	}
}
