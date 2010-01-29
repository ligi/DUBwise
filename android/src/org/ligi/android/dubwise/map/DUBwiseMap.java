package org.ligi.android.dubwise.map;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.R.id;
import org.ligi.android.dubwise.R.layout;
import org.ligi.android.dubwise.helper.ActivityCalls;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class DUBwiseMap extends MapActivity implements LocationListener {
	
	LinearLayout linearLayout;
	MapView mapView;
	ZoomControls mZoom;
	
	DUBwiseMapOverlay overlay;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//mapView = new MapView(this, "05yE31rWh6bR5W3A9cOq2www1gYQr2XDq5ACbSg"); /* debug */
		
		
		//	mapView = new MapView(this, "0VahPvPsWqqSbx0xm-ooX92szbtlSVcjq59N0Tw"); //  debug for marcus.bueschleb
		//mapView = new MapView(this, "0VahPvPsWqqRxfGPKknpotgPXNW29Jxqk-BcgDQ"); //  release for marcus.bueschleb
		
		//mapView = new MapView(this, "05yE31rWh6bQ0CDBqMorFro7SJA-udQb_n1fRgg"); /* ligi */
		
		ActivityCalls.beforeContent(this);
		
		 this.setContentView(R.layout.map);
		
		 
		 mapView=(MapView)findViewById(R.id.mapview);

		 mapView.setSatellite(true);
		 
		 //mapView.setZ .setZoom(23);
		 
		 LinearLayout zoomView = (LinearLayout) mapView.getZoomControls();

		 zoomView.setLayoutParams(new ViewGroup.LayoutParams
		   (ViewGroup.LayoutParams.WRAP_CONTENT,
		    ViewGroup.LayoutParams.WRAP_CONTENT)); 

		 zoomView.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		 mapView.addView(zoomView);
	
		 mapView.getController().setZoom(3);
		 
		 //mapView.getZoomButtonsController().setVisible(true);
		 
		 //LinearLayout lin=new LinearLayout(this);
		 //lin.addView(mapView);
		
		 
		 //.setVisible(true);
		 //lin.addView(mZoom);
		// this.setContentView(mapView);
		 	
		 
		 overlay=new DUBwiseMapOverlay(this);
		 mapView.getOverlays().add(overlay);
		 
		 LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		 lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);
		 
		 
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			GeoPoint p = new GeoPoint((int) lat * 1000000, (int) lng * 1000000);
			
			overlay.p=p;
			
			mapView.getController().animateTo(p);
			}
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
