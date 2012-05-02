package org.ligi.android.dubwise_mk.piloting;

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

import org.ligi.android.common.preferences.SetPreferenceEnabledByCheckBoxPreferenceState;
import org.ligi.android.dubwise_mk.helper.ActivityCalls;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;

public class PilotingPrefsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PilotingPrefs.init(this);
		ActivityCalls.beforeContent(this);
		super.onCreate(savedInstanceState);
		setPreferenceScreen(createPreferenceHierarchy());
	}

	@Override
	public void onResume() {
		ActivityCalls.afterContent(this);
		super.onResume();
	}

	private PreferenceScreen createPreferenceHierarchy() {
		// Root
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
		root.setPersistent(true);

		/* Misc */
		PreferenceCategory miscPrefCat = new PreferenceCategory(this);
		miscPrefCat.setTitle("Misc");
		root.addPreference(miscPrefCat);

		CheckBoxPreference showValuesCheckBoxPref = new CheckBoxPreference(this);
		showValuesCheckBoxPref.setKey(PilotingPrefs.KEY_SHOWVALUES);
		showValuesCheckBoxPref.setTitle("Show Values");
		showValuesCheckBoxPref.setSummary("Show Values on Top");
		miscPrefCat.addPreference(showValuesCheckBoxPref);

		
		CheckBoxPreference doExternControlCheckBoxPref = new CheckBoxPreference(this);
		doExternControlCheckBoxPref.setKey(PilotingPrefs.KEY_SENDEC);
		doExternControlCheckBoxPref.setTitle("Send Extern Control");
		doExternControlCheckBoxPref.setSummary("mixed with RC");
		miscPrefCat.addPreference(doExternControlCheckBoxPref);
		

		CheckBoxPreference doSerialChannelsCheckBoxPref = new CheckBoxPreference(this);
		doSerialChannelsCheckBoxPref.setKey(PilotingPrefs.KEY_SENDSC);
		doSerialChannelsCheckBoxPref.setTitle("Send Serial Channels");
		doSerialChannelsCheckBoxPref.setSummary("overrides RC");
		miscPrefCat.addPreference(doSerialChannelsCheckBoxPref);
		
		
		/* Left Pad Horizontal */
		PreferenceCategory leftPadHorizPrefCat = new PreferenceCategory(this);
		leftPadHorizPrefCat.setTitle("Left Pad Horizontal");
		root.addPreference(leftPadHorizPrefCat);

		CheckBoxPreference leftSpringHorizontalCheckBoxPref = new CheckBoxPreference(this);
		leftSpringHorizontalCheckBoxPref.setKey(PilotingPrefs.KEY_LEFT_PAD_SPRING_HORIZONTAL);
		leftSpringHorizontalCheckBoxPref.setTitle("Spring");
		leftSpringHorizontalCheckBoxPref.setSummary("automaticaly center Horizontaly");
		leftPadHorizPrefCat.addPreference(leftSpringHorizontalCheckBoxPref);

		
		ListPreference leftPadHorizontalExternalControlMappingPref = new ListPreference(this);
		leftPadHorizontalExternalControlMappingPref.setEntries(PilotingPrefs.getExternControlMappingStrings());
		leftPadHorizontalExternalControlMappingPref.setEntryValues(PilotingPrefs.getExternControlMappingStrings());
		leftPadHorizontalExternalControlMappingPref.setDialogTitle("External Control Mapping");
		leftPadHorizontalExternalControlMappingPref.setKey(PilotingPrefs.KEY_LEFT_PAD_EC_MAPPING_HORIZONTAL);
		leftPadHorizontalExternalControlMappingPref.setTitle("External Control Mapping");
		leftPadHorizontalExternalControlMappingPref.setDefaultValue(PilotingPrefs.getLeftPadHorizontalECMappingStr());
		leftPadHorizontalExternalControlMappingPref.setEnabled(PilotingPrefs.isExternControlEnabled());
		
		leftPadHorizPrefCat.addPreference(leftPadHorizontalExternalControlMappingPref);
		
		ListPreference leftPadHorizontalSerialChannelMappingPref = new ListPreference(this);
		leftPadHorizontalSerialChannelMappingPref.setEntries(PilotingPrefs.getSerialChannelMappingStrings());
		leftPadHorizontalSerialChannelMappingPref.setEntryValues(PilotingPrefs.getSerialChannelMappingStrings());
		leftPadHorizontalSerialChannelMappingPref.setDialogTitle("Serial Channels Mapping");
		leftPadHorizontalSerialChannelMappingPref.setKey(PilotingPrefs.KEY_LEFT_PAD_SC_MAPPING_HORIZONTAL);
		leftPadHorizontalSerialChannelMappingPref.setTitle("Serial Channels Mapping");
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
		leftPadVerticalPrefCat.addPreference(leftSpringVerticalCheckBoxPref);

		ListPreference leftPadVerticalExternalControlMappingPref = new ListPreference(this);
		leftPadVerticalExternalControlMappingPref.setEntries(PilotingPrefs.getExternControlMappingStrings());
		leftPadVerticalExternalControlMappingPref.setEntryValues(PilotingPrefs.getExternControlMappingStrings());
		leftPadVerticalExternalControlMappingPref.setDialogTitle("External Control Mapping");
		leftPadVerticalExternalControlMappingPref.setKey(PilotingPrefs.KEY_LEFT_PAD_EC_MAPPING_VERTICAL);
		leftPadVerticalExternalControlMappingPref.setTitle("External Control Mapping");
		leftPadVerticalExternalControlMappingPref.setDefaultValue(PilotingPrefs.getLeftPadVerticalECMappingStr());
		leftPadVerticalExternalControlMappingPref.setEnabled(PilotingPrefs.isExternControlEnabled());
		
		leftPadVerticalPrefCat.addPreference(leftPadVerticalExternalControlMappingPref);

		ListPreference leftPadVerticalSerialChannelMappingPref = new ListPreference(this);
		leftPadVerticalSerialChannelMappingPref.setEntries(PilotingPrefs.getSerialChannelMappingStrings());
		leftPadVerticalSerialChannelMappingPref.setEntryValues(PilotingPrefs.getSerialChannelMappingStrings());
		leftPadVerticalSerialChannelMappingPref.setDialogTitle("Serial Channel Mapping");
		leftPadVerticalSerialChannelMappingPref.setKey(PilotingPrefs.KEY_LEFT_PAD_SC_MAPPING_VERTICAL);
		leftPadVerticalSerialChannelMappingPref.setTitle("Serial Channel  Mapping");
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
		rightPadHorizontalPrefCat.addPreference(rightSpringHorizontalCheckBoxPref);


		ListPreference rightPadHorizontalExternalControlMappingPref = new ListPreference(this);
		rightPadHorizontalExternalControlMappingPref.setEntries(PilotingPrefs.getExternControlMappingStrings());
		rightPadHorizontalExternalControlMappingPref.setEntryValues(PilotingPrefs.getExternControlMappingStrings());
		rightPadHorizontalExternalControlMappingPref.setDialogTitle("External Control Mapping");
		rightPadHorizontalExternalControlMappingPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_EC_MAPPING_HORIZONTAL);
		rightPadHorizontalExternalControlMappingPref.setTitle("External Control Mapping");

		rightPadHorizontalExternalControlMappingPref.setDefaultValue(PilotingPrefs.getRightPadHorizontalECMappingStr());
		rightPadHorizontalExternalControlMappingPref.setEnabled(PilotingPrefs.isExternControlEnabled());
		
		rightPadHorizontalPrefCat.addPreference(rightPadHorizontalExternalControlMappingPref);

		ListPreference rightPadHorizontalSerialChanelMappingPref = new ListPreference(this);
		rightPadHorizontalSerialChanelMappingPref.setEntries(PilotingPrefs.getSerialChannelMappingStrings());
		rightPadHorizontalSerialChanelMappingPref.setEntryValues(PilotingPrefs.getSerialChannelMappingStrings());
		rightPadHorizontalSerialChanelMappingPref.setDialogTitle("Serial Channel  Mapping");
		rightPadHorizontalSerialChanelMappingPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_SC_MAPPING_HORIZONTAL);
		rightPadHorizontalSerialChanelMappingPref.setTitle("Serial Channel  Mapping");
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
		rightPadVerticalPrefCat.addPreference(rightSpringVerticalCheckBoxPref);

		ListPreference rightPadVerticalExternalControlMappingPref = new ListPreference(this);
		rightPadVerticalExternalControlMappingPref.setEntries(PilotingPrefs.getExternControlMappingStrings());
		rightPadVerticalExternalControlMappingPref.setEntryValues(PilotingPrefs.getExternControlMappingStrings());
		rightPadVerticalExternalControlMappingPref.setDialogTitle("External Control Mapping");
		rightPadVerticalExternalControlMappingPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_EC_MAPPING_VERTICAL);
		rightPadVerticalExternalControlMappingPref.setTitle("External Control Mapping");
		rightPadVerticalExternalControlMappingPref.setDefaultValue(PilotingPrefs.getRightPadVerticalECMappingStr());
		rightPadVerticalExternalControlMappingPref.setEnabled(PilotingPrefs.isExternControlEnabled());
		rightPadVerticalPrefCat.addPreference(rightPadVerticalExternalControlMappingPref);

		ListPreference rightPadVerticalSerialChannelMappingPref = new ListPreference(this);
		rightPadVerticalSerialChannelMappingPref.setEntries(PilotingPrefs.getSerialChannelMappingStrings());
		rightPadVerticalSerialChannelMappingPref.setEntryValues(PilotingPrefs.getSerialChannelMappingStrings());
		rightPadVerticalSerialChannelMappingPref.setDialogTitle("Serial Channel  Mapping");
		rightPadVerticalSerialChannelMappingPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_SC_MAPPING_VERTICAL);
		rightPadVerticalSerialChannelMappingPref.setTitle("Serial Channel  Mapping");
		rightPadVerticalSerialChannelMappingPref.setDefaultValue(PilotingPrefs.getRightPadVerticalSCMappingStr());
		rightPadVerticalPrefCat.addPreference(rightPadVerticalSerialChannelMappingPref);

		new SetSummararyByListPreferenceValue(rightPadVerticalSerialChannelMappingPref);
		new SetSummararyByListPreferenceValue(leftPadVerticalSerialChannelMappingPref);
		
		new SetSummararyByListPreferenceValue(rightPadHorizontalSerialChanelMappingPref);
		new SetSummararyByListPreferenceValue(rightPadHorizontalExternalControlMappingPref);

		new SetSummararyByListPreferenceValue(leftPadVerticalSerialChannelMappingPref);
		new SetSummararyByListPreferenceValue(leftPadHorizontalSerialChannelMappingPref);
		
		new SetSummararyByListPreferenceValue(leftPadHorizontalSerialChannelMappingPref);
		new SetSummararyByListPreferenceValue(leftPadHorizontalExternalControlMappingPref);

		new SetPreferenceEnabledByCheckBoxPreferenceState(doSerialChannelsCheckBoxPref)
			.addPreference2SetEnable(rightPadVerticalSerialChannelMappingPref)
			.addPreference2SetEnable(leftPadVerticalSerialChannelMappingPref)
			.addPreference2SetEnable(rightPadHorizontalSerialChanelMappingPref)
			.addPreference2SetEnable(rightPadHorizontalSerialChanelMappingPref);
		
		new SetPreferenceEnabledByCheckBoxPreferenceState(doExternControlCheckBoxPref) 
			.addPreference2SetEnable(rightPadVerticalExternalControlMappingPref)
			.addPreference2SetEnable(leftPadVerticalExternalControlMappingPref)
			.addPreference2SetEnable(rightPadHorizontalExternalControlMappingPref)
			.addPreference2SetEnable(leftPadHorizontalExternalControlMappingPref);
		
		return root;

	}

	public class SetSummararyByListPreferenceValue implements OnPreferenceChangeListener {

		public SetSummararyByListPreferenceValue(ListPreference lp) {
			lp.setSummary(lp.getValue());
			lp.setOnPreferenceChangeListener(this);
		}
		
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			preference.setSummary((String)newValue);
			return true;
		}
		
	}
	
}
