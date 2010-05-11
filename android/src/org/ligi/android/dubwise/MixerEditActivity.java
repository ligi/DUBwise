/**************************************************************************
 *                                          
 * Activity to edit the Mixer Table
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

package org.ligi.android.dubwise;



import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.MixerManager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView.OnEditorActionListener;

public class MixerEditActivity extends Activity implements OnItemSelectedListener, OnCheckedChangeListener, TextWatcher, OnEditorActionListener, KeyListener {

	private static final int MENU_LOAD = 0;
	private static final int MENU_HELP = 1;
	private static final int MENU_SAVE_TO_FC = 2;
	private static final int MENU_SAVE_TO_PHONE = 3;

	
	String[] menu_items;
	
	// 	each is tagged with the id of the row
	Spinner spinners[];
	EditText edit_texts[][];
	CheckBox checkboxes[];
	int act_topic;
	EditText name_edit;
	TableLayout table;
	
	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActivityCalls.beforeContent(this);
/*
		menu_items=new String[MKProvider.getMK().params.tab_stringids.length];
		for (int i=0;i<menu_items.length;i++)
		menu_items[i]=getString(DUBwiseStringHelper.table[MKProvider.getMK().params.tab_stringids[i]]);
		*/     
		
		ScrollView view=new ScrollView(this);
		
		table=new TableLayout(this);
		
		LayoutParams lp=new TableLayout.LayoutParams();
		lp.width=LayoutParams.FILL_PARENT;
		table.setLayoutParams(lp);
		
		//table.setColumnStretchable(1, true);
		
		
		TableRow name_row=new TableRow(this);

		TextView name_label=new TextView(this);
		name_label.setFocusableInTouchMode(true); // so that keyboard won't pop up
		name_label.setText("Name:");
		name_row.addView(name_label);
		
		name_edit=new EditText(this);
		
		TableRow.LayoutParams layout_p=(new TableRow.LayoutParams());
		layout_p.span=4;
		
		name_edit.setLayoutParams(layout_p);
		name_edit.setSingleLine();
		name_row.addView(name_edit);
		
		table.addView(name_row);
		
		for (int c=1;c<5;c++)
			table.setColumnStretchable(c, true);
		
		TableRow top_row=new TableRow(this);
		
		String[] top_strings={"","Gas","Nick","Roll","Yaw"};
		
		for (byte str_id=0 ; str_id<top_strings.length;str_id++){
		
			TextView txt_view=new TextView(this);
			txt_view.setText(top_strings[str_id]);	
			top_row.addView(txt_view);
		}
		
		table.addView(top_row);
		edit_texts=new EditText[12][4];
		
		for (byte edit_id=0;edit_id<12;edit_id++)
		{
			TableRow new_row = new TableRow(this);
			TextView txt_view = new TextView(this);
			txt_view.setText("Motor"+(edit_id+1));
			new_row.addView(txt_view);
		
			for (int type=0;type<4;type++) {
				edit_texts[edit_id][type]=new EditText(this);
				edit_texts[edit_id][type].setInputType(InputType.TYPE_CLASS_NUMBER);
				
				new_row.addView(edit_texts[edit_id][type]);
			}
			table.addView(new_row);
		}
		copyMixerManagerValues2Layout( ) ;
		/*
        for (int i=0;i<menu_items.length;i++)
        {
        	TableRow row=new TableRow(this);
        	table.addView(row);
			row.setPadding(0, 5, 0, 5);
			TextView text_v=new TextView(this);
        	text_v.setText(DUBwiseStringHelper.table[MKProvider.getMK().params.field_stringids[act_topic][i]]);
        	text_v.setMinHeight(50);
        	text_v.setPadding(3, 0, 5, 0);
        	row.addView(text_v);
        
        	//text_v.addTextChangedListener(this);
        	//text_v.setOnEditorActionListener(this);
        	row.setTag(i);
        	MKParamsParser params=MKProvider.getMK().params;
        	switch(MKProvider.getMK().params.field_types[act_topic][i]) {

        	case MKParamDefinitions.PARAMTYPE_STICK:
        		Spinner spinner=new Spinner(this);
        		String stick_strings[]=new String[12];
        		
        		for (int stick=0;stick<12;stick++)
        			stick_strings[stick]=""+stick;

         		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
        	            android.R.layout.simple_spinner_item , stick_strings);
         		spinner.setAdapter(spinner_adapter);
        		
        		row.addView(spinner);
        		break;

        	case MKParamDefinitions.PARAMTYPE_BITSWITCH:
        	
        		checkboxes[i]=new CheckBox(this);
        		checkboxes[i].setTag(i);
        		checkboxes[i].setOnCheckedChangeListener(this);
        		checkboxes[i].setChecked(!((MKProvider.getMK().params.get_field_from_act(MKProvider.getMK().params.field_positions[act_topic][i]/8)&(1<<MKProvider.getMK().params.field_positions[act_topic][i]%8))==0));
        		row.addView(checkboxes[i]);
        		break;

        	case MKParamDefinitions.PARAMTYPE_BITMASK:
        		row.addView(new EditText(this));
        		LinearLayout lin=new LinearLayout(this);
        		for (int bm=0;bm<8;bm++)
        			lin.addView(new CheckBox(this));
        		TableRow new_row=new TableRow(this);
        		TableRow.LayoutParams layout_p=(new TableRow.LayoutParams());
        		layout_p.span=3;
        		lin.setLayoutParams(layout_p);
        		new_row.addView(lin);
        		table.addView(new_row);
        		new_row.setVisibility(View.GONE);
        		break;

        		
        	case MKParamDefinitions.PARAMTYPE_MKBYTE:
        		edit_texts[i]=new EditText(this);
        		edit_texts[i].setText("" +params.field[params.act_paramset][params.field_positions[act_topic][i]] );
        		
        		edit_texts[i].setTag(new Integer(i));
        		edit_texts[i].setInputType(InputType.TYPE_CLASS_NUMBER);
//        		edit_texts[i].addTextChangedListener(this);
        		//edit_texts[i].setKeyListener(this);
        		final int act_i=i;
        		
        		edit_texts[i].addTextChangedListener(new TextWatcher() {

					@Override
					public void afterTextChanged(Editable s) {
						try{
							int txt_val=Integer.parseInt(s.toString());
							if (txt_val>255)
								txt_val=255;
							else if (txt_val<0)
								txt_val=0;
							MKProvider.getMK().params.field[MKProvider.getMK().params.act_paramset][MKProvider.getMK().params.field_positions[act_topic][act_i]]=txt_val;						// TODO Auto-generated method stub
							Log.d("!!!" , "" + txt_val);
							
							if (!(""+txt_val).equals(edit_texts[act_i].getText().toString()))
								edit_texts[act_i].setText( "" + txt_val);
						}catch(Exception e){};
							
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
									}

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						
					}});
        		//edit_texts[i].set
        		
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
        		break;
        	}
        	
        }
        */
		
        view.addView(table);
        this.setContentView(view);
	}

	@Override 
	public void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	/**
	 * copy the values in MixerManager to the Layout
	 */
	public void copyMixerManagerValues2Layout() {
		for (byte type=0;type<4;type++) {
			byte[]	data= MKProvider.getMK().mixer_manager.getValuesByType(type);
			
			for (int row=0;row<12;row++)					
				edit_texts[row][type].setText(""+data[row]);
			}
		
		name_edit.setText(MKProvider.getMK().mixer_manager.getName());

		table.requestLayout(); // relayout the table 
	}


	/**
	 * copy the values in the Layout to the MixerManager
	 */
	public void copyLayoutValues2MixerManager() {
		byte[]	data= new byte[12];
		
		for (byte type=0;type<4;type++) 
			for (int row=0;row<12;row++)					
				{
				data[row]=(byte)Integer.parseInt(""+edit_texts[row][type].getText());
				MKProvider.getMK().mixer_manager.setValuesByType(type, data);
				}
		
		MKProvider.getMK().mixer_manager.setName(""+name_edit.getText());
		
	}

	
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	    case MENU_SAVE_TO_FC:
	    	copyLayoutValues2MixerManager();
	    	MKProvider.getMK().set_mixer_table( MKProvider.getMK().mixer_manager.getFCArr());
	    	break;
	    	
	    case MENU_SAVE_TO_PHONE:
	    	/*Map<String,?> mixer=this.getSharedPreferences("foo", 0).getAll();
	    	SharedPreferences.Editor edit= this.getSharedPreferences(SHARED_PREFS_MIXER_TAG, 0).edit();
	    	edit.p .putString(arg0, arg1)
	    	
	    	"as".sp
	    	*/
	    	break;
	    case MENU_HELP:
	    	this.startActivity(new Intent( "android.intent.action.VIEW", 
	        		Uri.parse( "http://mikrokopter.de/ucwiki/MK-Parameter/Mixer-SETUP")));
	    	break;
	    case MENU_LOAD:
	    	
	    	final Spinner spinner=new Spinner(this);
    		
	    	ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
    	            android.R.layout.simple_spinner_item , MixerManager.getDefaultNames());
	    	spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    	spinner.setAdapter(spinner_adapter);
    		

			new AlertDialog.Builder(this).setTitle("Load Mixer").setMessage("What Mixer setup should i load?").setView(spinner)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
		//		String value = input.getText().toString(); 
				byte id=(byte) spinner.getSelectedItemId();
				MKProvider.getMK().mixer_manager.setDefaultValues(id);
				copyMixerManagerValues2Layout( ) ;
			}
			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			// Do nothing.
			}
			}).show();

			
	    	
	    	return true;
	    }
	    return false;
	}





	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		try {
		Log.d("on item sel ");
		if (arg1!=null) {
		// view has a parent
		if (arg1.getParent()!=null)
		{/*
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
			}*/
				
		}
		}
		}
		catch (Exception e) { Log.d("DD" + e); }
		
		//arg1.getParent()
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

		Log.d( ""+arg0.getTag());
		
	}
	

	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem write_menu=menu.add(0,MENU_SAVE_TO_FC,0,"Write to FC");
		write_menu.setIcon(android.R.drawable.ic_menu_save);
		
		/*MenuItem write_phone_menu=menu.add(0,MENU_SAVE_TO_PHONE,0,"Save on Phone");
		write_phone_menu.setIcon(android.R.drawable.ic_menu_save);
		*/
		
		
		MenuItem load_menu=menu.add(0,MENU_LOAD,0,"Load");
		load_menu.setIcon(android.R.drawable.ic_menu_set_as);
	
		MenuItem help_menu=menu.add(0,MENU_HELP,0,"Help");
		help_menu.setIcon(android.R.drawable.ic_menu_help);
	
		return true;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		Log.d( "text cahnged" + s );
		Log.d( "text cahnged" + ((View)s).getTag() );
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		Log.d( "text cahnged2" + v.getText() );
		return false;
	}

	@Override
	public void clearMetaKeyState(View view, Editable content, int states) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInputType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onKeyDown(View view, Editable text, int keyCode,
			KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onKeyOther(View view, Editable text, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


}
