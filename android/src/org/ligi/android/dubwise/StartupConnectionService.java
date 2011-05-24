/**************************************************************************
 *                                          
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

package org.ligi.android.dubwise;

import it.gerdavax.easybluetooth.LocalDevice;
import it.gerdavax.easybluetooth.ReadyListener;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.io.BluetoothCommunicationAdapter;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.simulation.SimulatedMKCommunicationAdapter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Class to fire up a connection on startup
 * 
 * TODO Rename to AutoConnectionActivity
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class StartupConnectionService {
	
	/**
	 * toast for the user and log it
	 * 
	 * @param msg
	 * @param ctx
	 */
	public static void tellNlog(String msg,Context ctx) {
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
		Log.i(msg);
		
	}

	public static void start(Context context) {

		// stop if already connected
		if (MKProvider.getMK().isConnected())
			return;
		
		switch(DUBwisePrefs.getStartConnType()) {
			case DUBwisePrefs.STARTCONNTYPE_BLUETOOTH:
				/*BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (mBluetoothAdapter == null) {
					tellNlog("#Fail: Bluetooth is not supported by device" , context);
				    return;
				}
				mBluetoothAdapter.enable()
				if (!mBluetoothAdapter.isEnabled()) {
				    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				    startActivityForResult(enableBtIntent, 1);
				    context.startActivityForResult();
				}
				*/
				
				class myReadyListener extends ReadyListener {
					
					Context context;
					public myReadyListener(Context context) {
						this.context=context;
					}
					@Override
					public void ready() {
						tellNlog("Conecting to " + DUBwisePrefs.getStartConnBluetootName() + " - " + DUBwisePrefs.getStartConnBluetootMAC() , context);
						MKProvider.getMK().setCommunicationAdapter(new BluetoothCommunicationAdapter(DUBwisePrefs.getStartConnBluetootMAC()));
					}
				}
					
				tellNlog( "switching bluetooth ON", context);
				LocalDevice.getInstance().init(context.getApplicationContext(), new myReadyListener(context));
				break;
				
			case DUBwisePrefs.STARTCONNTYPE_SIMULATION:
				tellNlog( "connecting to simulation", context);
				MKProvider.getMK().setCommunicationAdapter(new SimulatedMKCommunicationAdapter());
				//ConnectionHandler.setCommunicationAdapter(new SimulatedMKCommunicationAdapter());
				break;
					
			default:
				tellNlog("No default Connection in StartupConnectionService",context); 
				break;
			}
	}
}