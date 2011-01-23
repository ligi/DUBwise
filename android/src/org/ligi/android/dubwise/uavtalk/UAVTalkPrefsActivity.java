package org.ligi.android.dubwise.uavtalk;

/**************************************************************************
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

import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.preferences.SetPreferenceEnabledByCheckBoxPreferenceState;
import org.ligi.android.dubwise.voice.VoiceDebugActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

/**
 * Activity to edit the UAVTalk Preferences
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UAVTalkPrefsActivity extends PreferenceActivity {

	private CheckBoxPreference doFlightTelemetry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		UAVTalkPrefs.init(this);
		ActivityCalls.beforeContent(this);
		super.onCreate(savedInstanceState);
		setPreferenceScreen(createPreferenceHierarchy());
	}

	@Override
	public void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	private PreferenceScreen createPreferenceHierarchy() {
		// Root
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
		root.setPersistent(true);
	
		/* Display */
		PreferenceCategory displayPrefCat = new PreferenceCategory(this);
		displayPrefCat.setTitle("Display");
		root.addPreference(displayPrefCat);

		CheckBoxPreference doDisplayInMainMenu = new CheckBoxPreference(this);
		doDisplayInMainMenu.setKey(UAVTalkPrefs.KEY_ENABLE_UAVOBJECTS_MAINMENU);
		doDisplayInMainMenu.setTitle("in MainMenu");
		doDisplayInMainMenu.setSummary("show UAVOBjects entry in MainMenu ");
		doDisplayInMainMenu.setChecked(UAVTalkPrefs.isUAVObjectsMainMenuEnabled());
		displayPrefCat.addPreference(doDisplayInMainMenu);

		CheckBoxPreference doDisplayExtendedFlightTelemetry = new CheckBoxPreference(this);
		doDisplayExtendedFlightTelemetry.setKey(UAVTalkPrefs.KEY_ENABLE_DESCRIPTIONDISPLAY);
		doDisplayExtendedFlightTelemetry.setTitle("UAVObject Description");
		doDisplayExtendedFlightTelemetry.setSummary("show UAVOBject Descriptions - compact vs. info & you might know desc after a while");
		doDisplayExtendedFlightTelemetry.setChecked(UAVTalkPrefs.isUAVObjectDescriptionDisplayEnabled());
		displayPrefCat.addPreference(doDisplayExtendedFlightTelemetry);

		CheckBoxPreference doDisplayUAVObjectFieldUnits = new CheckBoxPreference(this);
		doDisplayUAVObjectFieldUnits.setKey(UAVTalkPrefs.KEY_ENABLE_UAVTALK_UNITS);
		doDisplayUAVObjectFieldUnits.setTitle("units");
		doDisplayUAVObjectFieldUnits.setSummary("show the unit for UAVObject Fields");
		doDisplayUAVObjectFieldUnits.setChecked(UAVTalkPrefs.isUAVTalkUnitDisplayEnabled());
		displayPrefCat.addPreference(doDisplayUAVObjectFieldUnits);

		/* Flight Telemetry */
		PreferenceCategory flightTelemetryPrefCat = new PreferenceCategory(this);
		flightTelemetryPrefCat.setTitle("FlightTelemetry");
		root.addPreference(flightTelemetryPrefCat);


		CheckBoxPreference doUDPFlightTelemetry = new CheckBoxPreference(this);
		
		doFlightTelemetry = new CheckBoxPreference(this);
		doFlightTelemetry.setKey(UAVTalkPrefs.KEY_ENABLE_FLIGHTTELEMETRY);
		doFlightTelemetry.setTitle("enable FlightTelemetry");
		doFlightTelemetry.setSummary("let other GCS Software connect to DUBwise via UAVTalk");
		
		flightTelemetryPrefCat.addPreference(doFlightTelemetry);

		doUDPFlightTelemetry.setKey(UAVTalkPrefs.KEY_ENABLE_FLIGHTTELEMETRY_UDP);
		doUDPFlightTelemetry.setTitle("UDP");
		doUDPFlightTelemetry.setSummary("let GCS connect via UDP");
		
		flightTelemetryPrefCat.addPreference(doUDPFlightTelemetry);

        CheckBoxPreference doBluetoothFlightTelemetry = new CheckBoxPreference(this);
        doBluetoothFlightTelemetry.setKey(UAVTalkPrefs.KEY_ENABLE_FLIGHTTELEMETRY_BT);
		doBluetoothFlightTelemetry.setTitle("Bluetooth");
		doBluetoothFlightTelemetry.setSummary("(TODO) let GCS connect via Bluetooth (rfcomm) ");
		doBluetoothFlightTelemetry.setEnabled(false);
		
		flightTelemetryPrefCat.addPreference(doBluetoothFlightTelemetry);

        PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
        intentPref.setIntent(new Intent(this,DUBwiseFlightTelemetryStatusActivity.class));
        intentPref.setTitle("FlightTelemetry Info");
        intentPref.setSummary("show FlightTelemetry Information");
        flightTelemetryPrefCat.addPreference(intentPref);
		
        // link enabled/disabled state between preferences
		new SetPreferenceEnabledByCheckBoxPreferenceState(doFlightTelemetry).addPreference2SetEnable(doUDPFlightTelemetry);
		
		return root;
	}


}
