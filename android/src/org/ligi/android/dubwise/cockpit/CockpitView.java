package org.ligi.android.dubwise.cockpit;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.graphics.*;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

// not working atm - import org.bluez.*;
import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.ufo.*;

public class CockpitView extends View implements DUBwiseDefinitions, SensorListener,OnTouchListener

{
	private Paint mPaint = new Paint();
	private Paint altitudeTextPaint = new Paint();
	
	SensorManager sensorManager;
	private int sensor = SensorManager.SENSOR_ORIENTATION;
	
	RectF alt_rect=new RectF(0.0f,0.0f,0.0f,0.0f);
	
	public CockpitView(Activity context) {
		super(context);

		// needed to get Key Events
		setFocusable(true);

		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_FASTEST);
		altitudeTextPaint.setTextSize(10);
		altitudeTextPaint.setColor(0xFFFFFFFF);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		 alt_rect.left=10.0f;
		 alt_rect.top=h - altitudeTextPaint.getTextSize()*2;
		 
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Paint paint = mPaint;
		paint.setAntiAlias(true);

		paint.setARGB(255, 255, 0, 0);

		if (MKProvider.getMK().is_fake() )
			canvas.rotate(roll,getWidth()/2,getHeight()/2);
		else
			canvas.rotate((MKProvider.getMK().AngleRoll()*(-90))/3000,getWidth()/2,getHeight()/2);

        
        
        paint.setARGB(255,177,129,0);
        // roll rect                                                                                                                       
        canvas.drawRect(-getWidth(),getHeight()/2,2*getWidth(),3*getHeight()/2,paint);

        int bar_height=20;
        // nick rect                                                                                                                       
        paint.setARGB(200,0,200,0);
        
        if (MKProvider.getMK().is_fake() )
;//        	canvas.drawRoundRect(new RectF(getWidth()/3,getHeight()/2 -bar_height/2 + pitch*getHeight()/130.0 ,2*getWidth()/3, getHeight()/2+ pitch*getHeight()/130.0+bar_height),5,5,paint);
		else
			canvas.drawRoundRect(new RectF(getWidth()/3,getHeight()/2 -bar_height/2 + MKProvider.getMK().AngleNick()*getHeight()/(3*3000) ,2*getWidth()/3, getHeight()/2+ MKProvider.getMK().AngleNick()*getHeight()/(3*3000)+bar_height),5,5,paint);


        canvas.restore();

//        paint.setARGB(state_intro_frame,0,0,255);
        //              canvas.drawRoundRect(new RectF(getWidth()/2-getWidth()/8,getHeight()/2-getWidth()/8,getWidth()/2+getWidth()/8,getH\
//eight()/2+getWidth()/8),5,5,paint);                                                                                                                

        //canvas.drawRoundRect(new RectF(flight_x,flight_y,flight_x+getWidth()/8,flight_y+getWidth()/8),5,5,paint);
        //paint.setARGB(255,0,0,0);


        canvas.restore();
        
        canvas.drawText("Altitude" + MKProvider.getMK().Alt(), alt_rect.left,alt_rect.right, altitudeTextPaint);
		invalidate();
		
		
	}

	@Override
	public void onAccuracyChanged(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	float pitch;
    float roll;

	@Override
	public void onSensorChanged(int sensor, float[] values) {
		pitch = values[1];
        roll =-1*values[2];
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
