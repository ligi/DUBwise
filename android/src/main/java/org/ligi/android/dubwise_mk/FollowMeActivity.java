package org.ligi.android.dubwise_mk;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import org.ligi.android.dubwise_mk.app.App;
import org.ligi.axt.base_activities.RefreshingStringBaseListActivity;
import org.ligi.ufo.MKCommunicator;

/**
 * Activity to send Phone GPS Coordinates to the UFO so that it follows the Phone
 * Displays the status of this action as a List of relative values
 *
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 */
public class FollowMeActivity extends RefreshingStringBaseListActivity implements LocationListener {

    private LocationManager lm = null;
    private double phone_lat = 0.0, phone_lng = 0.0;


    @Override
    protected void onStart() {
        super.onStart();

        if (lm == null) {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 5.0f, this);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (lm != null) { // should not be null here, but better save than sorry ^^
            lm.removeUpdates(this);
        }
    }


    @Override
    public String getStringByPosition(int pos) {
        MKCommunicator mk = App.getMK();


        switch (pos) {
            case 0:
                if (phone_lat != 0.0)
                    return "Mobile: lat:" + phone_lat + " lon:" + phone_lng;
                else
                    return "No GPS Data yet";
            case 1:
                //return "UFO lat:" + mk.gps_position.Latitude_str() + " lon:" +mk.gps_position.Longitude_str();
                return "UFO lat:" + mk.gps_position.Latitude + " lon:" + mk.gps_position.Longitude;
            case 2:
                return "Target lat" + mk.gps_position.TargetLatitude + " lon" + mk.gps_position.TargetLongitude;
            case 3:
                return "updates:" + mk.stats.follow_me_request_count;
            case 4:
                //return "dist: " + mk.gps_position.distance(mk.gps_position.Latitude, mk.gps_position.Longitude, (int)(phone_lat*10000000), (int)(phone_lng*10000000));
                return "dist: " + mk.gps_position.Distance2Target;

            case 5:
                return null;
            //return "sane"+ MKHelperTests.isConvertionSane();

        }
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            phone_lat = location.getLatitude();
            phone_lng = location.getLongitude();

            App.getMK().follow_me_lat = (int) (phone_lat * 10000000);
            App.getMK().follow_me_lon = (int) (phone_lng * 10000000);
            App.getMK().user_intent = MKCommunicator.USER_INTENT_FOLLOWME;
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
