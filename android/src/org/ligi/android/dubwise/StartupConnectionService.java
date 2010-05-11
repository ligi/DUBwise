/**************************************************************************
 *                                          
 * Class to initiate a connection on startup
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

import org.ligi.android.dubwise.con.bluetooth.BluetoothCommunicationAdapter;
import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.tracedroid.logging.Log;

import android.content.Context;
import android.widget.Toast;

public class StartupConnectionService {
	
	public static void start(Context context) {
		//if (MKProvider.getMK().getCommunicationAdapter()==null)
			switch(DUBwisePrefs.getStartConnType()) {
				case DUBwisePrefs.STARTCONNTYPE_BLUETOOTH:
					class myReadyListener extends ReadyListener {

						Context context;
						public myReadyListener(Context context) {
							this.context=context;
						}
						@Override
						public void ready() {
							
							Toast.makeText(context, "Conecting to " + DUBwisePrefs.getStartConnBluetootName() + " - " + DUBwisePrefs.getStartConnBluetootMAC(), Toast.LENGTH_LONG).show();

							MKProvider.getMK().setCommunicationAdapter(new BluetoothCommunicationAdapter(DUBwisePrefs.getStartConnBluetootMAC()));
							Log.i( "connecting");
							MKProvider.getMK().connect_to("","" );
						
						}
					}
					Toast.makeText(context, "Enabling Bluetooth", Toast.LENGTH_LONG).show();
					LocalDevice.getInstance().init(context.getApplicationContext(), new myReadyListener(context));
				break;
				
			default:
				Toast.makeText(context, "No default Connection", Toast.LENGTH_SHORT).show(); 
				break;
			}
	}
	
	
	
}
