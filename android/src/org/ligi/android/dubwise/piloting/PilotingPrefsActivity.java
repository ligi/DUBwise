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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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

		
		CheckBoxPreference doExternControlCheckBoxPref = new CheckBoxPreference(this);
		doExternControlCheckBoxPref.setKey(PilotingPrefs.KEY_SENDEC);
		doExternControlCheckBoxPref.setTitle("Send Extern Control");
		doExternControlCheckBoxPref.setSummary("mixed with RC");
		doExternControlCheckBoxPref.setOnPreferenceChangeListener(this);
		miscPrefCat.addPreference(doExternControlCheckBoxPref);
		

		CheckBoxPreference doSerialChannelsCheckBoxPref = new CheckBoxPreference(this);
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
		leftPadHorizPrefCat.addPreference(leftPadHorizontalExternalControlMappingPref);

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
		leftPadVerticalPrefCat.addPreference(leftPadVerticalExternalControlMappingPref);

		
		
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
		rightPadHorizontalPrefCat.addPreference(rightPadHorizontalExternalControlMappingPref);

		

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
		rightPadVerticalPrefCat.addPreference(rightPadVerticalExternalControlMappingPref);


		return root;

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
		if ((preference==rightPadHorizontalExternalControlMappingPref)
				|| (preference==rightPadVerticalExternalControlMappingPref)
				|| (preference==leftPadHorizontalExternalControlMappingPref)
				|| (preference==leftPadVerticalExternalControlMappingPref))
			preference.setSummary((String)newValue);
		
		return true; // return that we are OK with preferences
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}

}
