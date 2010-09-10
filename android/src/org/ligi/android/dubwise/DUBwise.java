/**************************************************************************
 *                                          
 * Main Menu ( & startup ) Activity for DUBwise 
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

package org.ligi.android.dubwise;

import java.util.Vector;

import org.ligi.android.dubwise.blackbox.BlackBox;
import org.ligi.android.dubwise.cockpit.CockpitActivity;
import org.ligi.android.dubwise.conn.ConnectionListActivity;
import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.conn.bluetooth.BluetoothMaster;
import org.ligi.android.dubwise.flightsettings.FlightSettingsActivity;
import org.ligi.android.dubwise.graph.GraphActivity;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.IconicAdapter;
import org.ligi.android.dubwise.helper.IconicMenuItem;
import org.ligi.android.dubwise.lcd.LCDActivity;
import org.ligi.android.dubwise.map.DUBwiseMap;
import org.ligi.android.dubwise.piloting.PilotingListActivity;
import org.ligi.android.dubwise.voice.StatusVoice;
import org.ligi.android.dubwise.voice.VoicePrefs;
import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.logging.Log;
import org.ligi.tracedroid.sending.TraceDroidEmailSender;
import org.ligi.ufo.DUBwiseNotificationListenerInterface;
import org.ligi.ufo.MKCommunicator;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class DUBwise extends ListActivity implements DUBwiseNotificationListenerInterface , Runnable{

	public final static int ACTIONID_QUIT = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		Log.setTAG("DUBwise");
		TraceDroid.init(this);
		TraceDroidEmailSender.sendStackTraces("ligi@ligi.de", this);

		
		BluetoothMaster.init(this);
		
		VoicePrefs.init(this);
		
		if (VoicePrefs.isVoiceEnabled())
			StatusVoice.getInstance().init(this);
		
		ActivityCalls.beforeContent(this);
		refresh_list();

		StartupConnectionService.start(this);
		BlackBox.init();
		Log.d("create");

	}

	public void refresh_list() {
		MKCommunicator mk = MKProvider.getMK();
		Vector<IconicMenuItem> menu_items_vector = new Vector<IconicMenuItem>();

		menu_items_vector.add(new IconicMenuItem("Connection",
				android.R.drawable.ic_menu_share, new Intent(this,
						ConnectionListActivity.class)));
	
		menu_items_vector.add(new IconicMenuItem("Settings",
				android.R.drawable.ic_menu_preferences, new Intent(this,
						SettingsListActivity.class)));

		
		if (DUBwisePrefs.isExpertModeEnabled())
		{
			menu_items_vector.add(new IconicMenuItem("OpenGL",
					android.R.drawable.ic_menu_preferences, new Intent(this,
							OpenGLActivity.class)));

			menu_items_vector.add(new IconicMenuItem("Flash Firmware",
					android.R.drawable.ic_menu_preferences, new Intent(this,
							FlashFirmwareActivity.class)));

			menu_items_vector.add(new IconicMenuItem("Control Panel",
				android.R.drawable.ic_menu_preferences, new Intent(this,
						ControlPanelActivity.class)));
		
			menu_items_vector.add(new IconicMenuItem("Voice",
				android.R.drawable.ic_menu_view, new Intent(this,
						VoiceControlActivity.class)));
		}
		
		if (mk.connected) {
			
				menu_items_vector.add(new IconicMenuItem("Device Details",
						android.R.drawable.ic_menu_view, new Intent(this,
								DeviceDetails.class)));

				
				menu_items_vector.add(new IconicMenuItem("LCD",
						android.R.drawable.ic_menu_view, new Intent(this,
								LCDActivity.class)));

			if (mk.is_mk() || mk.is_navi() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("Pilot",
						android.R.drawable.ic_menu_preferences, new Intent(
								this, PilotingListActivity.class)));

			if (mk.is_mk() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("Motor Test",
						android.R.drawable.ic_menu_rotate, new Intent(this,
								MotorTestActivity.class)));

			if (mk.is_mk() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("RCData",
						android.R.drawable.ic_menu_view, new Intent(this,
								RCDataActivity.class)));

			if (mk.is_mk() || mk.is_navi() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("Cockpit",
						android.R.drawable.ic_menu_view, new Intent(this,
								CockpitActivity.class)));

			if (mk.is_mk() || mk.is_navi() || mk.is_fake() || mk.is_mk3mag())
				menu_items_vector.add(new IconicMenuItem("Analog Values",
						android.R.drawable.ic_menu_view, new Intent(this,
								AnalogValuesActivity.class)));

			if (mk.is_navi() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("View on Map",
						android.R.drawable.ic_menu_mapmode, new Intent(this,
								DUBwiseMap.class)));

			if (mk.is_mk() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("Flight Settings",
						android.R.drawable.ic_menu_edit, new Intent(this,
								FlightSettingsActivity.class)));

			if (mk.is_mk() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("Edit Mixer",
						android.R.drawable.ic_menu_edit, new Intent(this,
								MixerEditActivity.class)));

			
			if (mk.is_mk() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("Graph",
						android.R.drawable.ic_menu_view, new Intent(this,
								GraphActivity.class)));

		}
		menu_items_vector.add(new IconicMenuItem("Information Desk",
				android.R.drawable.ic_menu_info_details, new Intent(this,
						InformationDeskActivity.class)));

		// menu_items_vector.add(new IconicMenuItem("Quit" ,
		// android.R.drawable.ic_menu_close_clear_cancel,ACTIONID_QUIT));

		this.setListAdapter(new IconicAdapter(this, (menu_items_vector
				.toArray())));

	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
		MKProvider.getMK().do_log=DUBwisePrefs.isVerboseLoggingEnabled();
		Log.d("onResume DUBwise.java");
		
		refresh_list();
		
		MKProvider.getMK().addNotificationListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCalls.onDestroy(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		IconicMenuItem item = ((IconicMenuItem) (this.getListAdapter()
				.getItem(position)));

		if (item.intent != null)
			startActivity(item.intent);

	}

	public void processNotification(byte notification) {
		switch(notification) {
			case NOTIFY_CONNECTION_CHANGED:
				this.runOnUiThread(this);
				break;
		}
	}

	@Override
	public void run() {
		refresh_list();
	}
}
