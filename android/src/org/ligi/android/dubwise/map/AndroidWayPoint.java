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

import org.ligi.ufo.WayPoint;

import com.google.android.maps.GeoPoint;

/**
 * class representing a waypoint 
 * 
 * @author Marcus -ligi- Bueschleb
 *
 */

public class AndroidWayPoint extends WayPoint {

		private GeoPoint point;
		
		public AndroidWayPoint(GeoPoint p) {
			super(p.getLatitudeE6()*10, p.getLongitudeE6()*10);
			point=p;	
		}
		
		public GeoPoint getGeoPoint() {
			return point;
		}
	
}
