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

package org.ligi.android.dubwise_mk;

import java.util.Vector;

import org.ligi.android.dubwise_mk.app.ApplicationContext;
import org.ligi.android.dubwise_mk.balance.BalanceActivity;
import org.ligi.android.dubwise_mk.cockpit.CockpitActivity;
import org.ligi.android.dubwise_mk.cockpit.VarioSound;
import org.ligi.android.dubwise_mk.conn.ConnectionListActivity;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.flightsettings.FlightSettingsActivity;
import org.ligi.android.dubwise_mk.graph.GraphActivity;
import org.ligi.android.dubwise_mk.helper.ActivityCalls;
import org.ligi.android.dubwise_mk.helper.DUBwiseStringHelper;
import org.ligi.android.dubwise_mk.helper.IconicAdapter;
import org.ligi.android.dubwise_mk.helper.IconicMenuItem;
import org.ligi.android.dubwise_mk.lcd.LCDActivity;
import org.ligi.android.dubwise_mk.map.DUBwiseMap;
import org.ligi.android.dubwise_mk.piloting.PilotingListActivity;
import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.logging.Log;
import org.ligi.tracedroid.sending.TraceDroidEmailSender;
import org.ligi.ufo.DUBwiseNotificationListenerInterface;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.logging.NotLogger;
import org.ligi.ufo.tests.MKHelperTests;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DUBwise extends ListActivity implements DUBwiseNotificationListenerInterface , Runnable{

	private VarioSound vs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("sane" + MKHelperTests.isConvertionSane());
		Log.i("TableLen" + DUBwiseStringHelper.table.length);
		TraceDroid.init(this);
		Log.setTAG("DUBwise"); // It's all about DUBwise from here ;-)
		
		TraceDroidEmailSender.sendStackTraces("ligi@ligi.de", this);

		ActivityCalls.beforeContent(this);

		refresh_list();
		Log.d("created DUBwise class");
		
		
		vs= new VarioSound((ApplicationContext)this.getApplicationContext());
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

		menu_items_vector.add(new IconicMenuItem("Map",
				android.R.drawable.ic_menu_mapmode, new Intent(this,
						DUBwiseMap.class)));

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

			if ( mk.is_navi() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("Follow me",
						android.R.drawable.ic_menu_crop, new Intent(
								this, FollowMeActivity.class)));

			if (mk.is_mk() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("Motor Test",
						android.R.drawable.ic_menu_rotate, new Intent(this,
								MotorTestActivity.class)));

			if (mk.is_mk() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("RCData",
						android.R.drawable.ic_menu_view, new Intent(this,
								RCDataActivity.class)));
			
			if (mk.is_mk() || mk.is_fake() || mk.is_navi())
				menu_items_vector.add(new IconicMenuItem("Balance",
						android.R.drawable.ic_menu_crop, new Intent(this,
								BalanceActivity.class)));

			if (mk.is_mk() || mk.is_navi() || mk.is_fake())
				menu_items_vector.add(new IconicMenuItem("Cockpit",
						android.R.drawable.ic_menu_view, new Intent(this,
								CockpitActivity.class)));

			if (mk.is_mk() || mk.is_navi() || mk.is_fake() || mk.is_mk3mag())
				menu_items_vector.add(new IconicMenuItem("Analog Values",
						android.R.drawable.ic_menu_view, new Intent(this,
								AnalogValuesActivity.class)));


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

		this.setListAdapter(new IconicAdapter(this, (menu_items_vector
				.toArray())));

	}
	
	@Override
	public void onBackPressed() {
		
		LinearLayout lin=new LinearLayout(this);
		lin.setOrientation(LinearLayout.VERTICAL);
		
		Button kidding_btn=new Button(this);
		kidding_btn.setText("No - just kidding");
		
		kidding_btn.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((AlertDialog)view.getTag()).hide();
			}
		});
		
		lin.addView(kidding_btn);
		
		Button yes_btn=new Button(this);
		yes_btn.setText("Yes");
		lin.addView(yes_btn);
		
		yes_btn.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityCalls.shutdownDUBwise();
				finish();
			}
		});
		
		
		Button rmbt_btn=new Button(this);
		rmbt_btn.setText("Yes and disable BT");
		
		rmbt_btn.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityCalls.shutdownDUBwise();
				BluetoothAdapter.getDefaultAdapter().disable();
				finish();
			}
		});
		
		lin.addView(rmbt_btn);
				
		Button stay_awake_btn=new Button(this);
		stay_awake_btn.setText("Yes - but stay awake");
		lin.addView(stay_awake_btn);
		
		stay_awake_btn.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		
		kidding_btn.setTag((new AlertDialog.Builder(this)).setView(lin).setTitle("Exit DUBwise " + getVersionCode() + " ?").show());
		
	}
	
	private String getVersionCode() {
		try
		{
			return "v"+getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		}
		catch (NameNotFoundException e)
		{
		    return "v?";
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
		updateMKLogging();
		Log.d("onResume DUBwise.java" + this);
		refresh_list();
		MKProvider.getMK().addNotificationListener(this);
	}
	
	/**
	 * enable/disable MK Protocol logging depending on user settings
	 */
	public void updateMKLogging() {
		if (DUBwisePrefs.isVerboseLoggingEnabled())
			MKProvider.getMK().setLoggingInterface(new AndroidLogger());
		else
			MKProvider.getMK().setLoggingInterface(new NotLogger());
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		ActivityCalls.onDestroy(this);
		super.onDestroy();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		IconicMenuItem item = ((IconicMenuItem) (this.getListAdapter().getItem(position)));

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
