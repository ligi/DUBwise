/**************************************************************************
 *                                          
 * View for Piloting the UFO via Multitouch
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

package org.ligi.android.dubwise.piloting;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.MKCommunicator;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.graphics.*;

public class MultiTouchPilotingView extends View implements OnTouchListener

{
	
	int act_nick=0;
	int act_roll=0;
	int act_gier=0;
	
	int act_gas=-127;
	
	int circle_size=30;
	

	public float gierAsFloat() {
		return act_gier/127.0f;
	}

	public float gasAsFloat() {
		return act_gas/127.0f*-1.0f;
	}
	
	public float nickAsFloat() {
		return act_nick/127.0f;
	}
	

	public float rollAsFloat() {
		return act_roll/127.0f;
	}
	
	
	int rect_size=0;
	
	private Paint mPaint = new Paint();
	public MultiTouchPilotingView(Activity context) {
		super(context);
		this.setOnTouchListener(this );
		MKProvider.getMK().user_intent=MKCommunicator.USER_INTENT_EXTERNAL_CONTROL;
		  
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		rect_size=h-42;
	}

	public void uptate2mk() {
		MKProvider.getMK().extern_control[MKCommunicator.EXTERN_CONTROL_NICK]=(int)act_nick*-1;
	    MKProvider.getMK().extern_control[MKCommunicator.EXTERN_CONTROL_ROLL]=(int)act_roll*-1;
	    MKProvider.getMK().extern_control[MKCommunicator.EXTERN_CONTROL_GAS]=act_gas+127;
	    MKProvider.getMK().extern_control[MKCommunicator.EXTERN_CONTROL_GIER]=(int)act_gier;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		mPaint.setColor(0xCCCCCCCC);
	    
	    canvas.drawRect(new Rect(0,this.getHeight(),rect_size,this.getHeight()-rect_size), mPaint);
	    canvas.drawRect(new Rect(this.getWidth(),this.getHeight(),this.getWidth()-rect_size,this.getHeight()-rect_size), mPaint);
	  
	    mPaint.setColor(0xFF000000);
	    canvas.drawLine(0,this.getHeight(),rect_size,this.getHeight()-rect_size, mPaint);
	    canvas.drawLine(0,this.getHeight()-rect_size,rect_size,this.getHeight(), mPaint);
	    
	    
	    canvas.drawLine(this.getWidth(),this.getHeight(),this.getWidth()-rect_size,this.getHeight()-rect_size, mPaint);
	    canvas.drawLine(this.getWidth(),this.getHeight()-rect_size,this.getWidth()-rect_size,this.getHeight(), mPaint);

	    
	    mPaint.setColor(0xCCCCCCBB);
	    
	    // circles for calced position
	    mPaint.setColor(0xCCCCCCBB);
	    
	    canvas.drawCircle(rect_size/2  + gierAsFloat()*rect_size/2  ,this.getHeight()-rect_size/2  + gasAsFloat()*rect_size/2 ,circle_size,mPaint );
	    canvas.drawCircle(this.getWidth()-rect_size/2 -rollAsFloat()*rect_size/-2 ,this.getHeight()-rect_size/2 + nickAsFloat()*rect_size/2 ,circle_size,mPaint );
	   
	    //canvas.drawCircle(this.getHeight()-rect_size/2 + circle_size/2 ,rect_size/2  - circle_size,circle_size,mPaint );
		   
	    //canvas.drawCircle(x2,y2,30,mPaint );
	    
	    if (PilotingPrefs.showValues()) {
	    	mPaint.setColor(0xFFFFFFFF);
		    
	    	mPaint.setTextSize(20);
	    	canvas.drawText("nick" + act_nick + " - roll" + act_roll + " gas"+ act_gas + " yaw" + act_gier
	    		+" request " +MKProvider.getMK().stats.external_control_request_count + " confirm "+ MKProvider.getMK().stats.external_control_confirm_frame_count ,0,15,mPaint);
	    }
	    
		invalidate();
	}
	
    public boolean onTouch( View arg0, MotionEvent event ) {
      
    	for ( int mevent_i=0; mevent_i<event.getPointerCount(); mevent_i++ )
    		
        
        if (event.getY(mevent_i)>(this.getHeight()-rect_size))
        {
        	Log.i("is in y bounds");
        	
        	if (event.getX(mevent_i)<rect_size)
        	{
        		Log.i("left stick hit");
        		act_gier=(int)(((event.getX(mevent_i)-rect_size/2)/rect_size)*127*2);
        		
        		act_gas=(int)((event.getY(mevent_i)-this.getHeight()+rect_size/2)/rect_size*127*-2);
        
        		if (event.getAction()==MotionEvent.ACTION_UP)
        		{
        			if (PilotingPrefs.hasLeftPadVerticalSpring())
        				act_gas=0;	
        			if (PilotingPrefs.hasLeftPadHorizontalSpring())
        				act_gier=0;
        			
        		}
        	}
        	
        	if (event.getX(mevent_i)> (this.getWidth()-rect_size))
        	{
        		Log.i("right stick hit");
        		act_roll=(int)(((event.getX(mevent_i)-this.getWidth()+rect_size/2)/rect_size)*127*2);
        		
        		act_nick=(int)((event.getY(mevent_i)-this.getHeight()+rect_size/2)/rect_size*127*2);
        		
        		if (event.getAction()==MotionEvent.ACTION_UP)
        		{
        			if (PilotingPrefs.hasRightPadVerticalSpring())
        				act_nick=0;
        			if (PilotingPrefs.hasRightPadHorizontalSpring())
        				act_roll=0;
        		}
        	}
        	
        	
        }
            
    	uptate2mk();
        return true;
    }

}
