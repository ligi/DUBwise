/**************************************************************************
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
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise.map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.ligi.tracedroid.logging.Log;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Activity to show the FlightPlan set HoldTimes and delete WayPoints
 * 
 * @author Marcus -LiGi- Bueschleb   
 *
 */
public class ShowFlightPlanActivity extends Activity implements OnClickListener {
	private static final int MENU_SAVE = 0;
	private static final int MENU_LOAD = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createContent() ;
	}
	
	
	public void createContent() {
		LinearLayout lin=new LinearLayout(this);
		
		lin.setOrientation(LinearLayout.VERTICAL);
		
		ScrollView sv=new ScrollView(this);
		int wp_id=0;	
		for (WayPoint p:FlightPlanProvider.getWPList())
		{
			wp_id++;
			TextView tv=new TextView(this);
			tv.setText("wp: " + wp_id +  "lat: " + p.getGeoPoint().getLatitudeE6()/1000000.0 + " lon: " + p.getGeoPoint().getLongitudeE6()/1000000.0);
			
			ImageButton del_btn=new ImageButton(this);
			
			lin.addView(tv);
			
			LinearLayout actions_lin=new LinearLayout(this);
			
			del_btn.setTag(wp_id);
			del_btn.setOnClickListener(this);
			del_btn.setImageResource(android.R.drawable.ic_menu_delete);
			actions_lin.addView(del_btn);
			
			
			EditText hold_time_et=new EditText(this);
			hold_time_et.setText("" + p.getHoldTime());
			hold_time_et.setInputType(InputType.TYPE_CLASS_NUMBER);
			hold_time_et.setTag(wp_id);
			class MyTextWatcher implements TextWatcher {

				int id=0;
				public MyTextWatcher(int id) {
					this.id=id;
				}
				@Override
				public void afterTextChanged(Editable s) {
					
					try{
						int txt_val=Integer.parseInt(s.toString());
						FlightPlanProvider.getWPList().get(id-1).setHoldTime(txt_val);
					}catch(Exception e){};
						
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
								}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					
				}
				
			}
			
			hold_time_et.addTextChangedListener(new MyTextWatcher(wp_id) );

			actions_lin.addView(hold_time_et);
			
			lin.addView(actions_lin);
		}
		sv.addView(lin);
		setContentView(sv);
	}
	@Override
	public void onClick(View v) {
		if (v instanceof ImageButton)
		{
			int id=(Integer)v.getTag()-1;
			FlightPlanProvider.getWPList().remove(id);
			createContent() ;
		}
	}
	
	/* Creates the menu items */
	public boolean onPrepareOptionsMenu(Menu menu) {

		menu.clear();
	
		menu.add(0,MENU_SAVE,0,"Save").setIcon(android.R.drawable.ic_menu_save);
		menu.add(0,MENU_LOAD,0,"Load").setIcon(android.R.drawable.ic_menu_more);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {

		
	    switch (item.getItemId()) {
	    	case MENU_LOAD:
	    		this.startActivity(new Intent(this,GPXListActivity.class));
	    		finish();
	    		break;
	    case MENU_SAVE:
	    	final EditText input = new EditText(this);   
			input.setText("default");

			new AlertDialog.Builder(this).setTitle("Save GPX").setMessage("How should the file I will write to " +MapPrefs.getGPXPath() + " be named?").setView(input)
			.setPositiveButton("OK" , new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString(); 
					
				File f = new File(MapPrefs.getGPXPath());
				
				if (!f.isDirectory())
					f.mkdirs();
				
				try {
					f=new File(MapPrefs.getGPXPath() + "/"+value+".gpx");
					f.createNewFile();
					
					FileWriter sgf_writer = new FileWriter(f);
					
					BufferedWriter out = new BufferedWriter(sgf_writer);
					
					out.write(FlightPlanProvider.toGPX());
					out.close();
					sgf_writer.close();
				} catch (IOException e) {
					Log.i(""+e);
				}
	
			}
			}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			// Do nothing.
			}
			}).show();
	    	return true;
	    }
	    return true;
	}


}
