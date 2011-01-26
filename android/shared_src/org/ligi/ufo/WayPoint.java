package org.ligi.ufo;

import com.google.android.maps.GeoPoint;

public class WayPoint {

	
	private int hold_time=23;        // in s
	private int tolerance_radius=5;  // in m
	private int channel_event=200;   // 
	private int lat;                 // in deg*10^-7
	private int lon;                 // in deg*10^-7
	
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
