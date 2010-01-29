
package org.ligi.android.dubwise;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import it.gerdavax.easybluetooth.LocalDevice;
import it.gerdavax.easybluetooth.ReadyListener;
import it.gerdavax.easybluetooth.RemoteDevice;
import it.gerdavax.easybluetooth.ScanListener;

public class BluetoothDeviceListActivity extends ListActivity {
	
	private static final int MENU_SCAN = 0;
	private static final int MENU_SCAN_STOP = 1;

	private ArrayAdapter<String> arrayAdapter;

	private ProgressDialog progress_dialog;
	public void log(String msg) {
	    Log.i("DUBwise", msg);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Log.i("DUBwise", "starting scan activity");
	    
	    ActivityCalls.beforeContent(this);

		progress_dialog=new ProgressDialog(this);
		progress_dialog.setMessage("Switching on Bluetooth ..");
		progress_dialog.show();

		LocalDevice.getInstance().init(this, new myReadyListener());
		
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		ListView lv = this.getListView();
		lv.setAdapter(arrayAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LocalDevice.getInstance().stopScan();
				
				// that connections can go through
				String btDeviceInfo = ((TextView) arg1).getText().toString();
				String btHardwareAddress = btDeviceInfo.substring(btDeviceInfo
						.length() - 17);
								
				MKProvider.getMK().setCommunicationAdapter(new BluetoothCommunicationAdapter(btHardwareAddress));
				MKProvider.getMK().connect_to("btspp://"+btHardwareAddress+"",btDeviceInfo );
			
				finish();
			}
		});
		
	}

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
	}
	
	private boolean scanning=false;
		
	class myReadyListener extends ReadyListener {

		@Override
		public void ready() {
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
	    	LocalDevice.getInstance().stopScan();
	    	arrayAdapter.clear();
	    	LocalDevice.getInstance().scan(new myScanListener());	
	    	break;
	    }
	    return false;
	}

	class myScanListener extends ScanListener {

		@Override
		public void deviceFound(RemoteDevice tobounce) {
			progress_dialog.hide();
			log("Device found" + tobounce.getFriendlyName());
			arrayAdapter
			.add(tobounce.getFriendlyName() + " - " + tobounce.getAddress());
		}

		@Override
		public void scanCompleted() {
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
}