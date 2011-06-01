/*********************************************
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
 ********************************************/

package org.ligi.ufo;

/**
 * class to describe a MikroKopter WayPoint
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class WayPoint {

	/** time the MK should hold the position in seconds **/
	private int hold_time=23;        
	
	/** the radius around the waypoint in meters in which the point is seen as reached **/
	private int tolerance_radius=5;  
	
	/** TODO make some doc for this value **/
	private int channel_event=200;    
	
	/** the latitude in deg*10^-7 **/
	private int lat;                 
	
	/** the longitude in deg*10^-7 **/
	private int lon; 
	
	public WayPoint(int lat,int lon) {
		this.setLat(lat);
		this.setLon(lon);
	}
		
	public int getHoldTime() {
		return hold_time;
	}
	
	public void setHoldTime(int t) {
		hold_time=t;
	}

	public void setToleranceRadius(int tolerance_radius) {
		this.tolerance_radius = tolerance_radius;
	}

	public int getToleranceRadius() {
		return tolerance_radius;
	}

	public void setChannelEvent(int channel_event) {
		this.channel_event = channel_event;
	}

	public int getChannelEvent() {
		return channel_event;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLat() {
		return lat;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public int getLon() {
		return lon;
	}
}
