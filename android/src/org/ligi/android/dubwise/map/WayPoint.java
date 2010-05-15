package org.ligi.android.dubwise.map;

import com.google.android.maps.GeoPoint;

public class WayPoint {

		
		private int hold_time; // in s
		private GeoPoint point;
		
		public WayPoint(GeoPoint p,int t) {
			point=p;	
			hold_time=t;
		}
		
		public GeoPoint getGeoPoint() {
			return point;
		}
		
		public void setGeoPoint(GeoPoint p) {
			point=p;
		}
		
		public int getHoldTime() {
			return hold_time;
		}
		
		public void setHoldTime(int t) {
			hold_time=t;
		}
}
