package org.ligi.android.dubwise;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class DUBwiseMap extends MapActivity {
	
	LinearLayout linearLayout;
	MapView mapView;
	ZoomControls mZoom;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//mapView = new MapView(this, "05yE31rWh6bR5W3A9cOq2www1gYQr2XDq5ACbSg"); /* debug */
		
		
		mapView = new MapView(this, "0VahPvPsWqqSbx0xm-ooX92szbtlSVcjq59N0Tw"); //  debug for marcus.bueschleb
		//mapView = new MapView(this, "0VahPvPsWqqRxfGPKknpotgPXNW29Jxqk-BcgDQ"); //  release for marcus.bueschleb
		
		//mapView = new MapView(this, "05yE31rWh6bQ0CDBqMorFro7SJA-udQb_n1fRgg"); /* ligi */
		 
		
		 //this.setContentView(R.layout.map);
		 this.setContentView(mapView);
		 //mapView=(MapView)this.findViewById(R.id.mapview);
		 /*
		 MapController mc = mapView.getController(); 
		 GeoPoint p =  new GeoPoint(35410000, 139460000);
		 
			 
			 new GeoPoint((int) (37.416402 * 1000000), (int)
	                (-122.025079	 * 1000000));
		 mc.animateTo(p);
		 mc.setZoom(9);
		 mapView.setEnabled(true);
		 */
		Log.i("DUBwise","Content map set");
		
	}

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

}
