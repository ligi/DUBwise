/**************************************************************************
 *                                          
 * View for the Cockpit
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

package org.ligi.android.dubwise.cockpit;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.graphics.*;

import org.ligi.android.dubwise.DUBwisePrefs;
import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.ufo.DUBwiseDefinitions;
import org.ligi.ufo.DUBwiseHelper;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.VesselData;

public class CockpitView extends View implements DUBwiseDefinitions,OnTouchListener
{
	private Paint mPaint = new Paint();
	private Paint altitudeTextPaint = new Paint();
		
	
	public CockpitView(Activity context) {
		super(context);

		MKProvider.getMK().user_intent=MKCommunicator.USER_INTENT_3DDATA;
		// needed to get Key Events
		setFocusable(true);

		altitudeTextPaint.setColor(Color.WHITE);
		altitudeTextPaint.setFakeBoldText(true);
		altitudeTextPaint.setShadowLayer(2, 2, 2, Color.BLACK);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		altitudeTextPaint.setTextSize((0.2f*w));	  
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = mPaint;
		paint.setAntiAlias(true);

		paint.setARGB(255, 255, 0, 0);

		float angle_roll=0;
	
		angle_roll=VesselData.attitude.getRoll()/10; // 0.1Deg int to float
		
		if (DUBwisePrefs.isArtificialHorizonInverted())
			angle_roll*=-1;
		
		canvas.rotate(angle_roll,getWidth()/2,getHeight()/2);
        
        paint.setARGB(255,177,129,0);
        // roll rect                                                                                                                       
        //canvas.drawRect(-getWidth(),getHeight()/2,2*getWidth(),3*getHeight()/2,paint);
        canvas.drawRect(-getWidth(),getHeight()/2,2*getWidth(),getHeight()*2,paint);

        int bar_height=20;
        // nick rect                                                                                                                       
        paint.setARGB(200,0,200,0);

        int nick_bar_move=VesselData.attitude.getNick()*getHeight()/(9*this.getHeight());
        
        canvas.drawRoundRect(new RectF(getWidth()/3,getHeight()/2 -bar_height/2 + nick_bar_move ,2*getWidth()/3, getHeight()/2+ nick_bar_move+bar_height),5,5,paint);

        canvas.restore();

        //  paint.setARGB(state_intro_frame,0,0,255);
        //  canvas.drawRoundRect(new RectF(getWidth()/2-getWidth()/8,getHeight()/2-getWidth()/8,getWidth()/2+getWidth()/8,getH\
        //	eight()/2+getWidth()/8),5,5,paint);                                                                                                                

        //canvas.drawRoundRect(new RectF(flight_x,flight_y,flight_x+getWidth()/8,flight_y+getWidth()/8),5,5,paint);
        //paint.setARGB(255,0,0,0);

        // canvas.restore();

        float act_text_pos=this.getHeight();
        
        if (DUBwisePrefs.showAlt()) {
        	canvas.drawText(MKProvider.getMK().getAlt()/10.0 + "m", 7f,act_text_pos, altitudeTextPaint);
        	act_text_pos-=altitudeTextPaint.getTextSize();
       	}
        
        if (DUBwisePrefs.showFlightTime()) {
        	canvas.drawText(DUBwiseHelper.seconds2str(MKProvider.getMK().stats.flying_time()), 7f,act_text_pos, altitudeTextPaint);
        	act_text_pos-=altitudeTextPaint.getTextSize();
       	}
        
        if (DUBwisePrefs.showCurrent()) {
        	canvas.drawText(VesselData.battery.getCurrent()/10.0+"A" , 7f,act_text_pos, altitudeTextPaint);	
        	act_text_pos-=altitudeTextPaint.getTextSize();
       	}
        
        
        if (DUBwisePrefs.showUsedCapacity()) {
        	canvas.drawText(VesselData.battery.getUsedCapacity() +"mAh" , 7f,act_text_pos, altitudeTextPaint);
        	act_text_pos-=altitudeTextPaint.getTextSize();
       	}        
        
        invalidate();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		/*	if(event.getHistorySize()>0) 
		{
			alt_rect.left+=event.getHistoricalX(event.getHistorySize()-1)-event.getX();
			alt_rect.top+=event.getHistoricalY(event.getHistorySize()-1)-event.getY();
		}
		*/
		return false;
	}
}
