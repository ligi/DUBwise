package org.ligi.android.dubwise.map;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.R.id;
import org.ligi.android.dubwise.R.layout;
import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.android.dubwise.graph.GraphSettingsActivity;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.ufo.MKCommunicator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class DUBwiseMap extends MapActivity implements LocationListener {
	
	private static final int MENU_SETTINGS = 0;
	private static final int MENU_ZOOM_KOPTER = 1;
	private static final int MENU_ZOOM_HOME = 2;
	private static final int MENU_FLIGHTPLAN = 3;
	private static final int MENU_CLEAR_FP = 4;
	
	private static final int MENU_START_FP = 5;
	private static final int MENU_STOP_FP = 6;
	

	LinearLayout linearLayout;
	MapView mapView;
	ZoomControls mZoom;
	
	DUBwiseMapOverlay overlay;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MKProvider.getMK().user_intent=MKCommunicator.USER_INTENT_GPSOSD;	
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
		 lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 5.0f, this);
		 
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
			GeoPoint p = new GeoPoint((int) (lat * 1000000), (int)( lng * 1000000));
			
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

	/* Creates the menu items */
	public boolean onPrepareOptionsMenu(Menu menu) {

		menu.clear();
		
		Bitmap kopter_icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.icon),32,32,true);
		
		MenuItem settings_menu=menu.add(0,MENU_SETTINGS,0,"Settings");
		settings_menu.setIcon(android.R.drawable.ic_menu_preferences);
	
		MenuItem settings_flightplan=menu.add(0,MENU_FLIGHTPLAN,0,"Draw Flight Plan" + (overlay.flightplan_mode?" off":" on"));
		settings_flightplan.setIcon(android.R.drawable.ic_menu_edit);

		
		MenuItem freeze_menu=menu.add(0,MENU_ZOOM_KOPTER,0,"Zoom to UFO");
		freeze_menu.setIcon(new BitmapDrawable(kopter_icon));

		if (overlay.flightplan_mode)
		{
			if (overlay.fp_running)
			{
				MenuItem clr_fp_menu=menu.add(0,MENU_STOP_FP,0,"Pause");
				clr_fp_menu.setIcon(android.R.drawable.ic_media_pause);
			}
			else
			{
				MenuItem clr_fp_menu=menu.add(0,MENU_START_FP,0,"Play");
				clr_fp_menu.setIcon(android.R.drawable.ic_media_play);
			}
			MenuItem clr_fp_menu=menu.add(0,MENU_CLEAR_FP,0,"Clear FlightPlan");
			clr_fp_menu.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
			
		}
		
		if (overlay.p!=null) {
			MenuItem freeze_menu2=menu.add(0,MENU_ZOOM_HOME,0,"Zoom to Home");
			freeze_menu2.setIcon(R.drawable.rc);
		}
		
		/*
	    Menu features_menu=menu.addSubMenu("Features");
	    features_menu.add(0, MENU_LEGEND, 0, "New Game").setCheckable(true);
	    features_menu.add(0, MENU_GRID, 0, "Quit").setCheckable(true);
	    */
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {

		
	    switch (item.getItemId()) {
	    case MENU_START_FP:
	    case MENU_STOP_FP:
	    	overlay.fp_running=!overlay.fp_running;
	    	break;
	    	
	    case MENU_CLEAR_FP:
	    	overlay.pnt_fp_vector.clear();
	    	break;
	    	
	    case MENU_FLIGHTPLAN:
	    	overlay.flightplan_mode=!overlay.flightplan_mode;
	    	break;
	    	
	    case MENU_ZOOM_KOPTER:
	    	GeoPoint kopterPoint=new GeoPoint(MKProvider.getMK().gps_position.Latitude/10,MKProvider.getMK().gps_position.Longitude/10);
	    	mapView.getController().setCenter(kopterPoint);
	    	mapView.getController().setZoom(14);
	    	return true;
	    	
	    case MENU_ZOOM_HOME:
	    	mapView.getController().setCenter(overlay.p);
	    	mapView.getController().setZoom(14);
	    	return true;
	
	    case MENU_SETTINGS:
	    	startActivity(new Intent(this, MapSettingsActivity.class));
	        return true;
	
	    }
	    
	    return false;
	}


}
