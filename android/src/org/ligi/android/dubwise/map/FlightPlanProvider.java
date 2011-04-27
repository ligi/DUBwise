/**************************************************************************
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
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ligi.tracedroid.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.maps.GeoPoint;

/**
 * class to handle the FlightPlan ( Waypoint List )
 * 
 * @author ligi
 *
 */
public class FlightPlanProvider {
	
	private static Vector <AndroidWayPoint> pnt_fp_vector=null;
	
	
	public static Vector<AndroidWayPoint> getWPList() {
		if (pnt_fp_vector==null)
			pnt_fp_vector=new Vector<AndroidWayPoint>();	
		return pnt_fp_vector;
	}

	public static void removeLast() {
		if (getWPList().size()>0)
			getWPList().remove(getWPList().size()-1);
	}
	
	public static void addWP(AndroidWayPoint point) {
		
		if (point==null)
			return;
		if ((getWPList().size()>1)&&(point.equals(getWPList().lastElement())))
			return;
		
		getWPList().add(point);	
	}
	
	public static void addWP(GeoPoint point) {
		addWP(new AndroidWayPoint(point));	
	}
	
	private static String formatGPXLatLon(int latlon) {
		String prefix="+";
		if (latlon<0) {
			prefix="-";
			latlon*=-1;
		}
		
		return prefix+latlon/1000000 +"." + latlon%1000000;
	}
	
	public static String toGPX() {
		String res="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n";
		res+="<gpx creator=\"DUBwise\" version=\"1.0\" >\n";
		res+="<metadata>\n";
		res+="\t<link href=\"http://www.ligi.de\">\n";
		res+="\t\t<text>DUBwise</text>\n";
		res+="\t</link>\n";
		res+="</metadata>\n";

		res+="<trk>\n\t<name>FlightPlan</name>\n\t<trkseg>\n";
		
		for (AndroidWayPoint wp:getWPList())
		{
			res+="\t\t<trkpt lat=\"" +formatGPXLatLon( wp.getGeoPoint().getLatitudeE6())+"\" lon=\"" + formatGPXLatLon(wp.getGeoPoint().getLongitudeE6())+"\">\n";
			res+="\t\t\t<HoldTime>" + wp.getHoldTime() + "</HoldTime>\n";
			res+="\t\t\t<ToleranceRadius>" + wp.getToleranceRadius() + "</ToleranceRadius>\n";
			res+="\t\t\t<ChannelEvent>" + wp.getChannelEvent() + "</ChannelEvent>\n";
			res+="\t\t</trkpt>\n";
		}
		res+="\t</trkseg>\n</trk>\n</gpx>";

		return res;
	}

	public static void fromGPX(File gpx) {
		 pnt_fp_vector=null;
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        
		 try {
			 DocumentBuilder builder = factory.newDocumentBuilder();
			 Document dom = builder.parse(gpx);
			 
			 Element root = dom.getDocumentElement();
			 NodeList items = root.getElementsByTagName("trkpt");
			 for (int i=0;i<items.getLength();i++){
				 
				 Node item = items.item(i);
				 String lat_str=item.getAttributes().getNamedItem("lat").getNodeValue();
				 String lon_str=item.getAttributes().getNamedItem("lon").getNodeValue();
				 Log.i("lat: " + lat_str);
				 Log.i("lon: " + lon_str);
				 // TODO better find hold time

				 NodeList properties = item.getChildNodes();
				 int lat = (int)(1000000*Double.parseDouble(lat_str));
				 int lon = (int)(1000000*Double.parseDouble(lon_str));
				
				 AndroidWayPoint wp=new AndroidWayPoint(new GeoPoint(lat,lon)); 
				 
				 for (int j=0;j<properties.getLength();j++){
					 Node property = properties.item(j);
					 String name = property.getNodeName();
					 if (name.equalsIgnoreCase("holdtime"))
						 wp.setHoldTime(Integer.parseInt (property.getFirstChild().getNodeValue()));
					 if (name.equalsIgnoreCase("toleranceradius"))
						 wp.setToleranceRadius(Integer.parseInt (property.getFirstChild().getNodeValue()));
					 if (name.equalsIgnoreCase("channelevent"))
						 wp.setChannelEvent(Integer.parseInt (property.getFirstChild().getNodeValue()));
					 
				 }
				 
				 addWP(wp);
			 }
		 } catch (Exception e) {
			 Log.e("Problem loading gpx " + gpx.toString() );
			 try {
				FileInputStream bin=new FileInputStream(gpx);
			    StringBuffer out = new StringBuffer();
			    byte[] b = new byte[4096];
			    for (int n; (n = bin.read(b)) != -1;) 
			        out.append(new String(b, 0, n));
				 Log.e("gpx content: " + out.toString() );

			 } catch (FileNotFoundException e1) {
				 Log.e("File not found");
			 } catch (IOException ioe) {
				 Log.e("I/O Error");
			}
	
			 throw new RuntimeException(e);
		 } 

	}
}
