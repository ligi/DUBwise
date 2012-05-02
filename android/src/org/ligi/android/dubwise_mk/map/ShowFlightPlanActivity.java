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

package org.ligi.android.dubwise_mk.map;

import org.ligi.android.common.activitys.RefreshingStringListActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Activity to show the FlightPlan set HoldTimes and delete WayPoints
 * 
 * @author Marcus -LiGi- Bueschleb   
 *
 */
public class ShowFlightPlanActivity extends RefreshingStringListActivity {
	private static final int MENU_SAVE = 0;
	private static final int MENU_LOAD = 1;

	/*
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
			tv.setText("#" + wp_id +  " lat: " + p.getGeoPoint().getLatitudeE6()/1000000.0 + " lon: " + p.getGeoPoint().getLongitudeE6()/1000000.0 );
			
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
	
	*/
	
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
		    	GPXHelper.show_save_dlg(this);
		    	return true;
	    }
	    return true;
	}

	@Override
	public String getStringByPosition(int pos) {
		if (pos>=FlightPlanProvider.getWPList().size())	
			return null;
		AndroidWayPoint wp =FlightPlanProvider.getWPList().get(pos);
		
		return "#" +pos + " lat: " + wp.getGeoPoint().getLatitudeE6()/1000000.0 + " lon: " + wp.getGeoPoint().getLongitudeE6()/1000000.0 + " ht:" + wp.getHoldTime()
		+ " ev:" + wp.getChannelEvent() + " radius:" + wp.getToleranceRadius();
		
	}


}
