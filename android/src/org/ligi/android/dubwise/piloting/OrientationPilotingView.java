/**************************************************************************
 *                                          
 * View for Piloting the UFO via ACC / Orientation sensor
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

import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.ufo.MKCommunicator;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.graphics.*;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

public class OrientationPilotingView extends View implements SensorListener 

{
	SensorManager sensorManager;
	private int sensor = SensorManager.SENSOR_ORIENTATION;
    private Paint mPaint = new Paint();
    
	public OrientationPilotingView(Activity context) {
		super(context);
		
	  sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	  sensorManager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_FASTEST);
	
	  MKProvider.getMK().user_intent=MKCommunicator.USER_INTENT_EXTERNAL_CONTROL;
	  
	}
	  
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	}

	@Override
	protected void onDraw(Canvas canvas) {
	    mPaint.setColor(0xCCCCCCCC);
	    int text_size=50;
	    mPaint.setTextSize(text_size);
	    MKProvider.getMK().extern_control[MKCommunicator.EXTERN_CONTROL_NICK]=(int)roll;
	    MKProvider.getMK().extern_control[MKCommunicator.EXTERN_CONTROL_ROLL]=(int)pitch;
	    MKProvider.getMK().extern_control[MKCommunicator.EXTERN_CONTROL_GAS]=(int)100;
	    canvas.drawText("az" + azimuth , 10,text_size,mPaint );
	    canvas.drawText("pitch" + pitch , 10,text_size*2,mPaint );
	    canvas.drawText("roll" + roll , 10,text_size*3,mPaint );
		
	}

    public void onAccuracyChanged( int sensor, int accuracy ) {
    }

    float azimuth;
    float pitch;
    float roll;

    public void onSensorChanged( int sensorReporting, float[] values ) {
        if (sensorReporting != sensor)
            return;

          azimuth = Math.round(values[0]);
          pitch = Math.round(values[1]);
          roll = Math.round(values[2]);

          invalidate();    
    }
	
}
