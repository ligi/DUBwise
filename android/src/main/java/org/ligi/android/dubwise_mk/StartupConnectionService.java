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

package org.ligi.android.dubwise_mk;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.widget.Toast;

import org.ligi.android.dubwise_mk.conn.ConnectionStatusAlertDialog;
import org.ligi.android.dubwise_mk.app.App;
import org.ligi.android.io.BluetoothCommunicationAdapter;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.simulation.SimulatedMKCommunicationAdapter;

/**
 * Class to fire up a connection on startup
 * <p/>
 * TODO Rename to AutoConnectionActivity
 *
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 */
public class StartupConnectionService {

    /**
     * toast for the user and log it
     *
     * @param msg
     * @param ctx
     */
    public static void tellNlog(String msg, Activity ctx) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
        Log.i(msg);

    }

    public static void start(Activity context) {

        // stop if already connected
        if (App.getMK().isConnected())
            return;

        switch (DUBwisePrefs.getStartConnType()) {
            case DUBwisePrefs.STARTCONNTYPE_BLUETOOTH:
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    tellNlog("#Fail: Bluetooth is not supported by device", context);
                    return;
                }

                mBluetoothAdapter.enable();
                App.getMK().setCommunicationAdapter(new BluetoothCommunicationAdapter(DUBwisePrefs.getStartConnBluetootMAC()));
                App.getMK().connect_to("btspp://" + DUBwisePrefs.getStartConnBluetootMAC(), DUBwisePrefs.getStartConnBluetootName());
                ConnectionStatusAlertDialog.show(context);

				/*
				
				class myReadyListener extends ReadyListener {
					
					Context context;
					public myReadyListener(Context context) {
						this.context=context;
					}
					@Override
					public void ready() {
						tellNlog("Conecting to " + DUBwisePrefs.getStartConnBluetootName() + " - " + DUBwisePrefs.getStartConnBluetootMAC() , context);
						App.getMK().setCommunicationAdapter(new BluetoothCommunicationAdapter(DUBwisePrefs.getStartConnBluetootMAC()));
					}
				}
					
				tellNlog( "switching bluetooth ON", context);
				LocalDevice.getInstance().init(context.getApplicationContext(), new myReadyListener(context));
				*/
                break;

            case DUBwisePrefs.STARTCONNTYPE_SIMULATION:
                tellNlog("connecting to simulation", context);
                App.getMK().setCommunicationAdapter(new SimulatedMKCommunicationAdapter());
                //ConnectionHandler.setCommunicationAdapter(new SimulatedMKCommunicationAdapter());
                break;

            default:
                tellNlog("No default Connection in StartupConnectionService", context);
                break;
        }
    }
}