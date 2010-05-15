package org.ligi.android.dubwise.map;

import java.io.File;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ligi.tracedroid.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



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
		
		for (WayPoint wp:getWPList())
		{
			res+="\t\t<trkpt lat=\"" +formatGPXLatLon( wp.getGeoPoint().getLatitudeE6())+"\" lon=\"" + formatGPXLatLon(wp.getGeoPoint().getLongitudeE6())+"\">\n";
			res+="\t\t\t<HoldTime>" + wp.getHoldTime() + "</HoldTime>\n";
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
		                String lat_str=item.getAttributes().getNamedItem("lat").getNodeValue().substring(1);
		                String lon_str=item.getAttributes().getNamedItem("lon").getNodeValue().substring(1);
		                Log.i("lat: " + lat_str);
		                Log.i("lon: " + lon_str);
		                // TODO better find hold time
		                int hold_time=Integer.parseInt(item.getChildNodes().item(0).getNodeValue());
		                int lat = (int)(1000000*Double.parseDouble(lat_str));
		                int lon = (int)(1000000*Double.parseDouble(lon_str));
		                
		                addWP(new WayPoint(new GeoPoint(lat,lon),hold_time));
		               }
		        } catch (Exception e) {
		            throw new RuntimeException(e);
		        } 
		        
		    

	}
}
