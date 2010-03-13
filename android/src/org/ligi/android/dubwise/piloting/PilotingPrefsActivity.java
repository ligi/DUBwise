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


import org.ligi.android.dubwise.helper.ActivityCalls;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;

public class PilotingPrefsActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnSharedPreferenceChangeListener {

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

		/* Left Pad */
		PreferenceCategory leftPadPrefCat = new PreferenceCategory(this);
		leftPadPrefCat.setTitle("Left Pad");
		root.addPreference(leftPadPrefCat);

		CheckBoxPreference leftSpringHorizontalCheckBoxPref = new CheckBoxPreference(this);
		leftSpringHorizontalCheckBoxPref.setKey(PilotingPrefs.KEY_LEFT_PAD_SPRING_HORIZONTAL);
		leftSpringHorizontalCheckBoxPref.setTitle("Horizontal Spring");
		leftSpringHorizontalCheckBoxPref.setSummary("automaticaly center Horizontaly");
		leftSpringHorizontalCheckBoxPref.setOnPreferenceChangeListener(this);
		leftPadPrefCat.addPreference(leftSpringHorizontalCheckBoxPref);

		CheckBoxPreference leftSpringVerticalCheckBoxPref = new CheckBoxPreference(this);
		leftSpringVerticalCheckBoxPref.setKey(PilotingPrefs.KEY_LEFT_PAD_SPRING_VERTICAL);
		leftSpringVerticalCheckBoxPref.setTitle("Vertical Spring");
		leftSpringVerticalCheckBoxPref.setSummary("automaticaly center Verticaly");
		leftSpringVerticalCheckBoxPref.setOnPreferenceChangeListener(this);
		leftPadPrefCat.addPreference(leftSpringVerticalCheckBoxPref);

		/* Right Pad */
		PreferenceCategory rightPadPrefCat = new PreferenceCategory(this);
		rightPadPrefCat.setTitle("Right Pad");
		root.addPreference(rightPadPrefCat);

		CheckBoxPreference rightSpringHorizontalCheckBoxPref = new CheckBoxPreference(this);
		rightSpringHorizontalCheckBoxPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_SPRING_HORIZONTAL);
		rightSpringHorizontalCheckBoxPref.setTitle("Horizontal Spring");
		rightSpringHorizontalCheckBoxPref.setSummary("automaticaly center Horizontaly");
		rightSpringHorizontalCheckBoxPref.setOnPreferenceChangeListener(this);
		rightPadPrefCat.addPreference(rightSpringHorizontalCheckBoxPref);

		CheckBoxPreference rightSpringVerticalCheckBoxPref = new CheckBoxPreference(this);
		rightSpringVerticalCheckBoxPref.setKey(PilotingPrefs.KEY_RIGHT_PAD_SPRING_VERTICAL);
		rightSpringVerticalCheckBoxPref.setTitle("Vertical Spring");
		rightSpringVerticalCheckBoxPref.setSummary("automaticaly center Verticaly");
		rightSpringVerticalCheckBoxPref.setOnPreferenceChangeListener(this);
		rightPadPrefCat.addPreference(rightSpringVerticalCheckBoxPref);

		return root;

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		return true; // return that we are OK with preferences
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}

}
