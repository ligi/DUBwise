package org.ligi.android.dubwise.map;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.tracedroid.logging.Log;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShowFlightPlanActivity extends Activity implements OnClickListener {
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

}
