package org.ligi.android.dubwise.map;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
		int id=0;
		for (WayPoint p:FlightPlanProvider.getWPList())
		{
			id++;
			TextView tv=new TextView(this);
			tv.setText("wp: " + id +  "lat: " + p.getGeoPoint().getLatitudeE6()/1000000.0 + " lon: " + p.getGeoPoint().getLongitudeE6()/1000000.0);
			
			ImageButton del_btn=new ImageButton(this);
			del_btn.setTag(id);
			del_btn.setOnClickListener(this);
			del_btn.setImageResource(android.R.drawable.ic_menu_delete);
			lin.addView(tv);
			lin.addView(del_btn);
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
