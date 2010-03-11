/**************************************************************************
 *                                          
 * Activity to show results of a bluetooth scan an option 
 * to connect to the found devices
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

package org.ligi.android.dubwise.con.bluetooth;

import java.util.Vector;

import org.ligi.android.dubwise.DUBwisePrefs;
import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import it.gerdavax.easybluetooth.LocalDevice;
import it.gerdavax.easybluetooth.ReadyListener;
import it.gerdavax.easybluetooth.RemoteDevice;
import it.gerdavax.easybluetooth.ScanListener;

public class BluetoothDeviceListActivity extends ListActivity implements Runnable, OnCancelListener, OnClickListener {
	
	private static final int MENU_SCAN = 0;
	private static final int MENU_SCAN_STOP = 1;

	private final static byte CONNECTION_STATE_IDLE=0;
	private final static byte CONNECTION_STATE_STOP_SCAN=1;
	private final static byte CONNECTION_STATE_STOPING_SCAN=2;
	private final static byte CONNECTION_STATE_BUILD_COMM=3;
	private final static byte CONNECTION_STATE_BUILDING_COMM=4;
	private final static byte CONNECTION_STATE_WAIT4COMM =5;
	private final static byte CONNECTION_STATE_WAIT4DEVINFO=6;
	private final static byte CONNECTION_STATE_CONNECTED=7;
	
	private byte conn_state=CONNECTION_STATE_IDLE;
	
	private ArrayAdapter<String> arrayAdapter;
	private Vector<String> bt_friendly_names=new Vector<String>();
	private Vector<String> bt_macs=new Vector<String>();
	
	private ProgressDialog progress_dialog;

	private boolean scanning=false;
	
	public void log(String msg) {
	    Log.i("DUBwise", msg);
	}

	Thread connection_thread=new Thread(this);
	
	int connect_to_id=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Log.i("DUBwise", "starting scan activity");

	    
	    
	    ActivityCalls.beforeContent(this);

		progress_dialog=new ProgressDialog(this);
		progress_dialog.setMessage("Switching on Bluetooth ..");
		progress_dialog.setTitle("Bluetooth");
		progress_dialog.setCancelable(false);
		progress_dialog.setOnCancelListener(this);
		progress_dialog.setButton("Cancel", this);
		progress_dialog.show();

		connection_thread.start();
		
		LocalDevice.getInstance().init(this, new myReadyListener());
		
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		ListView lv = this.getListView();
		lv.setAdapter(arrayAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
		
				connect_to_id=pos;
				conn_state=CONNECTION_STATE_STOP_SCAN;
				
				
			}
		});
		
	}

	String act_dialog_str="";
	 final Handler mHandler = new Handler();
	    
	 	// Create runnable for posting
	   final Runnable mUpdateProgressText = new Runnable() {
	       public void run() {
	    	   if (act_dialog_str.equals(""))
	    		   progress_dialog.hide();
	    	   else {
	    		   progress_dialog.show();
	    		   progress_dialog.setTitle("Connecting");
	    		   progress_dialog.setMessage(act_dialog_str);
	    	   }
	       }
	    };

	@Override
	protected void onResume() {
		super.onResume();
	
		ActivityCalls.afterContent(this);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalDevice.getInstance().stopScan();
		ActivityCalls.onDestroy(this);
	}
		
	class myReadyListener extends ReadyListener {

		@Override
		public void ready() {
			scanning=true;
			LocalDevice.getInstance().scan(new myScanListener());
			progress_dialog.setMessage("Waiting for at least one device");
						
		}
	}

	/* Creates the menu items */
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
	    
		if (!scanning) {
			MenuItem settings_menu=menu.add(0,MENU_SCAN,0,"Scan again");
			settings_menu.setIcon(android.R.drawable.ic_menu_rotate);
		} else {
			
			MenuItem settings_menu=menu.add(0,MENU_SCAN_STOP,0,"Stop Scan");
			settings_menu.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		}
		
	    return true;
	}


	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	    case MENU_SCAN:
	    	if (scanning) LocalDevice.getInstance().stopScan();
	    	arrayAdapter.clear();
	    	bt_friendly_names.clear();
	    	bt_macs.clear();
	    	
	    	scanning=true;
	    	LocalDevice.getInstance().scan(new myScanListener());	
	    	break;
	    }
	    return false;
	}

	class myScanListener extends ScanListener {

		@Override
		public void deviceFound(RemoteDevice remote_device) {
			progress_dialog.hide();
			log("Device found name: " + remote_device.getFriendlyName() + " / mac: " + remote_device.getAddress() + " / rssi: " + remote_device.getRSSI() );
			arrayAdapter
			.add(remote_device.getFriendlyName() + " - " + remote_device.getAddress());
			
			bt_friendly_names.add(remote_device.getFriendlyName());
			bt_macs.add(remote_device.getAddress());
		}

		@Override
		public void scanCompleted() {
			Toast.makeText(BluetoothDeviceListActivity.this, "Scan Completed", Toast.LENGTH_SHORT).show(); 

			
			if (arrayAdapter.getCount()>0) {
				progress_dialog.hide();
				scanning=false;
			}
			else {
				progress_dialog.setMessage("No Device found - trying again");
				LocalDevice.getInstance().scan(new myScanListener());	
			}
		}
		
		
	}

	String connect_to="";
	@Override
	public void run() {
		
		while (true) {
			
			switch (conn_state) {
			
			case CONNECTION_STATE_IDLE:
				// do nothing
				break;
							
			case CONNECTION_STATE_STOP_SCAN:
				act_dialog_str="Stopping scan";
				mHandler.post(mUpdateProgressText);
				LocalDevice.getInstance().stopScan();
				conn_state=CONNECTION_STATE_STOPING_SCAN;
				break;
				
			case CONNECTION_STATE_STOPING_SCAN:
				if (!scanning)
					conn_state=CONNECTION_STATE_BUILD_COMM;
				break;
			
			case CONNECTION_STATE_BUILD_COMM:
				act_dialog_str="Building communication adapter";
				mHandler.post(mUpdateProgressText);
				String btDeviceInfo = connect_to;
				
				
				MKProvider.getMK().setCommunicationAdapter(new BluetoothCommunicationAdapter(bt_macs.get(connect_to_id)));
				Log.i("DUBwise" , "connecting");
				MKProvider.getMK().connect_to("","" );
				Log.i("DUBwise" , "finishing BluetoothDeviceListActivity");
				conn_state=CONNECTION_STATE_BUILDING_COMM;
				break;
			case CONNECTION_STATE_BUILDING_COMM:
				conn_state=CONNECTION_STATE_WAIT4COMM;
				break;
				
			case CONNECTION_STATE_WAIT4COMM:
				act_dialog_str="Waiting for Connection";
				mHandler.post(mUpdateProgressText);
				if (MKProvider.getMK().connected)
					conn_state=CONNECTION_STATE_WAIT4DEVINFO;
				break;
				
			case CONNECTION_STATE_WAIT4DEVINFO:
				
				act_dialog_str="Waiting for Device Info";
				mHandler.post(mUpdateProgressText);
				if (MKProvider.getMK().version.known) 
					conn_state=CONNECTION_STATE_CONNECTED;
				break;
				
			case CONNECTION_STATE_CONNECTED:
				DUBwisePrefs.setStartConnType(DUBwisePrefs.STARTCONNTYPE_BLUETOOTH);
				DUBwisePrefs.setStartConnBluetootName(bt_friendly_names.get(connect_to_id));
				DUBwisePrefs.setStartConnBluetootMAC(bt_macs.get(connect_to_id));
				finish();
				break;
			}
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				
			}
		} // while true
			
			/*
			if (connecting) {
				
				
				act_dialog_str="Stopping scan";
				mHandler.post(mUpdateProgressText);
				
				LocalDevice.getInstance().stopScan();
				
				while (scanning) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						
					}
				}
				// that connections can go through
				String btDeviceInfo = connect_to;
				String btHardwareAddress = btDeviceInfo.substring(btDeviceInfo
						.length() - 17);
								
				Log.i("DUBwise" , "Building Communication adapter for mac " + btHardwareAddress + " name:" +  btDeviceInfo);
				
				act_dialog_str="Building communication adapter";
				mHandler.post(mUpdateProgressText);
				
				
				MKProvider.getMK().setCommunicationAdapter(new BluetoothCommunicationAdapter(btHardwareAddress));
				Log.i("DUBwise" , "connecting");
				MKProvider.getMK().connect_to("btspp://"+btHardwareAddress+"",btDeviceInfo );
				Log.i("DUBwise" , "finishing BluetoothDeviceListActivity");
		
				
				act_dialog_str="Waiting for Connection";
				mHandler.post(mUpdateProgressText);
				
				while (!MKProvider.getMK().connected) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						
					}
				}
				
				
				act_dialog_str="Waiting for Device Info";
				mHandler.post(mUpdateProgressText);
				while ((!MKProvider.getMK().version.known)||(MKProvider.getMK().UBatt()==-1)) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						
					}
				}
				
				act_dialog_str="";
				mHandler.post(mUpdateProgressText);
							
				finish();
				connecting=false;
			} else
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
		
				}
		}*/
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	Log.i("DUBwise","finishing scan activity");
	    	connection_thread.stop();
	    	finish();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		finish();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		finish();
	}

}