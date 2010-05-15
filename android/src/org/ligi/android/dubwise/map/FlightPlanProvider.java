package org.ligi.android.dubwise.map;

import java.util.Vector;

import com.google.android.maps.GeoPoint;

public class FlightPlanProvider {
	
	private static Vector <GeoPoint> pnt_fp_vector=null;
	
	public static Vector<GeoPoint> getWPList() {
		if (pnt_fp_vector==null)
			pnt_fp_vector=new Vector<GeoPoint>();	
		return pnt_fp_vector;
	}
	

	public static void addWP(GeoPoint point) {
		
		if (point==null)
			return;
		if ((getWPList().size()>1)&&(point.equals(getWPList().lastElement())))
			return;
		
		getWPList().add(point);	
	}
	
	
}
