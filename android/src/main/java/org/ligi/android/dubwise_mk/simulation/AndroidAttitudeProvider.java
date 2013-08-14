/**************************************************************************
 *                                          
 * This code is part of DUBwise 
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

package org.ligi.android.dubwise_mk.simulation;

import org.ligi.ufo.simulation.AttitudeProvider;

import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

/**
 * class that implements AttitudeProvider for usage with simulated connection 
 * with the SensorData of the Android Device
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class AndroidAttitudeProvider implements AttitudeProvider, SensorListener {

	private float pitch;
	private float roll;
	
	private SensorManager sensorManager;
	private int sensor = SensorManager.SENSOR_ORIENTATION;
	
	public AndroidAttitudeProvider(Context ctx) {
		sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	public void onAccuracyChanged(int arg0, int arg1) {
	}
	
	@Override
	public void onSensorChanged(int sensor, float[] values) {
		pitch = values[1];
        roll =-1*values[2];
	}
	
	@Override
	public int getNick() {
		return (int)(pitch*10);
	}

	@Override
	public int getRoll() {
		return (int)(roll*10);
	}

	@Override
	public int getYaw() {
		return 0;
	}

}
