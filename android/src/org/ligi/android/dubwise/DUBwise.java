package org.ligi.android.dubwise;

import java.util.Vector;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.util.Log;

import android.net.Uri;

import android.content.SharedPreferences;

public class DUBwise extends ListActivity {

	// DUBwiseView canvas;
	boolean do_sound;
	boolean fullscreen;
	SharedPreferences settings;

	public final static int ACTIONID_QUIT=1;
		 
	 /** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    
	    /*
	    try {
	        log("system service" +  this.getSystemService("bluetooth"));

	        log ("Platform Supported? " + PlatformChecker.isThisPlatformSupported());
	        //LocalBluetoothDevice.
	        PlatformChecker.printPlatformDescription();
	         log (" bt init #1");
	        LocalBluetoothDevice.initLocalDevice(this);
	        log (" bt init #2");
	        LocalBluetoothDevice.initLocalDevice(this);
	        
            log (" local device " +LocalBluetoothDevice.getLocalDevice() );
            BluetoothSocket bt_connection;
            bt_connection=LocalBluetoothDevice.getLocalDevice().getRemoteBluetoothDevice("00:0B:CE:01:2E:00").openSocket(1 );
            log (" reading from device" + bt_connection.getInputStream().read());
            MKProvider.getMK().context=this;
        }
        catch (Exception e) {
            log("bt exception" + e);
            // XXX Auto-generated catch block
            
        }
        */
	    
	    ActivityCalls.beforeContent(this);
        
	    Vector<IconicMenuItem> menu_items_vector=new Vector<IconicMenuItem>();
	    
	    menu_items_vector.add(new IconicMenuItem("Connection",android.R.drawable.ic_menu_share,new Intent(this, ConnectionListActivity.class) ) );
	    menu_items_vector.add(new IconicMenuItem("Settings",android.R.drawable.ic_menu_preferences ,new Intent(this, SettingsActivity.class) ) );
	    menu_items_vector.add(new IconicMenuItem("LCD",android.R.drawable.ic_menu_view ,new Intent(this, LCDActivity.class) ) );
	    menu_items_vector.add(new IconicMenuItem("Pilot",android.R.drawable.ic_menu_preferences ,new Intent(this, PilotingListActivity.class) ) );
	    menu_items_vector.add(new IconicMenuItem("Motor Test",android.R.drawable.ic_menu_rotate ,new Intent(this, MotorTestActivity.class) ) );
        menu_items_vector.add(new IconicMenuItem("RCData",android.R.drawable.ic_menu_view ,new Intent(this, RCDataActivity.class) ) );
	    menu_items_vector.add(new IconicMenuItem("Cockpit",android.R.drawable.ic_menu_view ,new Intent(this, CockpitActivity.class) ) );
	    
        menu_items_vector.add(new IconicMenuItem("View on Map",android.R.drawable.ic_menu_mapmode,new Intent(this, RCDataActivity.class) ) );
        
	    menu_items_vector.add(new IconicMenuItem("Flight Settings",android.R.drawable.ic_menu_edit ,new Intent(this, FlightSettingsActivity.class) ) );

	    
	    menu_items_vector.add(new IconicMenuItem("Information Desk",android.R.drawable.ic_menu_preferences ,new Intent(this, InformationDeskActivity.class) ) );
        
	    menu_items_vector.add(new IconicMenuItem("Graph",android.R.drawable.ic_menu_view ,new Intent(this, GraphActivity.class) ) );

	    

	    menu_items_vector.add(new IconicMenuItem("About" , android.R.drawable.ic_menu_info_details, new Intent( "android.intent.action.VIEW", Uri.parse( "http://www.ligi.de/" ))));
	    menu_items_vector.add(new IconicMenuItem("Quit" , android.R.drawable.ic_menu_close_clear_cancel,ACTIONID_QUIT));
	    
		settings = getSharedPreferences("DUBWISE", 0);
		
		 //		getWindow().setFeatureInt(featureId, value)
		
		//settings.
		
		// menu_items[0]=settings.getString("conn_host","--");
		/*this.setListAdapter(new ArrayAdapter<String>(this,
		 android.R.layout.simple_list_item_1, menu_items). );
		*/
		//		menu_items_=(menu_items_vector.toArray());
		this.setListAdapter(new IconicAdapter(this,(menu_items_vector.toArray())));
		Log.d("DUWISE", "create");
		//		this.setTitle("DUBwise Main Menu");
		//ActivityCalls.afterContent(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
		//		finish();
		//		startActivity(new Intent(this, DUBwise.class));
		Log.d("DUBWISE", "resume");
		
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void log(String msg) {
		Log.d("DUWISE", msg);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	
		IconicMenuItem item  = ((IconicMenuItem)(this.getListAdapter().getItem(position) )) ;

		switch (item.action) {
		    case ACTIONID_QUIT:
		        
		        //MKProvider.getMK().close_connections(true );
		          
		        // MKProvider.disposeMK();
		           finish();
		        break;
		}
		
		if (item.intent!=null)
		    startActivity(item.intent);
		
	}

	
	
	
}
