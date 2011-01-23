package org.ligi.android.dubwise.uavtalk;

import org.ligi.ufo.VesselData;
import org.openpilot.uavtalk.UAVObjects;

/**
 * class to copy values aggregated in VesselData ( e.g. from MK or simulation ) to UAVObjects
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class VesselData2UAVObjectsThread implements Runnable {

	@Override
	public void run() {
		UAVObjects.init();
		while (true) {
			UAVObjects.getFlightBatteryState().setVoltage(VesselData.battery.getVoltage()/10.0f);
			UAVObjects.getFlightBatteryState().setCurrent(VesselData.battery.getCurrent());
			UAVObjects.getFlightBatteryState().setConsumedEnergy(VesselData.battery.getUsedCapacity());
			
			UAVObjects.getAttitudeActual().setPitch(VesselData.attitude.getNick()/10.0f);
			UAVObjects.getAttitudeActual().setRoll(VesselData.attitude.getRoll()/10.0f);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}		
	}

}
