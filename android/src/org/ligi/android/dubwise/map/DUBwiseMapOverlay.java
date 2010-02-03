package org.ligi.android.dubwise.map;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.R.drawable;
import org.ligi.android.dubwise.con.MKProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

class DUBwiseMapOverlay extends com.google.android.maps.Overlay {

	DUBwiseMap context;
	GeoPoint p=null;
	Paint circle_paint;
	Bitmap home_icon;
	Bitmap kopter_icon;
	
	public DUBwiseMapOverlay(DUBwiseMap context) {
		//	p=new GeoPoint(0,0);
		this.context=context;
		circle_paint=new Paint();
		//paint.setColor(0x550000FF);
		circle_paint.setColor(0x230000FF);
		circle_paint.setStyle(Paint.Style.FILL);
	
		kopter_icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon),42,42,true);
		
		home_icon = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.rc);
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {

		super.draw(canvas, mapView, shadow);
		Paint paint = new Paint();
		// Converts lat/lng-Point to OUR coordinates on the screen.
		float gps_radius_in_pixels=mapView.getProjection().metersToEquatorPixels(500.0f);
		
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
		
		canvas.drawCircle(kopterScreenCoords.x, kopterScreenCoords.y, gps_radius_in_pixels, circle_paint);
		canvas.drawBitmap(kopter_icon, kopterScreenCoords.x-kopter_icon.getWidth()/2, kopterScreenCoords.y-kopter_icon.getHeight()/2, paint);

		return true;
	}

}