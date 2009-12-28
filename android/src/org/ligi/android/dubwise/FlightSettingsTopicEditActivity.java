package org.ligi.android.dubwise;

import java.util.HashMap;

import org.ligi.ufo.MKParamDefinitions;
import org.ligi.ufo.MKParamsParser;

import android.app.Activity;
import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FlightSettingsTopicEditActivity extends Activity implements OnItemSelectedListener, OnCheckedChangeListener {

	String[] menu_items;
	
	// 	each is tagged with the id of the row
	Spinner spinners[];
	EditText edit_texts[];
	CheckBox checkboxes[];
	int act_topic;
	
	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*
		menu_items=new String[MKProvider.getMK().params.tab_stringids.length];
		for (int i=0;i<menu_items.length;i++)
		menu_items[i]=getString(DUBwiseStringHelper.table[MKProvider.getMK().params.tab_stringids[i]]);
		*/     
		
		ScrollView view=new ScrollView(this);
		
		act_topic=getIntent().getIntExtra("topic",1);
		menu_items=new String[MKProvider.getMK().params.field_stringids[act_topic].length];
    
		spinners=new Spinner[menu_items.length];;
		edit_texts=new EditText[menu_items.length];;
        checkboxes=new CheckBox[menu_items.length];
		TableLayout table=new TableLayout(this);
		
		LayoutParams lp=new TableLayout.LayoutParams();
		lp.width=LayoutParams.FILL_PARENT;
		table.setLayoutParams(lp);
		
		table.setColumnStretchable(1, true);
		
        for (int i=0;i<menu_items.length;i++)
        {
        	Log.d("D","creating item" + i );
        	TableRow row=new TableRow(this);
        	table.addView(row);
			row.setPadding(0, 5, 0, 5);
			TextView text_v=new TextView(this);
        	text_v.setText(DUBwiseStringHelper.table[MKProvider.getMK().params.field_stringids[act_topic][i]]);
        	text_v.setMinHeight(50);
        	text_v.setPadding(3, 0, 5, 0);
        	row.addView(text_v);
        	
        	MKParamsParser params=MKProvider.getMK().params;
        	if(MKProvider.getMK().params.field_types[act_topic][i]==MKParamDefinitions.PARAMTYPE_BITSWITCH){
        		checkboxes[i]=new CheckBox(this);
        		checkboxes[i].setTag(i);
        		checkboxes[i].setOnCheckedChangeListener(this);
        		checkboxes[i].setChecked(!((MKProvider.getMK().params.get_field_from_act(MKProvider.getMK().params.field_positions[act_topic][i]/8)&(1<<MKProvider.getMK().params.field_positions[act_topic][i]%8))==0));
        		row.addView(checkboxes[i]);
        	}
        

        	if(MKProvider.getMK().params.field_types[act_topic][i]==MKParamDefinitions.PARAMTYPE_MKBYTE)
        	{
        		edit_texts[i]=new EditText(this);
        		edit_texts[i].setText("" +params.field[params.act_paramset][params.field_positions[act_topic][i]] );
        		
        		edit_texts[i].setTag(new Integer(i));
        		
        		spinners[i]=new Spinner(this);
        		String poti_strings[]=new String[6];
        		poti_strings[0]="no Poti";
        		for (int s=1;s<6;s++) 
        			poti_strings[s]="Poti " + s;
        		
         		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        	            android.R.layout.simple_spinner_item , poti_strings);
         		spinners[i].setAdapter(adapter);
        		spinners[i].setTag(new Integer(i));
         		
        	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        	    
        	    spinners[i].setOnItemSelectedListener(this);
        	    
        		row.addView(edit_texts[i]);
        		row.addView(spinners[i]);
        	}
        	
        }
        
		
        view.addView(table);
        this.setContentView(view);
	}

	public void log(String msg) {
		Log.d("DUWISE", msg);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		try {
		Log.d("D","on item sel ");
		if (arg1!=null) {
		// view has a parent
		if (arg1.getParent()!=null)
		{
			Log.d("D","item with parent" + arg1.getParent());
			if (arg1.getParent() instanceof Spinner)  {
				Spinner spin=(Spinner)arg1.getParent();
				
				Log.d("D","Spinner tag" + spin.getTag());
				
				if (arg2==0)
					edit_texts[(Integer)(spin.getTag())].setEnabled(true);
				else
				{
					int tag_id=(Integer)(spin.getTag());
					edit_texts[tag_id].setEnabled(false);
					edit_texts[tag_id].setText("" + (250+arg2));
					MKProvider.getMK().params.set_field_from_act(MKProvider.getMK().params.field_positions[act_topic][tag_id], 250+arg2);
				}
			}
				
		}
		}
		}
		catch (Exception e) { Log.d("D","DD" + e); }
		
		//arg1.getParent()
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

		Log.d("Checkbox " , ""+arg0.getTag());
		
	}

}
