package org.ligi.android.dubwise;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.helper.RefreshingStringListActivity;
import org.ligi.ufo.MKCommunicator;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class FollowMeActivity extends RefreshingStringListActivity  implements LocationListener {

	private LocationManager lm=null;
	private double phone_lat=0.0,phone_lng=0.0;
	
	@Override
	public String getStringByPosition(int pos) {
		MKCommunicator mk = MKProvider.getMK();
		
		if (lm==null) {
			lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 5.0f, this);
			mk.user_intent=MKCommunicator.USER_INTENT_FOLLOWME;
		}
		
		switch (pos) {
			case 0:
				mk.follow_me_lat=(int)(phone_lat*10000000);
				mk.follow_me_lon=(int)(phone_lng*10000000);
				if (phone_lat!=0.0)
					return "Mobile: lat:" + phone_lat + " lon:" + phone_lng;
				else
					return "No GPS Data yet";
			case 1:
				//return "UFO lat:" + mk.gps_position.Latitude_str() + " lon:" +mk.gps_position.Longitude_str();   
				return "UFO lat:" + mk.gps_position.Latitude + " lon:" +mk.gps_position.Longitude;
			case 2:
				return "Target lat" + mk.gps_position.TargetLatitude + " lon" + mk.gps_position.TargetLongitude ;
			case 3:
				return "updates:" + mk.stats.follow_me_request_count;
			case 4:
				//return "dist: " + mk.gps_position.distance(mk.gps_position.Latitude, mk.gps_position.Longitude, (int)(phone_lat*10000000), (int)(phone_lng*10000000));
				return "dist: " + mk.gps_position.Distance2Target;
		}
		return null;
	}	
		
	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			phone_lat = location.getLatitude();
			phone_lng = location.getLongitude();
			GeoPoint p = new GeoPoint((int) (phone_lat * 1000000), (int)( phone_lng * 1000000));
		}
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
