package org.ligi.android.dubwise;

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
