/*

 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ligi.android.dubwise;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple list activity that displays Bluetooth devices that are in
 * discoverable mode. This can be used as a gamelobby where players can see
 * available servers and pick the one they wish to connect to.
 */

public class ServerListActivity extends ListActivity {
	public static String EXTRA_SELECTED_ADDRESS = "btaddress";

	private BluetoothAdapter btAdapter;

	private ServerListActivity self;

	private ArrayAdapter<String> arrayAdapter;

	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Parcelable btParcel = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			BluetoothDevice btDevice = (BluetoothDevice) btParcel;
			arrayAdapter
					.add(btDevice.getName() + " - " + btDevice.getAddress());
		}
	};

	public void log(String msg)
	{
	    Log.i("DUBwise", msg);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    Log.i("DUBwise", "starting scan activity");
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		self = this;

		arrayAdapter = new ArrayAdapter<String>(self, android.R.layout.simple_list_item_1);
		ListView lv = self.getListView();
		lv.setAdapter(arrayAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				btAdapter.cancelDiscovery(); // Cancel BT discovery explicitly so
				// that connections can go through
				String btDeviceInfo = ((TextView) arg1).getText().toString();
				String btHardwareAddress = btDeviceInfo.substring(btDeviceInfo
						.length() - 17);
				/*Intent i = new Intent();
				i.putExtra(EXTRA_SELECTED_ADDRESS, btHardwareAddress);
				self.setResult(Activity.RESULT_OK, i);*/
				MKProvider.getMK().connect_to("btspp://"+btHardwareAddress+"",btDeviceInfo );
				finish();
			}
		});
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		

        if (!btAdapter.isEnabled()) {
            log("bt is disabled ->enabling");
            btAdapter.enable();
        }
            
        
        while (btAdapter.getState()!=BluetoothAdapter.STATE_ON)
        {
            // todo implement timeout
            try {
                Thread.sleep(100 );
            }
            catch (InterruptedException e) {
                // sleeping is not that important
            }
            log("waiting for bt to be enabled");
        }
		
		
		if (btAdapter == null) {
			Log.e("Bluetooth Scan Error", "BluetoothAdapter.getDefaultAdapter() returned null" );
			self.setResult(Activity.RESULT_CANCELED);
			finish();
		}
		else 
			btAdapter.startDiscovery();

		self.setResult(Activity.RESULT_CANCELED);
/*		
	       arrayAdapter
           .add("foo");
	*/
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(myReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(myReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (btAdapter != null) {
			btAdapter.cancelDiscovery(); // Ensure that we don't leave discovery
			// running by accident
		}
	}
	
}