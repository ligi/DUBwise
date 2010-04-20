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
import org.ligi.android.dubwise.con.MKProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

class DUBwiseMapOverlay extends com.google.android.maps.Overlay  implements Runnable {

	
	public GeoPoint p=null;
	public boolean flightplan_mode=false;
	public boolean fp_running=false;
	
	private Paint circle_paint;
	private Bitmap home_icon;
	private Bitmap kopter_icon;
	private int act_wp=0;
	
	public DUBwiseMapOverlay(DUBwiseMap context) {
		circle_paint=new Paint();
		//paint.setColor(0x550000FF);
		circle_paint.setColor(0x23FF0000);
		circle_paint.setStyle(Paint.Style.FILL);
	
		kopter_icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon),42,42,true);
		
		home_icon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.rc);
		
		

		pnt_fp_vector=new Vector<GeoPoint>();	
	
		new Thread(this).start();
	}
	
	public Vector <GeoPoint> pnt_fp_vector;
	
	boolean last_was_up;
	
	public boolean onTouchEvent(MotionEvent e,MapView mapView) {
		
		if 	(flightplan_mode)
		{
			if ((e.getAction()==MotionEvent.ACTION_MOVE)||(e.getAction()==MotionEvent.ACTION_DOWN))
				{
				last_was_up=false;
				}
			
			if (e.getAction()==MotionEvent.ACTION_UP)
				last_was_up=true;
	
	
			
			pnt_fp_vector.add(mapView.getProjection().fromPixels  ((int)e.getX(), (int)e.getY()));
			return true;
		}
			
		return false;
	}
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {

		super.draw(canvas, mapView, shadow);
		Paint paint = new Paint();
		// Converts lat/lng-Point to OUR coordinates on the screen.
		float gps_radius_in_pixels=mapView.getProjection().metersToEquatorPixels(50.0f);
		
		if (p!=null) {
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(p, myScreenCoords);
			canvas.drawCircle(myScreenCoords.x, myScreenCoords.y, gps_radius_in_pixels, circle_paint);
			canvas.drawBitmap(home_icon, myScreenCoords.x-home_icon.getWidth()/2, myScreenCoords.y-home_icon.getHeight()/2, paint);
			canvas.drawText("lat" + p.getLatitudeE6() + " lon" + p.getLongitudeE6() , (float)myScreenCoords.x,(float)myScreenCoords.y,paint);
		}
		
		paint.setStrokeWidth(1);
		paint.setARGB(255, 255, 255, 255);
		paint.setStyle(Paint.Style.STROKE);
		
		GeoPoint kopterPoint=new GeoPoint(MKProvider.getMK().gps_position.Latitude/10,MKProvider.getMK().gps_position.Longitude/10);
		Point kopterScreenCoords = new Point();
		mapView.getProjection().toPixels(kopterPoint, kopterScreenCoords);
		
		//canvas.drawCircle(kopterScreenCoords.x, kopterScreenCoords.y, gps_radius_in_pixels, circle_paint);
		RectF act_rectf=new RectF(kopterScreenCoords.x-gps_radius_in_pixels/2,kopterScreenCoords.y-gps_radius_in_pixels/2,kopterScreenCoords.x+gps_radius_in_pixels/2,kopterScreenCoords.y+gps_radius_in_pixels/2);
		canvas.drawArc(act_rectf,MKProvider.getMK().gps_position.CompasHeading-20 -90 , 40, true, circle_paint);
		//canvas.d
		canvas.drawBitmap(kopter_icon, kopterScreenCoords.x-kopter_icon.getWidth()/2, kopterScreenCoords.y-kopter_icon.getHeight()/2, paint);

		Point last_pnt=new Point();
		Point act_pnt=new Point();
		boolean first=true;
		paint.setStrokeWidth(3);
		
		paint.setAntiAlias(true);
		for (GeoPoint pnt:pnt_fp_vector) 
		{
			mapView.getProjection().toPixels(pnt, act_pnt);
					
			if (!first) 
	
				canvas.drawLine(act_pnt.x, act_pnt.y, last_pnt.x , last_pnt.y, paint);
			
			
			first=false;
			last_pnt=new Point(act_pnt);
		}
		
		if ((pnt_fp_vector.size()!=0)&&(act_wp<pnt_fp_vector.size()))
		{
			paint.setAlpha(130);
			
			mapView.getProjection().toPixels(pnt_fp_vector.get(act_wp), act_pnt);
			
			canvas.drawBitmap(kopter_icon, act_pnt.x-kopter_icon.getWidth()/2, act_pnt.y-kopter_icon.getHeight()/2, paint);			
		}
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
		if (act_wp<pnt_fp_vector.size()-1)
			act_wp++;
		else
			act_wp=0;
		}
			
	}
	
	
	}

}