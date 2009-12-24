package org.ligi.android.dubwise;

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
	}
	  
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	}

	@Override
	protected void onDraw(Canvas canvas) {
	    mPaint.setColor(0xCCCCCCCC);
	   
	    canvas.drawText("az" + azimuth , 10,10,mPaint );
	    canvas.drawText("pitch" + pitch , 10,40,mPaint );
	    canvas.drawText("pitch" + roll , 10,70,mPaint );
		
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
