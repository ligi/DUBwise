/**************************************************************************
 *                                          
 * Overlay for the DUBwise Map
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
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise.map;

import java.util.Vector;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.conn.MKProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class DUBwiseMapOverlay extends com.google.android.maps.Overlay  implements Runnable {

	
	public GeoPoint phonePoint=null;
	private GeoPoint kopterPoint=null;
	private GeoPoint homePoint=null;
	
	
	public boolean flightplan_mode=false;
	public boolean fp_running=false;
	
	private Paint compas_heading_paint;
	private Paint radius_paint;
	
	private Bitmap home_icon;
	private Bitmap kopter_icon;
	private Bitmap phone_icon;
	
	private int act_wp=0;
	
	
	public Bitmap getKopterIcon() {
		return kopter_icon;
	}
	
	public Bitmap getHomeIcon() {
		return home_icon;
	}
	
	public Bitmap getPhoneIcon() {
		return phone_icon;
	}
	
	public GeoPoint getPhonePos() {
		return phonePoint;
	}
	
	public GeoPoint getUFOPos() {
		return kopterPoint;
	}
	
	public GeoPoint getHomePos() {
		return homePoint;
	}
	public void phonepos2wp() {
		FlightPlanProvider.addWP(phonePoint);
	}
	
	public void homepos2wp() {
		FlightPlanProvider.addWP(homePoint);
	}
	
	public void ufopos2wp() {
		FlightPlanProvider.addWP(kopterPoint);
	}
	
	public boolean hasPhonePos() {
		return (phonePoint!=null);
	}
	public DUBwiseMapOverlay(DUBwiseMap context) {
		compas_heading_paint=new Paint();
		
		compas_heading_paint.setColor(0x23FF0000);
		compas_heading_paint.setStyle(Paint.Style.FILL);
	
		radius_paint=new Paint();
	
		radius_paint.setColor(0xBB0000FF);
		radius_paint.setStyle(Paint.Style.STROKE);
		radius_paint.setStrokeWidth(3);
		
		kopter_icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon),42,42,true);
		
		home_icon =( BitmapFactory.decodeResource(context.getResources(),
				R.drawable.rc));
		
		
		phone_icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
				android.R.drawable.ic_menu_call),42,42,true);
		
		

		
	
		new Thread(this).start();
	}
	
	
	
	boolean last_was_up;
	
	public boolean onTouchEvent(MotionEvent e,MapView mapView) {
		
		if 	(flightplan_mode)
		{
			if ((e.getAction()==MotionEvent.ACTION_MOVE)||(e.getAction()==MotionEvent.ACTION_DOWN))
				last_was_up=false;
			
			if (e.getAction()==MotionEvent.ACTION_UP)
				last_was_up=true;
	
	
			
			FlightPlanProvider.addWP(mapView.getProjection().fromPixels  ((int)e.getX(), (int)e.getY()));
			return true;
		}
			
		return false;
	}
	
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {

		super.draw(canvas, mapView, shadow);
		Paint paint = new Paint();
		Paint wp_circle_paint = new Paint();
		wp_circle_paint.setColor(0xFFFFFFFF);

		Paint wp_text_paint = new Paint();
		wp_text_paint.setColor(0xFF000000);
		wp_text_paint.setTextAlign(Paint.Align.CENTER );
		
		// 	draw the waypoint lines
		if (FlightPlanProvider.getWPList().size()>0) {
			Point last_pnt=new Point();
			Point act_pnt=new Point();
			boolean first=true;
			paint.setStrokeWidth(3);
			paint.setARGB(255, 255, 255, 255);
			paint.setStyle(Paint.Style.STROKE);
			
			paint.setShadowLayer(2, 1, 1, 0xCC000000);
			paint.setAntiAlias(true);
			wp_circle_paint.setAntiAlias(true);
			
			int wp_id=0;
			FontMetrics fm=wp_text_paint.getFontMetrics();
			for (GeoPoint pnt:FlightPlanProvider.getWPList()) 
			{
				
				mapView.getProjection().toPixels(pnt, act_pnt);
						
				if (!first)
					{
					canvas.drawLine(act_pnt.x, act_pnt.y, last_pnt.x , last_pnt.y, paint);
					canvas.drawCircle(last_pnt.x, last_pnt.y, 10, wp_circle_paint);
					canvas.drawText(""+wp_id,last_pnt.x, last_pnt.y-5-(fm.top+fm.bottom), wp_text_paint);
					}
				
				wp_id++;
				first=false;
				last_pnt=new Point(act_pnt);
			
			}
			canvas.drawCircle(last_pnt.x, last_pnt.y, 10, wp_circle_paint);
			canvas.drawText(""+wp_id,last_pnt.x, last_pnt.y-5-(fm.top+fm.bottom), wp_text_paint);
	
			if (act_wp<FlightPlanProvider.getWPList().size())
			{
				paint.setAlpha(130);
				
				mapView.getProjection().toPixels(FlightPlanProvider.getWPList().get(act_wp), act_pnt);
				
				canvas.drawBitmap(kopter_icon, act_pnt.x-kopter_icon.getWidth()/2, act_pnt.y-kopter_icon.getHeight()/2, paint);			
			}
			
		}
		
		
		// Converts lat/lng-Point to OUR coordinates on the screen.
		float gps_radius_in_pixels=mapView.getProjection().metersToEquatorPixels(500.0f);
		
		
		if ((phonePoint!=null)&&MapPrefs.showPhone()) {
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(phonePoint, myScreenCoords);
			canvas.drawCircle(myScreenCoords.x, myScreenCoords.y, gps_radius_in_pixels, compas_heading_paint);
			canvas.drawBitmap(phone_icon, myScreenCoords.x-phone_icon.getWidth()/2, myScreenCoords.y-phone_icon.getHeight()/2, paint);
			canvas.drawText("lat" + phonePoint.getLatitudeE6() + " lon" + phonePoint.getLongitudeE6() , (float)myScreenCoords.x,(float)myScreenCoords.y,paint);
		}
		
		
		kopterPoint=new GeoPoint(MKProvider.getMK().gps_position.Latitude/10,MKProvider.getMK().gps_position.Longitude/10);
		Point kopterScreenCoords = new Point();
		mapView.getProjection().toPixels(kopterPoint, kopterScreenCoords);
		
		if (MapPrefs.showUFO())
			canvas.drawBitmap(kopter_icon, kopterScreenCoords.x-kopter_icon.getWidth()/2, kopterScreenCoords.y-kopter_icon.getHeight()/2, paint);
		
		
		homePoint=new GeoPoint(MKProvider.getMK().gps_position.HomeLatitude /10,MKProvider.getMK().gps_position.HomeLongitude/10);
		Point homeScreenCoords = new Point();
		mapView.getProjection().toPixels(homePoint, homeScreenCoords);
		
		if (MapPrefs.showHome())
			canvas.drawBitmap(home_icon, homeScreenCoords.x-home_icon.getWidth()/2, homeScreenCoords.y-home_icon.getHeight()/2, paint);
		
		
		if (MapPrefs.showUFORadius())		
			canvas.drawCircle(kopterScreenCoords.x, kopterScreenCoords.y, gps_radius_in_pixels, radius_paint);
		
		if (MapPrefs.showHomeRadius())
			canvas.drawCircle(homeScreenCoords.x, homeScreenCoords.y, gps_radius_in_pixels, radius_paint);
		
		
		RectF act_rectf=new RectF(kopterScreenCoords.x-kopter_icon.getHeight(),kopterScreenCoords.y-kopter_icon.getHeight(),kopterScreenCoords.x+kopter_icon.getHeight(),kopterScreenCoords.y+kopter_icon.getHeight());
		canvas.drawArc(act_rectf,MKProvider.getMK().gps_position.CompasHeading-20 -90 , 40, true, compas_heading_paint);
		
		
		
		return true;
	}
	@Override
	public void run() {
	while (true)
	{
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
		if (fp_running) {
		if (act_wp<FlightPlanProvider.getWPList().size()-1)
			act_wp++;
		else
			act_wp=0;
		}
			
	}
	
	
	}

}