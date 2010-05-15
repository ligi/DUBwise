package org.ligi.android.dubwise.map;

import java.util.Vector;

import com.google.android.maps.GeoPoint;

public class FlightPlanProvider {
	
	private static Vector <WayPoint> pnt_fp_vector=null;
	
	public static Vector<WayPoint> getWPList() {
		if (pnt_fp_vector==null)
			pnt_fp_vector=new Vector<WayPoint>();	
		return pnt_fp_vector;
	}
	

	public static void addWP(WayPoint point) {
		
		if (point==null)
			return;
		if ((getWPList().size()>1)&&(point.equals(getWPList().lastElement())))
			return;
		
		getWPList().add(point);	
	}

	public static void addWP(GeoPoint point,int hold_time) {
		addWP(new WayPoint(point,hold_time));	
	}
	
}
