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

import java.util.Vector;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.helper.canvas.CanvasButton;
import org.ligi.android.dubwise.helper.canvas.CanvasButtonAction;
import org.ligi.android.dubwise.helper.canvas.TogglingCanvasButton;
import org.ligi.android.dubwise.map.dialogs.AddWPDialog;
import org.ligi.android.dubwise.map.dialogs.LoadSaveDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

/**
 *                                        
 * Overlay for the DUBwise Map
 *       
 * @author ligi
 *
 */

public class DUBwiseMapOverlay extends com.google.android.maps.Overlay  implements Runnable {

	
	public GeoPoint phonePoint=null;
	private GeoPoint kopterPoint=null;
	private GeoPoint homePoint=null;
	
 	public boolean flightplan_mode=false;
	public boolean fp_running=false;
	
	private Paint compas_heading_paint;
	private Paint radius_paint;
	private Paint button_bg_paint;
	private Paint image_paint;
	
	private Bitmap home_icon;
	private Bitmap kopter_icon;
	private Bitmap phone_icon;
	
	private boolean show_fp_menu=true;
	private boolean draw_flightplan_via_touch=true;
	
	private float width=0.0f;
	private float height=0.0f;
	
	
	private int act_wp=0;
	
	private TogglingCanvasButton edit_button;
	private CanvasButton undo_button;
	private CanvasButton clear_button;
	private CanvasButton addwp_button;
	private CanvasButton loadsave_button;
	private CanvasButton view_button;
	private CanvasButton upload_button;
	
	private Vector<CanvasButton> fp_edit_buttons;
	
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
	
	private RectF edit_icon_rect;;
	
	public DUBwiseMapOverlay(DUBwiseMap context) {
		
		button_bg_paint=new Paint();
		image_paint=new Paint();
		
		button_bg_paint.setColor(0x23000000);
		
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
	
		
		fp_edit_buttons=new Vector<CanvasButton>();
		
		fp_edit_buttons.add(edit_button=new TogglingCanvasButton(context,android.R.drawable.ic_menu_edit,"toggle edit"));
		
		
		fp_edit_buttons.add(undo_button=new CanvasButton(context,android.R.drawable.ic_menu_revert,"delete last"));
		
		class UndoAction implements CanvasButtonAction {

			@Override
			public void action() {
				FlightPlanProvider.removeLast();
			}
			
		}
		
		undo_button.setAction(new UndoAction());
		
		fp_edit_buttons.add(clear_button=new CanvasButton(context,android.R.drawable.ic_menu_close_clear_cancel,"clear"));
		
		class ClearAction implements CanvasButtonAction {

			@Override
			public void action() {
				FlightPlanProvider.getWPList().clear();
			}
			
		}
		
		clear_button.setAction(new ClearAction());

		fp_edit_buttons.add(upload_button=new CanvasButton(context,android.R.drawable.ic_menu_share,"upload"));
		
		class UploadAction implements CanvasButtonAction {

			private DUBwiseMap map;
			public UploadAction(DUBwiseMap map) {
				this.map=map;
			}
			@Override
			public void action() {
				map.upload();
			}
			
		}
		
		upload_button.setAction(new UploadAction(context));

		
		fp_edit_buttons.add(loadsave_button=new CanvasButton(context,android.R.drawable.ic_menu_save,"load/save"));

		class LoadSaveAction implements CanvasButtonAction {


			private DUBwiseMap map;
			public LoadSaveAction(DUBwiseMap map) {
				this.map=map;
			}

			@Override
			public void action() {
				LoadSaveDialog.show(map);
			}
			
		}
		
		loadsave_button.setAction(new LoadSaveAction(context));

		fp_edit_buttons.add(view_button=new CanvasButton(context,android.R.drawable.ic_menu_view,"view"));

		class ViewAction implements CanvasButtonAction {


			private DUBwiseMap map;
			
			public ViewAction(DUBwiseMap map) {
				this.map=map;
			}

			@Override
			public void action() {
				map.startActivity(new Intent(map, ShowFlightPlanActivity.class));
			}
			
		}
		
		view_button.setAction(new ViewAction(context));

		
		fp_edit_buttons.add(addwp_button=new CanvasButton(context,android.R.drawable.ic_menu_add,"Add"));
		
		class AddWPAction implements CanvasButtonAction {

			private DUBwiseMap map;
			public AddWPAction(DUBwiseMap map) {
				this.map=map;
			}
			@Override
			public void action() {
				AddWPDialog.show(map);
			}
			
		}
		
		addwp_button.setAction(new AddWPAction(context));


		
		
		positionIconRects();
		
		new Thread(this).start();
	
	}
	RectF allButtons=new RectF();
	/**
	 * depends on width and height of canvas and icons
	 */
	public void positionIconRects() {
		edit_button.layout(width-edit_button.getIconSize(), 0);
		undo_button.layout(width-undo_button.getIconSize(), undo_button.getIconSize());
		clear_button.layout(width-undo_button.getIconSize(), 2*undo_button.getIconSize());
		addwp_button.layout(width-undo_button.getIconSize(), 3*undo_button.getIconSize());
		loadsave_button.layout(width-undo_button.getIconSize(), 4*undo_button.getIconSize());
		view_button.layout(width-undo_button.getIconSize(), 5*undo_button.getIconSize());
		upload_button.layout(width-undo_button.getIconSize(), 6*undo_button.getIconSize());
		
		allButtons.set(loadsave_button.getRect());
		
		for (CanvasButton btn : fp_edit_buttons)
			allButtons.union(btn.getRect());
	}
	
	public boolean onTouchEvent(MotionEvent e,MapView mapView) {
		
		boolean done=false;
		for (CanvasButton btn : fp_edit_buttons) 
			done|= (btn.isTouched(e));
				
		if (done) 
			return true;
			
		/*
		if (flightplan_mode&&(edit_icon_rect.contains(e.getX(),e.getY()))) {
			if (e.getAction()==MotionEvent.ACTION_UP)  
				draw_flightplan_via_touch=!draw_flightplan_via_touch;
			return true;
		}
			*/
		
		if 	(edit_button.toggled&&flightplan_mode&&(e.getAction()!=MotionEvent.ACTION_UP)) {
			FlightPlanProvider.addWP(mapView.getProjection().fromPixels  ((int)e.getX(), (int)e.getY())  );
			return true;
		}
		
		
		return false;
	}
	
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {

		super.draw(canvas, mapView, shadow);
		
		width=canvas.getWidth();
		height=canvas.getHeight();

		positionIconRects();
				
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
			for (AndroidWayPoint pnt:FlightPlanProvider.getWPList()) {
				
				mapView.getProjection().toPixels(pnt.getGeoPoint(), act_pnt);
						
				if (!first) {
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
	
			if (act_wp<FlightPlanProvider.getWPList().size()) {
				paint.setAlpha(130);
				mapView.getProjection().toPixels(FlightPlanProvider.getWPList().get(act_wp).getGeoPoint(), act_pnt);
				canvas.drawBitmap(kopter_icon, act_pnt.x-kopter_icon.getWidth()/2, act_pnt.y-kopter_icon.getHeight()/2, image_paint);			
			}
		}
		
		// Converts lat/lng-Point to OUR coordinates on the screen.
		float gps_radius_in_pixels=mapView.getProjection().metersToEquatorPixels(500.0f);
		
		if ((phonePoint!=null)&&MapPrefs.showPhone()) {
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(phonePoint, myScreenCoords);
			//canvas.drawCircle(myScreenCoords.x, myScreenCoords.y, gps_radius_in_pixels, compas_heading_paint);
			canvas.drawBitmap(phone_icon, myScreenCoords.x-phone_icon.getWidth()/2, myScreenCoords.y-phone_icon.getHeight()/2, image_paint);
			//canvas.drawText("lat" + phonePoint.getLatitudeE6() + " lon" + phonePoint.getLongitudeE6() , (float)myScreenCoords.x,(float)myScreenCoords.y,paint);
		}
		
		
		kopterPoint=new GeoPoint(MKProvider.getMK().gps_position.Latitude/10,MKProvider.getMK().gps_position.Longitude/10);
		Point kopterScreenCoords = new Point();
		mapView.getProjection().toPixels(kopterPoint, kopterScreenCoords);
		
		if (MapPrefs.showUFO())
			canvas.drawBitmap(kopter_icon, kopterScreenCoords.x-kopter_icon.getWidth()/2, kopterScreenCoords.y-kopter_icon.getHeight()/2, image_paint);
		
		
		homePoint=new GeoPoint(MKProvider.getMK().gps_position.HomeLatitude /10,MKProvider.getMK().gps_position.HomeLongitude/10);
		Point homeScreenCoords = new Point();
		mapView.getProjection().toPixels(homePoint, homeScreenCoords);
		
		if (MapPrefs.showHome())
			canvas.drawBitmap(home_icon, homeScreenCoords.x-home_icon.getWidth()/2, homeScreenCoords.y-home_icon.getHeight()/2, image_paint);
		
		
		if (MapPrefs.showUFORadius())		
			canvas.drawCircle(kopterScreenCoords.x, kopterScreenCoords.y, gps_radius_in_pixels, radius_paint);
		
		if (MapPrefs.showHomeRadius())
			canvas.drawCircle(homeScreenCoords.x, homeScreenCoords.y, gps_radius_in_pixels, radius_paint);
		
		
		RectF act_rectf=new RectF(kopterScreenCoords.x-kopter_icon.getHeight(),kopterScreenCoords.y-kopter_icon.getHeight(),kopterScreenCoords.x+kopter_icon.getHeight(),kopterScreenCoords.y+kopter_icon.getHeight());
		canvas.drawArc(act_rectf,MKProvider.getMK().gps_position.CompasHeading-20 -90 , 40, true, compas_heading_paint);
		
		
		if (flightplan_mode) {
			canvas.drawRoundRect(allButtons, 7.0f, 7.0f, button_bg_paint);
			for (CanvasButton btn : fp_edit_buttons)
				btn.draw(canvas);

/*			if (draw_flightplan_via_touch) {
				canvas.drawCircle(canvas.getWidth()-edit_icon.getWidth()/2.0f,edit_icon.getHeight()/2.0f,edit_icon.getHeight()/2.0f , highlight_paint);
			}
		
			canvas.drawBitmap(edit_icon, edit_icon_rect.left, edit_icon_rect.top, wp_text_paint);
			canvas.drawBitmap(undo_icon, undo_icon, edit_icon.getHeight(), wp_text_paint);
	*/
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
		if (act_wp<FlightPlanProvider.getWPList().size()-1)
			act_wp++;
		else
			act_wp=0;
		}
			
	}
	
	
	}

}