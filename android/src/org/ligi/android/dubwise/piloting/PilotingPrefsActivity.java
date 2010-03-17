package org.ligi.android.dubwise.piloting;

/**************************************************************************
 *                                          
 * Activity to edit the Piloting Preferences
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


import org.ligi.android.dubwise.DUBwisePrefs;
import org.ligi.android.dubwise.helper.ActivityCalls;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;

public class PilotingPrefsActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnSharedPreferenceChangeListener {

	
	
	private ListPreference rightPadVerticalExternalControlMappingPref;
	private ListPreference leftPadVerticalExternalControlMappingPref;

	private ListPreference rightPadHorizontalExternalControlMappingPref;
	private ListPreference leftPadHorizontalExternalControlMappingPref;
	
	
	private ListPreference rightPadVerticalSerialChannelMappingPref;
	private ListPreference rightPadHorizontalSerialChanelMappingPref;
	
	private ListPreference leftPadVerticalSerialChannelMappingPref;
	private ListPreference leftPadHorizontalSerialChannelMappingPref;

	private CheckBoxPreference doSerialChannelsCheckBoxPref;
	private CheckBoxPreference doExternControlCheckBoxPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PilotingPrefs.init(this);
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
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(
				this);

		root.setPersistent(true);

	
		/* Misc */
		PreferenceCategory miscPrefCat = new PreferenceCategory(this);
		miscPrefCat.setTitle("Misc");
		root.addPreference(miscPrefCat);

		CheckBoxPreference showValuesCheckBoxPref = new CheckBoxPreference(this);
		showValuesCheckBoxPref.setKey(PilotingPrefs.KEY_SHOWVALUES);
		showValuesCheckBoxPref.setTitle("Show Values");
		showValuesCheckBoxPref.setSummary("Show Values on Top");
		showValuesCheckBoxPref.setOnPreferenceChangeListener(this);
		miscPrefCat.addPreference(showValuesCheckBoxPref);

		
		doExternControlCheckBoxPref = new CheckBoxPreference(this);
		doExternControlCheckBoxPref.setKey(PilotingPrefs.KEY_SENDEC);
		doExternControlCheckBoxPref.setTitle("Send Extern Control");
		doExternControlCheckBoxPref.setSummary("mixed with RC");
		doExternControlCheckBoxPref.setOnPreferenceChangeListener(this);
		miscPrefCat.addPreference(doExternControlCheckBoxPref);
		

		doSerialChannelsCheckBoxPref = new CheckBoxPreference(this);
		doSerialChannelsCheckBoxPref.setKey(PilotingPrefs.KEY_SENDSC);
		doSerialChannelsCheckBoxPref.setTitle("Send Serial Channels");
		doSerialChannelsCheckBoxPref.setSummary("overrides RC");
		doSerialChannelsCheckBoxPref.setOnPreferenceChangeListener(this);
		miscPrefCat.addPreference(doSerialChannelsCheckBoxPref);
		
		
		/* Left Pad Horizontal */
		PreferenceCategory leftPadHorizPrefCat = new PreferenceCategory(this);
		leftPadHorizPrefCat.setTitle("Left Pad Horizontal");
		root.addPreference(leftPadHorizPrefCat);

		CheckBoxPreference leftSpringHorizontalCheckBoxPref = new CheckBoxPreference(this);
		leftSpringHorizontalCheckBoxPref.setKey(PilotingPrefs.KEY_LEFT_PAD_SPRING_HORIZONTAL);
		leftSpringHorizontalCheckBoxPref.setTitle("Spring");
		leftSpringHorizontalCheckBoxPref.setSummary("automaticaly center Horizontaly");
		leftSpringHorizontalCheckBoxPref.setOnPreferenceChangeListener(this);
		leftPadHorizPrefCat.addPreference(leftSpringHorizontalCheckBoxPref);

		
		leftPadHorizontalExternalControlMappingPref = new ListPreference(this);
		leftPadHorizontalExternalControlMappingPref.setEntries(PilotingPrefs.getExternControlMappingStrings());
		leftPadHorizontalExternalControlMappingPref.setEntryValues(PilotingPrefs.getExternControlMappingStrings());
		leftPadHorizontalExternalControlMappingPref.setDialogTitle("External Control Mapping");
		leftPadHorizontalExternalControlMappingPref.setKey(PilotingPrefs.KEY_LEFT_PAD_EC_MAPPING_HORIZONTAL);
		leftPadHorizontalExternalControlMappingPref.setTitle("External Control Mapping");
		leftPadHorizontalExternalControlMappingPref.setSummary(PilotingPrefs.getLeftPadHorizontalECMappingStr());
		leftPadHorizontalExternalControlMappingPref.setOnPreferenceChangeListener(this);
		leftPadHorizontalExternalControlMappingPref.setDefaultValue(PilotingPrefs.getLeftPadHorizontalECMappingStr());
		leftPadHorizontalExternalControlMappingPref.setEnabled(PilotingPrefs.isExternControlEnabled());
		
		leftPadHorizPrefCat.addPreference(leftPadHorizontalExternalControlMappingPref);

		
		leftPadHorizontalSerialChannelMappingPref = new ListPreference(this);
		leftPadHorizontalSerialChannelMappingPref.setEntries(PilotingPrefs.getSerialChannelMappingStrings());
		leftPadHorizontalSerialChannelMappingPref.setEntryValues(PilotingPrefs.getSerialChannelMappingStrings());
		leftPadHorizontalSerialChannelMappingPref.setDialogTitle("Serial Channels Mapping");
		leftPadHorizontalSerialChannelMappingPref.setKey(PilotingPrefs.KEY_LEFT_PAD_SC_MAPPING_HORIZONTAL);
		leftPadHorizontalSerialChannelMappingPref.setTitle("Serial Channels Mapping");
		leftPadHorizontalSerialChannelMappingPref.setSummary(PilotingPrefs.getLeftPadHorizontalSCMappingStr());
		leftPadHorizontalSerialChannelMappingPref.setOnPreferenceChangeListener(this);
		leftPadHorizontalSerialChannelMappingPref.setDefaultValue(PilotingPrefs.getLeftPadHorizontalSCMappingStr());
		leftPadHorizontalSerialChannelMappingPref.setEnabled(PilotingPrefs.isSerialChannelsEnabled());
		
		leftPadHorizPrefCat.addPreference(leftPadHorizontalSerialChannelMappingPref);

		
		/* Left Pad Vertical */
		
		PreferenceCategory leftPadVerticalPrefCat = new PreferenceCategory(this);
		leftPadVerticalPrefCat.setTitle("Left Pad Vertical");
		root.addPreference(leftPadVerticalPrefCat);

		
		CheckBoxPreference leftSpringVerticalCheckBoxPref = new CheckBoxPreference(this);
		leftSpringVerticalCheckBoxPref.setKey(PilotingPrefs.KEY_LEFT_PAD_SPRING_VERTICAL);
		leftSpringVerticalCheckBoxPref.setTitle("Spring");
		leftSpringVerticalCheckBoxPref.setSummary("automaticaly center Verticaly");
		leftSpringVerticalCheckBoxPref.setOnPreferenceChangeListener(this);
		leftPadVerticalPrefCat.addPreference(leftSpringVerticalCheckBoxPref);

		leftPadVerticalExternalControlMappingPref = new ListPreference(this);
		leftPadVerticalExternalControlMappingPref.setEntries(PilotingPrefs.getExternControlMappingStrings());
		leftPadVerticalExternalControlMappingPref.setEntryValues(PilotingPrefs.getExternControlMappingStrings());
		leftPadVerticalExternalControlMappingPref.setDialogTitle("External Control Mapping");
		leftPadVerticalExternalControlMappingPref.setKey(PilotingPrefs.KEY_LEFT_PAD_EC_MAPPING_VERTICAL);
		leftPadVerticalExternalControlMappingPref.setTitle("External Control Mapping");
		leftPadVerticalExternalControlMappingPref.setSummary(PilotingPrefs.getLeftPadVerticalECMappingStr());
		leftPadVerticalExternalControlMappingPref.setOnPreferenceChangeListener(this);
		leftPadVerticalExternalControlMappingPref.setDefaultValue(PilotingPrefs.getLeftPadVerticalECMappingStr());
		leftPadVerticalExternalControlMappingPref.setEnabled(PilotingPrefs.isExternControlEnabled());
		
		leftPadVerticalPrefCat.addPreference(leftPadVerticalExternalControlMappingPref);

		leftPadVerticalSerialChannelMappingPref = new ListPreference(this);
		leftPadVerticalSerialChannelMappingPref.setEntries(PilotingPrefs.getSerialChannelMappingStrings());
		leftPadVerticalSerialChannelMappingPref.setEntryValues(PilotingPrefs.getSerialChannelMappingStrings());
		leftPadVerticalSerialChannelMappingPref.setDialogTitle("Serial Channel Mapping");
		leftPadVerticalSerialChannelMappingPref.setKey(PilotingPrefs.KEY_LEFT_PAD_SC_MAPPING_VERTICAL);
		leftPadVerticalSerialChannelMappingPref.setTitle("Serial Channel  Mapping");
		leftPadVerticalSerialChannelMappingPref.setSummary(PilotingPrefs.getLeftPadVerticalSCMappingStr());
		leftPadVerticalSerialChannelMappingPref.setOnPreferenceChangeListener(this);
		leftPadVerticalSerialChannelMappingPref.setDefaultValue(PilotingPrefs.getLeftPadVerticalSCMappingStr());
		leftPadVerticalSerialChannelMappingPref.setEnabled(PilotingPrefs.isSerialChannelsEnabled());
		leftPadVerticalPrefCat.addPreference(leftPadVerticalSerialChannelMappingPref);

		
		
		/* Right Pad */
		PreferenceCategory rightPadHorizontalPrefCat = new PreferenceCategory(this);
		rightPadHorizontalPrefCat.setTitle("Right Pad Horizontal");
		root.addPreference(rightPadHorizontalPrefCat);

		CheckBoxPreference rightSpringHorizontalCheckBoxPref = new CheckBoxPreference(this);
		rightSpringHorizontalCheckBoxPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_SPRING_HORIZONTAL);
		rightSpringHorizontalCheckBoxPref.setTitle("Horizontal Spring");
		rightSpringHorizontalCheckBoxPref.setSummary("automaticaly center Horizontaly");
		rightSpringHorizontalCheckBoxPref.setOnPreferenceChangeListener(this);
		rightPadHorizontalPrefCat.addPreference(rightSpringHorizontalCheckBoxPref);


		rightPadHorizontalExternalControlMappingPref = new ListPreference(this);
		rightPadHorizontalExternalControlMappingPref.setEntries(PilotingPrefs.getExternControlMappingStrings());
		rightPadHorizontalExternalControlMappingPref.setEntryValues(PilotingPrefs.getExternControlMappingStrings());
		rightPadHorizontalExternalControlMappingPref.setDialogTitle("External Control Mapping");
		rightPadHorizontalExternalControlMappingPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_EC_MAPPING_HORIZONTAL);
		rightPadHorizontalExternalControlMappingPref.setTitle("External Control Mapping");
		rightPadHorizontalExternalControlMappingPref.setSummary(PilotingPrefs.getRightPadHorizontalECMappingStr());
		rightPadHorizontalExternalControlMappingPref.setOnPreferenceChangeListener(this);
		rightPadHorizontalExternalControlMappingPref.setDefaultValue(PilotingPrefs.getRightPadHorizontalECMappingStr());
		rightPadHorizontalExternalControlMappingPref.setEnabled(PilotingPrefs.isExternControlEnabled());
		
		rightPadHorizontalPrefCat.addPreference(rightPadHorizontalExternalControlMappingPref);

		rightPadHorizontalSerialChanelMappingPref = new ListPreference(this);
		rightPadHorizontalSerialChanelMappingPref.setEntries(PilotingPrefs.getSerialChannelMappingStrings());
		rightPadHorizontalSerialChanelMappingPref.setEntryValues(PilotingPrefs.getSerialChannelMappingStrings());
		rightPadHorizontalSerialChanelMappingPref.setDialogTitle("Serial Channel  Mapping");
		rightPadHorizontalSerialChanelMappingPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_SC_MAPPING_HORIZONTAL);
		rightPadHorizontalSerialChanelMappingPref.setTitle("Serial Channel  Mapping");
		rightPadHorizontalSerialChanelMappingPref.setSummary(PilotingPrefs.getRightPadHorizontalSCMappingStr());
		rightPadHorizontalSerialChanelMappingPref.setOnPreferenceChangeListener(this);
		rightPadHorizontalSerialChanelMappingPref.setDefaultValue(PilotingPrefs.getRightPadHorizontalSCMappingStr());
		rightPadHorizontalSerialChanelMappingPref.setEnabled(PilotingPrefs.isSerialChannelsEnabled());
		rightPadHorizontalPrefCat.addPreference(rightPadHorizontalSerialChanelMappingPref);

		
		// Right Pad Vertical
		PreferenceCategory rightPadVerticalPrefCat = new PreferenceCategory(this);
		rightPadVerticalPrefCat.setTitle("Right Pad Vertical");
		root.addPreference(rightPadVerticalPrefCat);

		CheckBoxPreference rightSpringVerticalCheckBoxPref = new CheckBoxPreference(this);
		rightSpringVerticalCheckBoxPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_SPRING_VERTICAL);
		rightSpringVerticalCheckBoxPref.setTitle("Vertical Spring");
		rightSpringVerticalCheckBoxPref.setSummary("automaticaly center Verticaly");
		rightSpringVerticalCheckBoxPref.setOnPreferenceChangeListener(this);
		rightPadVerticalPrefCat.addPreference(rightSpringVerticalCheckBoxPref);


		rightPadVerticalExternalControlMappingPref = new ListPreference(this);
		rightPadVerticalExternalControlMappingPref.setEntries(PilotingPrefs.getExternControlMappingStrings());
		rightPadVerticalExternalControlMappingPref.setEntryValues(PilotingPrefs.getExternControlMappingStrings());
		rightPadVerticalExternalControlMappingPref.setDialogTitle("External Control Mapping");
		rightPadVerticalExternalControlMappingPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_EC_MAPPING_VERTICAL);
		rightPadVerticalExternalControlMappingPref.setTitle("External Control Mapping");
		rightPadVerticalExternalControlMappingPref.setSummary(PilotingPrefs.getRightPadVerticalECMappingStr());
		rightPadVerticalExternalControlMappingPref.setOnPreferenceChangeListener(this);
		rightPadVerticalExternalControlMappingPref.setDefaultValue(PilotingPrefs.getRightPadVerticalECMappingStr());
		rightPadVerticalExternalControlMappingPref.setEnabled(PilotingPrefs.isExternControlEnabled());
		rightPadVerticalPrefCat.addPreference(rightPadVerticalExternalControlMappingPref);

		rightPadVerticalSerialChannelMappingPref = new ListPreference(this);
		rightPadVerticalSerialChannelMappingPref.setEntries(PilotingPrefs.getSerialChannelMappingStrings());
		rightPadVerticalSerialChannelMappingPref.setEntryValues(PilotingPrefs.getSerialChannelMappingStrings());
		rightPadVerticalSerialChannelMappingPref.setDialogTitle("Serial Channel  Mapping");
		rightPadVerticalSerialChannelMappingPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_SC_MAPPING_VERTICAL);
		rightPadVerticalSerialChannelMappingPref.setTitle("Serial Channel  Mapping");
		rightPadVerticalSerialChannelMappingPref.setSummary(PilotingPrefs.getRightPadVerticalSCMappingStr());
		rightPadVerticalSerialChannelMappingPref.setOnPreferenceChangeListener(this);
		rightPadVerticalSerialChannelMappingPref.setDefaultValue(PilotingPrefs.getRightPadVerticalSCMappingStr());
		rightPadVerticalSerialChannelMappingPref.setEnabled(PilotingPrefs.isSerialChannelsEnabled());
		rightPadVerticalPrefCat.addPreference(rightPadVerticalSerialChannelMappingPref);

		return root;

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
		if ((preference==rightPadHorizontalExternalControlMappingPref)
				|| (preference==rightPadVerticalExternalControlMappingPref)
				|| (preference==leftPadHorizontalExternalControlMappingPref)
				|| (preference==leftPadVerticalExternalControlMappingPref))
			preference.setSummary((String)newValue);

		
		if (preference==doSerialChannelsCheckBoxPref)
			{
			rightPadVerticalSerialChannelMappingPref.setEnabled((Boolean)newValue);
			leftPadVerticalSerialChannelMappingPref.setEnabled((Boolean)newValue);
			rightPadHorizontalSerialChanelMappingPref.setEnabled((Boolean)newValue);
			leftPadHorizontalSerialChannelMappingPref.setEnabled((Boolean)newValue);
			}
		
		if (preference==doExternControlCheckBoxPref)
			{
			rightPadVerticalExternalControlMappingPref.setEnabled((Boolean)newValue);
			leftPadVerticalExternalControlMappingPref.setEnabled((Boolean)newValue);
			rightPadHorizontalExternalControlMappingPref.setEnabled((Boolean)newValue);
			leftPadHorizontalExternalControlMappingPref.setEnabled((Boolean)newValue);
		}
	
		
		return true; // return that we are OK with preferences
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}

}
