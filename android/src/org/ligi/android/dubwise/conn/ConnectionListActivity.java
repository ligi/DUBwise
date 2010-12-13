/**************************************************************************
 *                                          
 * Activity to show menu with possible connection types and options
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

package org.ligi.android.dubwise.conn;

import org.ligi.android.dubwise.conn.bluetooth.BluetoothDeviceListActivity;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseBaseListActivity;
import org.ligi.android.dubwise.simulation.AndroidAttitudeProvider;
import org.ligi.ufo.simulation.SimulatedMKCommunicationAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.widget.ArrayAdapter;

public class ConnectionListActivity extends DUBwiseBaseListActivity {

	private String[] menu_items = new String[] { "Fake Connection","Connect via Bluetooth","connect via TCP/IP" ,"disconnect","reconnect","Switch Device","Connection Details"};
	private int[] menu_actions = new int[] { ACTIONID_SIMULATED , ACTIONID_BT , ACTIONID_TCP , ACTIONID_DISCONN, ACTIONID_RECONNECT , ACTIONID_SWITCH, ACTIONID_CONDETAILS};
	
	public final static int ACTIONID_SIMULATED = 0;
	public final static int ACTIONID_BT = 1;
	public final static int ACTIONID_TCP = 2;
	public final static int ACTIONID_DISCONN = 3;
	public final static int ACTIONID_SWITCH = 4;
	public final static int ACTIONID_CONDETAILS=5;
	public final static int ACTIONID_RECONNECT=6;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
	    this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu_items));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		try {
            switch (menu_actions[position]) {
                case ACTIONID_SIMULATED:
                	SimulatedMKCommunicationAdapter sim=new SimulatedMKCommunicationAdapter();
                	sim.setAttitudeProvider(new AndroidAttitudeProvider(this));
                	MKProvider.getMK().setCommunicationAdapter(sim);
                	finish();
                    break;
                case ACTIONID_BT:
                    startActivity( new Intent( this, BluetoothDeviceListActivity.class ) );
                    break;
                case ACTIONID_TCP:
                    startActivity( new Intent( this, ConnectViaTCPActivity.class ) );
                    break;
                case ACTIONID_DISCONN:
                    MKProvider.getMK().close_connections( true );   
                    break;
    
                case ACTIONID_SWITCH:
                	startActivity( new Intent( this, SwitchDeviceListActivity.class ) );
                    break;
                    
                case ACTIONID_CONDETAILS:
                	startActivity( new Intent( this, ConnectionDetails.class ) );
                	break;
                	
                case ACTIONID_RECONNECT:
                	MKProvider.getMK().close_connections(false);
                	break;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCalls.onDestroy(this);
	}
}
