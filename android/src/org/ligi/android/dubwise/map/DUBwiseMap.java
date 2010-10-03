/**************************************************************************
 *                                          
 * Activity to show a Map with the UFO
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

package org.ligi.android.dubwise.map;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.map.dialogs.AddWPDialog;
import org.ligi.android.dubwise.map.dialogs.ZoomToDialog;
import org.ligi.tracedroid.Log;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKGPSPosition;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class DUBwiseMap extends MapActivity implements LocationListener {
	
	private static final int MENU_SETTINGS = 0;
	private static final int MENU_ZOOM = 1;
	
	private static final int MENU_FLIGHTPLAN = 3;
	private static final int MENU_CLEAR_FP = 4;
	
	private static final int MENU_START_FP = 5;
	private static final int MENU_STOP_FP = 6;
	
	private static final int MENU_FP_ADD_WP = 7; 
	
	private static final int MENU_FP_SHOW = 8;
	
	private static final int MENU_FP_UPLOAD = 9; 
	
	private MapView mapView;
	private DUBwiseMapOverlay overlay;
	
	private Handler handler=new Handler();

	private ProgressBar upload_progress;
	private AlertDialog upload_alert;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MapPrefs.init(this);
		MKProvider.getMK().user_intent=MKCommunicator.USER_INTENT_GPSOSD;	
		
		ActivityCalls.beforeContent(this);
		
		this.setContentView(R.layout.map);
		 
		mapView=(MapView)findViewById(R.id.mapview);

		mapView.setSatellite(true);
		 
		LinearLayout zoomView = (LinearLayout) mapView.getZoomControls();

		zoomView.setLayoutParams(new ViewGroup.LayoutParams
		   (ViewGroup.LayoutParams.WRAP_CONTENT,
		    ViewGroup.LayoutParams.WRAP_CONTENT)); 

		zoomView.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		mapView.addView(zoomView);
	
		mapView.getController().setZoom(19);
		 
		GeoPoint kopterPoint=new GeoPoint(MKProvider.getMK().gps_position.Latitude/10,MKProvider.getMK().gps_position.Longitude/10);
		mapView.getController().setCenter(kopterPoint);
	    	
		 
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
			overlay.phonePoint=p;
			mapView.getController().animateTo(p);
			}
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	/* Creates the menu items */
	public boolean onPrepareOptionsMenu(Menu menu) {

		menu.clear();
	
		menu.add(0,MENU_FLIGHTPLAN,0,"Draw Flight Plan" + (overlay.flightplan_mode?" off":" on")).setIcon(android.R.drawable.ic_menu_edit);
		
		if (overlay.flightplan_mode) {
			menu.add(0,MENU_FP_SHOW,0,"Show FlightPlan").setIcon(android.R.drawable.ic_menu_view);

			menu.add(0,MENU_FP_ADD_WP,0,"Add WP").setIcon(android.R.drawable.ic_menu_add);
			
			if (overlay.fp_running)
				menu.add(0,MENU_STOP_FP,0,"Pause").setIcon(android.R.drawable.ic_media_pause);
			else
				menu.add(0,MENU_START_FP,0,"Play").setIcon(android.R.drawable.ic_media_play);

			menu.add(0,MENU_CLEAR_FP,0,"Clear FlightPlan").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
			
			menu.add(0,MENU_FP_UPLOAD,0,"FlightPlan2UFO").setIcon(android.R.drawable.ic_menu_share);
		}
		else {
			menu.add(0,MENU_ZOOM,0,"Zoom to").setIcon(android.R.drawable.ic_menu_zoom);
			menu.add(0,MENU_SETTINGS,0,"Settings").setIcon(android.R.drawable.ic_menu_preferences);
		}
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
	    	FlightPlanProvider.getWPList().clear();
	    	break;
	    	
	    case MENU_FLIGHTPLAN:
	    	overlay.flightplan_mode=!overlay.flightplan_mode;
	    	break;
	    	
	    case MENU_ZOOM:
	    	ZoomToDialog.show(this);
	    	return true;
	
	    case MENU_SETTINGS:
	    	startActivity(new Intent(this, MapPrefsActivity.class));
	        return true;

	    case MENU_FP_SHOW:
	    	startActivity(new Intent(this, ShowFlightPlanActivity.class));
	        return true;
	        
	    case MENU_FP_ADD_WP:
	    	AddWPDialog.show(this);
	    	return true;
	    	
	    case MENU_FP_UPLOAD:
	    	if (FlightPlanProvider.getWPList().size()>20) {
	    		new AlertDialog.Builder(this).setTitle("Error").setMessage("too much waypoints for NC - reduce the number of WP's so that it is not over 20!").show();
	    		return true;
	    	}
	    	upload_progress=new ProgressBar(this,null, android.R.attr.progressBarStyleHorizontal);
	    	upload_progress.setMax(FlightPlanProvider.getWPList().size());
	    	upload_alert=new AlertDialog.Builder(this).setTitle("Uploading").setMessage("Uploading Waypoints to UFO")
	    	.setView(upload_progress).show();
	    
	    	class FPUploader implements Runnable {
	    		class ProgressUpdater implements Runnable {
	    			int progress=0;
	    			public ProgressUpdater(int progress) {
	    				this.progress=progress;
	    			}
	    			
					@Override
					public void run() {
						upload_progress.setProgress(progress);
					}
	    		}
				@Override
				public void run() {
					int i=0;
					MKCommunicator mk=MKProvider.getMK();
					
					// clear the list
					while(mk.gps_position.WayPointNumber!=0) {
						mk.add_gps_wp(MKGPSPosition.STATUS_INVALID, 0,0, 0, 0);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {	}
					
					}
					
					// upload wp's
					for (WayPoint wp:FlightPlanProvider.getWPList()) {
						Log.i("uploading WayPoint " + (i+1));
						handler.post(new ProgressUpdater(i));
						while(mk.gps_position.WayPointNumber!=(i+1)) {
							mk.add_gps_wp(MKGPSPosition.STATUS_NEWDATA, i+1,wp.getGeoPoint().getLongitudeE6(), wp.getGeoPoint().getLatitudeE6(), wp.getHoldTime());
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {	}
						
						}
						i++;
					} // for all WayPoints
					
					// when finished hide the progress dialog
			    	handler.post(new Runnable() {
						@Override
						public void run() {
							upload_alert.hide();
						}
			    	});
				}
	    	}
	    	
	    	new Thread(new FPUploader()).start();

	    	return true;
	
	    }
	    return false;
	}

	public DUBwiseMapOverlay getOverlay() {
		return overlay;
	}

	public MapView getMapView() {
		return mapView;	
	}
}