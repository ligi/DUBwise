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
import android.graphics.drawable.Drawable;

import org.ligi.android.dubwise.DUBwisePrefs;
import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.dubwise.R;

import org.ligi.ufo.DUBwiseDefinitions;
import org.ligi.ufo.DUBwiseHelper;
import org.ligi.ufo.VesselData;

public class CockpitView extends View implements DUBwiseDefinitions,OnTouchListener
{
	
	private Drawable ground_drawable;
    private Drawable sky_drawable;
    private Drawable nose_drawable;
    
	private Paint altitudeTextPaint = new Paint();
	
	public CockpitView(Activity context) {
		super(context);

		// needed to get Key Events
		setFocusable(true);

        ground_drawable=getResources().getDrawable(R.drawable.horizon_earth);
        sky_drawable=getResources().getDrawable(R.drawable.horizon_sky);
        nose_drawable=getResources().getDrawable(R.drawable.horizon_nose);

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
		
		float angle_roll=VesselData.attitude.getRoll()/10; // 0.1Deg int to float
		
		if (DUBwisePrefs.isArtificialHorizonInverted())
			angle_roll*=-1;
		
		canvas.rotate(angle_roll,getWidth()/2,getHeight()/2);
        
        // roll rect       
        sky_drawable.setBounds(-getWidth(),-getHeight()*2,2*getWidth(),getHeight()/2);
        sky_drawable.draw(canvas);
    
        ground_drawable.setBounds(-getWidth(),getHeight()/2,2*getWidth(),(int)(getHeight()*1.3));
        ground_drawable.draw(canvas);

        
        // pitch rect                                                                                                                       
        int bar_height=20;
        int nick_bar_move=VesselData.attitude.getNick()*getHeight()/(9*this.getHeight());
        nose_drawable.setBounds(getWidth()/3,getHeight()/2 -bar_height/2 + nick_bar_move ,2*getWidth()/3, getHeight()/2+ nick_bar_move+bar_height);
        nose_drawable.draw(canvas);
    
        canvas.restore();

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
		return false;
	}
}
