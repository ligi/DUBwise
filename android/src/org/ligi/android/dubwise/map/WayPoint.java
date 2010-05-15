/****************************************************
 *                                            
 * Author:        Marcus -LiGi- Bueschleb     
 * 
 * see README for further Infos
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
 *
 *
 *****************************************************/

package org.ligi.android.dubwise.map;

import com.google.android.maps.GeoPoint;

/**
 * class representing a waypoint 
 * 
 * @author Marcus -ligi- Bueschleb
 *
 */

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
